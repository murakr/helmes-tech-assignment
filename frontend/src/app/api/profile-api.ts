import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfile, UserProfileRequest } from './profile';

const PROFILE_URL = '/api/profile';

@Injectable({ providedIn: 'root' })
export class ProfileApi {
  private readonly http = inject(HttpClient);

  getProfile(): Observable<UserProfile | null> {
    return this.http.get<UserProfile | null>(PROFILE_URL);
  }

  saveProfile(request: UserProfileRequest): Observable<UserProfile> {
    return this.http.put<UserProfile>(PROFILE_URL, request);
  }
}
