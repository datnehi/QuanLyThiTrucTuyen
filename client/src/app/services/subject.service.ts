import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subject } from '../models/subject';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  private apiUrl = 'http://localhost:8080/api/subjects';

  constructor(private http: HttpClient) { }

  getSubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.apiUrl}/active`); // Chỉ lấy các môn học active
  }

  getActiveSubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.apiUrl}/active`);
  }

  createSubject(subjectData: Subject): Observable<Subject> {
    return this.http.post<Subject>(this.apiUrl, subjectData);
  }

  updateSubject(mamonhoc: string, subjectData: Subject): Observable<Subject> {
    return this.http.put<Subject>(`${this.apiUrl}/${mamonhoc}`, subjectData);
  }

  deleteSubject(mamonhoc: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${mamonhoc}`);
  }

  toggleSubjectStatus(mamonhoc: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${mamonhoc}/toggle-status`, {});
  }

  searchSubjects(keyword: string): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }
}