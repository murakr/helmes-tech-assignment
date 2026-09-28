import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Sector } from './sector';
import { SectorApi } from './sector-api';

describe('SectorApi', () => {
  let api: SectorApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(SectorApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('loads the sectors from the backend', () => {
    const sectors: Sector[] = [
      { id: 1, name: 'Manufacturing', level: 0 },
      { id: 19, name: 'Construction materials', level: 1 },
    ];
    let result: Sector[] | undefined;

    api.getSectors().subscribe((response) => (result = response));
    http.expectOne({ method: 'GET', url: '/api/sectors' }).flush(sectors);

    expect(result).toEqual(sectors);
  });
});
