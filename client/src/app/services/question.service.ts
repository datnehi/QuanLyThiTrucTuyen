import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private apiUrl = 'http://localhost:8080/api/questions';

  constructor(private http: HttpClient) { }

  // Thêm các phương thức này
  getQuestions(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createQuestion(questionData: any): Observable<any> {
    // Đảm bảo trạng thái mặc định là false (0)
    questionData.trangthai = false;
    return this.http.post(this.apiUrl, questionData);
  }

  deleteQuestion(macauhoi: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${macauhoi}`);
  }

  // Thêm các phương thức khác nếu cần
  getQuestionsBySubject(subjectId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/subject/${subjectId}`);
  }

  searchQuestions(keyword: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search?keyword=${keyword}`);
  }

  // Thêm phương thức này vào QuestionService
  saveAnswers(answers: any[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/answers`, answers);
  }

  //chỉnh sửa câu hỏi
  // Thêm các phương thức sau vào QuestionService
  getAnswersByQuestionId(macauhoi: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${macauhoi}/answers`);
  }

  updateQuestion(macauhoi: string, questionData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${macauhoi}`, questionData);
  }

  updateAnswers(macauhoi: string, answers: any[]): Observable<any> {
    return this.http.put(`${this.apiUrl}/${macauhoi}/answers`, answers);
  }
}
