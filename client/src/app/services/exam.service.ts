import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Exam } from '../models/exam';

@Injectable({
  providedIn: 'root'
})
export class ExamService {
  baseUrl = environment.apiUrl + 'exams';

  constructor(private http: HttpClient) { }

  getExams(): Observable<Exam[]> {
    return this.http.get<Exam[]>(this.baseUrl);
  }

  getExambyId(maKiThi: string): Observable<Exam> {
    return this.http.get<Exam>(`${this.baseUrl}/${maKiThi}`);
  }

  createExam(exam: Exam): Observable<Exam> {
    return this.http.post<Exam>(this.baseUrl, exam);
  }

  updateExam(maKiThi: string, exam: Exam): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${maKiThi}`, exam);
  }

  deleteExam(maKiThi: string): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl + '/delete'}/${maKiThi}`);
  }
}
