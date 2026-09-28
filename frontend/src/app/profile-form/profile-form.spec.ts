import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { UserProfile } from '../api/profile';
import { ProfileApi } from '../api/profile-api';
import { Sector } from '../api/sector';
import { SectorApi } from '../api/sector-api';
import { ProfileForm } from './profile-form';

const SECTORS: Sector[] = [
  { id: 1, name: 'Manufacturing', level: 0 },
  { id: 19, name: 'Construction materials', level: 1 },
];

const SAVED_PROFILE: UserProfile = {
  id: 1,
  name: 'testUser',
  sectorIds: [19],
  agreedToTerms: true,
};

describe('ProfileForm', () => {
  let fixture: ComponentFixture<ProfileForm>;
  let page: HTMLElement;
  let sectorApi: { getSectors: ReturnType<typeof vi.fn> };
  let profileApi: { getProfile: ReturnType<typeof vi.fn>; saveProfile: ReturnType<typeof vi.fn> };

  const nameInput = () => page.querySelector<HTMLInputElement>('#name')!;
  const termsCheckbox = () => page.querySelector<HTMLInputElement>('.checkbox input')!;
  const saveButton = () => page.querySelector<HTMLButtonElement>('button[type="submit"]')!;
  const sectorCheckbox = (name: string) =>
    Array.from(page.querySelectorAll<HTMLLabelElement>('.sector'))
      .find((row) => row.textContent?.includes(name))!
      .querySelector('input')!;
  const fieldErrors = () =>
    Array.from(page.querySelectorAll('.field-error')).map((error) => error.textContent?.trim());

  const render = async () => {
    fixture.detectChanges();
    await fixture.whenStable();
  };

  const typeName = (value: string) => {
    nameInput().value = value;
    nameInput().dispatchEvent(new Event('input'));
  };

  const fillInValidForm = () => {
    typeName('  testUser ');
    sectorCheckbox('Construction materials').click();
    termsCheckbox().click();
  };

  const createComponent = async () => {
    fixture = TestBed.createComponent(ProfileForm);
    page = fixture.nativeElement;
    await render();
  };

  beforeEach(() => {
    sectorApi = { getSectors: vi.fn(() => of(SECTORS)) };
    profileApi = { getProfile: vi.fn(() => of(null)), saveProfile: vi.fn() };

    TestBed.configureTestingModule({
      imports: [ProfileForm],
      providers: [
        { provide: SectorApi, useValue: sectorApi },
        { provide: ProfileApi, useValue: profileApi },
      ],
    });
  });

  it('shows an empty form for a new session', async () => {
    await createComponent();

    expect(nameInput().value).toBe('');
    expect(sectorCheckbox('Manufacturing').checked).toBe(false);
    expect(termsCheckbox().checked).toBe(false);
  });

  it('refills the form with the profile saved in this session', async () => {
    profileApi.getProfile.mockReturnValue(of(SAVED_PROFILE));
    await createComponent();

    expect(nameInput().value).toBe('testUser');
    expect(sectorCheckbox('Construction materials').checked).toBe(true);
    expect(sectorCheckbox('Manufacturing').checked).toBe(false);
    expect(termsCheckbox().checked).toBe(true);
  });

  it('shows every error and does not save when the form is empty', async () => {
    await createComponent();

    saveButton().click();
    await render();

    expect(fieldErrors()).toEqual([
      'Name is required.',
      'Select at least one sector.',
      'You must agree to the terms.',
    ]);
    expect(profileApi.saveProfile).not.toHaveBeenCalled();
    expect(document.activeElement).toBe(nameInput());
  });

  it('saves valid input and shows the data the server stored', async () => {
    profileApi.saveProfile.mockReturnValue(of(SAVED_PROFILE));
    await createComponent();

    fillInValidForm();
    saveButton().click();
    await render();

    expect(profileApi.saveProfile).toHaveBeenCalledWith({
      name: '  testUser ',
      sectorIds: [19],
      agreedToTerms: true,
    });
    expect(nameInput().value).toBe('testUser');
    expect(page.textContent).toContain('Your data has been saved.');
  });

  it('shows server validation errors under the matching field', async () => {
    profileApi.saveProfile.mockReturnValue(
      throwError(
        () =>
          new HttpErrorResponse({
            status: 400,
            error: { title: 'Bad Request', status: 400, errors: { name: 'Name is required!' } },
          }),
      ),
    );
    await createComponent();

    fillInValidForm();
    saveButton().click();
    await render();

    expect(fieldErrors()).toEqual(['Name is required!']);
    expect(page.textContent).toContain('Please correct the highlighted fields.');
  });

  it('offers a retry when loading fails', async () => {
    sectorApi.getSectors
      .mockReturnValueOnce(throwError(() => new HttpErrorResponse({ status: 500 })))
      .mockReturnValue(of(SECTORS));
    await createComponent();
    expect(page.textContent).toContain('The form could not be loaded.');

    page.querySelector<HTMLButtonElement>('[role="alert"] button')!.click();
    await render();

    expect(nameInput()).not.toBeNull();
    expect(sectorApi.getSectors).toHaveBeenCalledTimes(2);
  });
});
