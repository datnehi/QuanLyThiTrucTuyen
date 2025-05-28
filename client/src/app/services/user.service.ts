import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';
import { NguoiDung } from '../models/nguoidung';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) { }

  getUsersByMaMonHoc(maMonHoc: string): Observable<NguoiDung[]> {
    return this.http.get<{ data: NguoiDung[] }>(`${this.apiUrl}/${maMonHoc}`).pipe(
      map(res => res.data)
    );
  }

  getActiveUsers(): Observable<any> {
    return this.http.get(`${this.apiUrl}/active`);
  }

  createUser(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, user);
  }

  updateUser(id: string, user: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, user);
  }

  deleteUser(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  searchUsers(keyword: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/search?keyword=${keyword}`);
  }

  getUserById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  getUserIdFromSession(): string | null {
    const userJson = sessionStorage.getItem('user');
    if (!userJson) return null;

    try {
      const user = JSON.parse(userJson);
      return user.id || null;
    } catch (error) {
      console.error('Lỗi phân tích JSON từ sessionStorage:', error);
      return null;
    }
  }
}
