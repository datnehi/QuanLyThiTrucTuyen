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
    return this.http.get<{ data: Course[] }>(`${this.baseUrl}/all`).pipe(
      map(res => res.data)
    );
  }

  getCoursebyId(maMonHoc: string): Observable<Course> {
    return this.http.get<{ data: Course }>(`${this.baseUrl}/${maMonHoc}`).pipe(
      map(res => res.data)
    );
  }

  getSubjects(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/active`); // Chỉ lấy các môn học active
  }

  getActiveSubjects(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/active`);
  }

  createSubject(subjectData: Course): Observable<Course> {
    return this.http.post<Course>(this.baseUrl, subjectData);
  }

  updateSubject(mamonhoc: string, subjectData: Course): Observable<Course> {
    return this.http.put<Course>(`${this.baseUrl}/${mamonhoc}`, subjectData);
  }

  deleteSubject(mamonhoc: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${mamonhoc}`);
  }

  toggleSubjectStatus(mamonhoc: string): Observable<any> {
    return this.http.patch(`${this.baseUrl}/${mamonhoc}/toggle-status`, {});
  }

  searchSubjects(keyword: string): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/search?keyword=${keyword}`);
  }
}
