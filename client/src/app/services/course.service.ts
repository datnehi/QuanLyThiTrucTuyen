import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Course } from '../models/course';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  baseUrl = environment.apiUrl + 'courses';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Course[]> {
    return this.http.get<{data: Course[]}>(this.baseUrl).pipe(
      map(res => res.data)
    );
  }

  getCoursebyId(maMonHoc: string): Observable<Course> {
    return this.http.get<{data: Course}>(`${this.baseUrl}/${maMonHoc}`).pipe(
      map(res => res.data)
    );
  }
}
