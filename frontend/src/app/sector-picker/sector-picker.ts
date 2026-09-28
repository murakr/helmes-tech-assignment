import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  forwardRef,
  input,
  signal,
  viewChildren,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Sector } from '../api/sector';

@Component({
  selector: 'app-sector-picker',
  templateUrl: './sector-picker.html',
  styleUrl: './sector-picker.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SectorPicker),
      multi: true,
    },
  ],
})
export class SectorPicker implements ControlValueAccessor {
  readonly sectors = input.required<readonly Sector[]>();

  protected readonly selectedIds = signal<ReadonlySet<number>>(new Set());
  protected readonly disabled = signal(false);

  private readonly checkboxes = viewChildren<ElementRef<HTMLInputElement>>('checkbox');

  private onChange: (sectorIds: number[]) => void = () => {};
  private onTouched: () => void = () => {};

  writeValue(sectorIds: number[] | null): void {
    this.selectedIds.set(new Set(sectorIds ?? []));
  }

  registerOnChange(onChange: (sectorIds: number[]) => void): void {
    this.onChange = onChange;
  }

  registerOnTouched(onTouched: () => void): void {
    this.onTouched = onTouched;
  }

  setDisabledState(disabled: boolean): void {
    this.disabled.set(disabled);
  }

  protected toggle(sectorId: number, selected: boolean): void {
    const selection = new Set(this.selectedIds());
    if (selected) {
      selection.add(sectorId);
    } else {
      selection.delete(sectorId);
    }
    this.updateSelection(selection);
  }

  protected clearAll(): void {
    this.updateSelection(new Set());
    this.checkboxes()[0]?.nativeElement.focus();
  }

  private updateSelection(selection: ReadonlySet<number>): void {
    this.selectedIds.set(selection);
    this.onChange([...selection]);
    this.onTouched();
  }
}
