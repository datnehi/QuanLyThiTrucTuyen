import { Injectable } from '@angular/core';
import { ChiTietBaiThi } from '../models/chitietbaithi';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChitietbaithiService {
  baseUrl = environment.apiUrl + 'chitietbaithi';

  constructor(private http: HttpClient) { }

  getChiTietBaiLam(maKetQua: string): Observable<ChiTietBaiThi[]> {
    return this.http.get<ChiTietBaiThi[]>(`${this.baseUrl}/${maKetQua}`);
  }

  saveAnswer(body: any): Observable<any>{
    return this.http.post<any>(this.baseUrl + '/save', body);
  }
}
