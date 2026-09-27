import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Sector } from './sector';

@Injectable({ providedIn: 'root' })
export class SectorApi {
  private readonly http = inject(HttpClient);

  getSectors(): Observable<Sector[]> {
    return this.http.get<Sector[]>('/api/sectors');
  }
}
