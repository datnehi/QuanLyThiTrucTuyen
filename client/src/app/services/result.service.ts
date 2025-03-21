import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Result } from '../models/result';
import { forkJoin, map, Observable } from 'rxjs';
import { Account } from '../models/account';
import { Course } from '../models/course';

@Injectable({
  providedIn: 'root'
})
export class ResultService {
  userUrl = environment.apiUrl + 'accounts';
  resultUrl = environment.apiUrl + 'results';

  constructor(private http: HttpClient) { }

  getResultsWithCourses(maKiThi: string): Observable<Result[]> {
    return forkJoin({
      results: this.http.get<Result[]>(`${this.resultUrl}/${maKiThi}`),
      users: this.http.get<Account[]>(this.userUrl),
    }).pipe(
      map(({ results, users }) => {
        if (!results || !Array.isArray(results)) {
          return [];
        }
        const userMap = new Map(users.map(s => [s.id, s.hoten]));

        return results.map(results => ({
          ...results,
          hoten: userMap.get(results.id) || 'Không xác định',
        }));
      })
    );
  }
}
