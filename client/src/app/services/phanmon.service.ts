import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course } from '../models/course';

@Injectable({
  providedIn: 'root'
})
export class PhanmonService {
  private baseUrl = environment.apiUrl + 'phanmon';

  constructor(private http: HttpClient) {}

  getMonHocBySinhVien(id: string): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/${id}`);
  }

  addPhanMon(id: string, mamonhoc: string): Observable<string> {
    const params = new HttpParams()
      .set('id', id)
      .set('mamonhoc', mamonhoc);

    return this.http.post<string>(`${this.baseUrl}/add`, {}, { params });
  }
}
