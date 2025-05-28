import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable } from 'rxjs';
import { Exam } from '../models/exam';
import { Course } from '../models/course';

@Injectable({
  providedIn: 'root'
})
export class ExamService {
  baseUrl = environment.apiUrl + 'exams';
  courseUrl = environment.apiUrl + 'courses';

  constructor(private http: HttpClient) { }

  getExams(): Observable<Exam[]> {
    return this.http.get<{data: Exam[]}>(this.baseUrl).pipe(
      map(res => res.data)
    );
  }

  getExambyMa(maKiThi: string): Observable<Exam> {
    return this.http.get<{data: Exam}>(`${this.baseUrl}/${maKiThi}`).pipe(
      map(res => res.data)
    );
  }

  getExamsByMaMonHoc(maMonHoc: string): Observable<Exam[]> {
    return this.http.get<{data: Exam[]}>(`${this.baseUrl}/mamonhoc/${maMonHoc}`).pipe(
      map(res => res.data)
    );
  }

  createExam(exam: Exam): Observable<Exam> {
    return this.http.post<Exam>(this.baseUrl, exam);
  }

  updateExam(maKiThi: string, exam: Exam): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${maKiThi}`, exam);
  }

  deleteExam(maKiThi: string): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${maKiThi}`);
  }

  getExamsWithCourses(): Observable<Exam[]> {
    return forkJoin({
      exams: this.http.get<{ data: Exam[] }>(this.baseUrl).pipe(
        map(res => res.data)
      ),
      courses: this.http.get<{ data: Course[] }>(`${this.courseUrl}/all`).pipe(
        map(res => res.data)
      )
    }).pipe(
      map(({ exams, courses }) => {
        const courseMap = new Map(courses.map(s => [s.maMonHoc, s.tenMonHoc]));
        return exams.map(exam => ({
          ...exam,
          tenMonHoc: courseMap.get(exam.maMonHoc) || 'Không xác định'
        }));
      })
    );
  }

  getExamsWithCoursesById(id: string): Observable<Exam[]> {
    return forkJoin({
      exams: this.http.get<{ data: Exam[] }>(`${this.baseUrl}/sinhvien/${id}`).pipe(
        map(res => res.data)
      ),
      courses: this.http.get<{ data: Course[] }>(`${this.courseUrl}/all`).pipe(
        map(res => res.data)
      )
    }).pipe(
      map(({ exams, courses }) => {
        const courseMap = new Map(courses.map(s => [s.maMonHoc, s.tenMonHoc]));
        return exams.map(exam => ({
          ...exam,
          tenMonHoc: courseMap.get(exam.maMonHoc) || 'Không xác định'
        }));
      })
    );
  }
}
