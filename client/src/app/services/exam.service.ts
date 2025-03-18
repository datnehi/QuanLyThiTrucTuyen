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
  courseUrl = environment.apiUrl + 'courses'

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

  getExamsWithCourses(): Observable<Exam[]> {
    return forkJoin({
      exams: this.http.get<Exam[]>(this.baseUrl),
      courses: this.http.get<Course[]>(this.courseUrl)
    }).pipe(
      map(({ exams, courses }) => {
        // Chuyển danh sách subjects thành object để tra cứu nhanh
        const courseMap = new Map(courses.map(s => [s.maMonHoc, s.tenMonHoc]));

        // Thêm `tenMonHoc` vào mỗi exam
        return exams.map(exam => ({
          ...exam,
          tenMonHoc: courseMap.get(exam.maMonHoc) || 'Không xác định'
        }));
      })
    );
  }
}
