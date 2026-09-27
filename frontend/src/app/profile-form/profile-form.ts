import { ChangeDetectionStrategy, Component, computed, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { HttpErrorResponse } from '@angular/common/http';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize, forkJoin } from 'rxjs';
import { ProblemDetail } from '../api/problem-detail';
import { UserProfile } from '../api/profile';
import { ProfileApi } from '../api/profile-api';
import { Sector } from '../api/sector';
import { SectorApi } from '../api/sector-api';
import { notBlank } from './validators';

const NAME_MAX_LENGTH = 100;
const INDENT_PER_LEVEL = '\u00A0'.repeat(4);

type FormField = 'name' | 'sectorIds' | 'agreedToTerms';

const ERROR_MESSAGES: Record<FormField, Record<string, string>> = {
  name: {
    required: 'Name is required.',
    blank: 'Name is required.',
    maxlength: `Name must be at most ${NAME_MAX_LENGTH} characters.`,
  },
  sectorIds: {
    required: 'Select at least one sector.',
  },
  agreedToTerms: {
    required: 'You must agree to the terms.',
  },
};

interface StatusMessage {
  readonly kind: 'success' | 'error';
  readonly text: string;
}

@Component({
  selector: 'app-profile-form',
  imports: [ReactiveFormsModule],
  templateUrl: './profile-form.html',
  styleUrl: './profile-form.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProfileForm implements OnInit {
  private readonly sectorApi = inject(SectorApi);
  private readonly profileApi = inject(ProfileApi);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly nameMaxLength = NAME_MAX_LENGTH;

  protected readonly form = inject(NonNullableFormBuilder).group({
    name: ['', [Validators.required, notBlank, Validators.maxLength(NAME_MAX_LENGTH)]],
    sectorIds: [[] as number[], Validators.required],
    agreedToTerms: [false, Validators.requiredTrue],
  });

  protected readonly loadState = signal<'loading' | 'loaded' | 'failed'>('loading');
  protected readonly saving = signal(false);
  protected readonly statusMessage = signal<StatusMessage | null>(null);

  private readonly sectors = signal<readonly Sector[]>([]);
  protected readonly sectorOptions = computed(() =>
    this.sectors().map((sector) => ({
      id: sector.id,
      label: INDENT_PER_LEVEL.repeat(sector.level) + sector.name,
    })),
  );

  ngOnInit(): void {
    this.load();
  }

  protected load(): void {
    this.loadState.set('loading');
    forkJoin({ sectors: this.sectorApi.getSectors(), profile: this.profileApi.getProfile() })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: ({ sectors, profile }) => {
          this.sectors.set(sectors);
          if (profile) {
            this.fillForm(profile);
          }
          this.loadState.set('loaded');
        },
        error: () => this.loadState.set('failed'),
      });
  }

  protected save(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }

    this.saving.set(true);
    this.statusMessage.set(null);
    this.profileApi
      .saveProfile(this.form.getRawValue())
      .pipe(
        finalize(() => this.saving.set(false)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (profile) => {
          this.fillForm(profile);
          this.statusMessage.set({
            kind: 'success',
            text: 'Your data has been saved. You can edit it and save again.',
          });
        },
        error: (error: HttpErrorResponse) => this.showSaveError(error),
      });
  }

  protected errorFor(field: FormField): string | null {
    const control = this.form.controls[field];
    if (!control.invalid || !control.touched) {
      return null;
    }
    const serverMessage: string | undefined = control.getError('server');
    if (serverMessage) {
      return serverMessage;
    }
    const failedRule = Object.keys(control.errors ?? {})[0];
    return ERROR_MESSAGES[field][failedRule] ?? 'Invalid value.';
  }

  private fillForm(profile: UserProfile): void {
    this.form.setValue({
      name: profile.name,
      sectorIds: [...profile.sectorIds],
      agreedToTerms: profile.agreedToTerms,
    });
    this.form.markAsPristine();
    this.form.markAsUntouched();
  }

  private showSaveError(error: HttpErrorResponse): void {
    const problem = error.error as ProblemDetail | null;
    const fieldErrors = problem?.errors ?? {};
    for (const [field, message] of Object.entries(fieldErrors)) {
      this.form.get(field)?.setErrors({ server: message });
    }
    const hasFieldErrors = Object.keys(fieldErrors).length > 0;
    this.statusMessage.set({
      kind: 'error',
      text: hasFieldErrors
        ? 'Please correct the highlighted fields.'
        : (problem?.detail ?? 'Saving failed. Please try again.'),
    });
  }
}
