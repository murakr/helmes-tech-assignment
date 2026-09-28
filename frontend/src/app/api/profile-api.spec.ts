import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { UserProfile, UserProfileRequest } from './profile';
import { ProfileApi } from './profile-api';

describe('ProfileApi', () => {
  const request: UserProfileRequest = { name: 'testUser', sectorIds: [1, 19], agreedToTerms: true };
  const profile: UserProfile = { id: 1, ...request };

  let api: ProfileApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(ProfileApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('returns the profile saved in this session', () => {
    let result: UserProfile | null | undefined;

    api.getProfile().subscribe((response) => (result = response));
    http.expectOne({ method: 'GET', url: '/api/profile' }).flush(profile);

    expect(result).toEqual(profile);
  });

  it('returns null when the session has no profile yet', () => {
    let result: UserProfile | null | undefined;

    api.getProfile().subscribe((response) => (result = response));
    http
      .expectOne({ method: 'GET', url: '/api/profile' })
      .flush(null, { status: 204, statusText: 'No Content' });

    expect(result).toBeNull();
  });

  it('saves the profile with a PUT request', () => {
    let result: UserProfile | undefined;

    api.saveProfile(request).subscribe((response) => (result = response));
    const call = http.expectOne({ method: 'PUT', url: '/api/profile' });
    expect(call.request.body).toEqual(request);
    call.flush(profile);

    expect(result).toEqual(profile);
  });
});
