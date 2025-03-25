import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Result } from '../models/result';
import { forkJoin, map, Observable } from 'rxjs';
import { Account } from '../models/account';

@Injectable({
  providedIn: 'root'
})
export class ResultService {
  userUrl = environment.apiUrl + 'accounts';
  resultUrl = environment.apiUrl + 'results';

  constructor(private http: HttpClient) { }

  getResultsWithUser(maKiThi: string, maMonHoc: string): Observable<any[]> {
    const params = new HttpParams()
      .set('maKiThi', maKiThi)
      .set('maMonHoc', maMonHoc);

    return this.http.get<any[]>(`${this.resultUrl}/getall`, { params }).pipe(
      map(results =>
        results?.map(result => ({
          ...result,
          id: result.id,
          hoten: result.hoten,
          diem: result.diem ?? null,
          thoiGianVaoThi: result.thoiGianVaoThi ?? null,
          thoiGianLamBai: result.thoiGianLamBai ?? null
        })))
    );
  }

  getResultByMaKiThiAndId(maKiThi: string, id: string): Observable<Result>{
    return this.http.get<Result>(`${this.resultUrl}/${maKiThi}/${id}`);
  }

  createResult(maKiThi: string, id: string): Observable<any[]> {
    const params = new HttpParams()
      .set('maKiThi', maKiThi)
      .set('id', id);
      return this.http.post<any[]>(`${this.resultUrl}/create-exam`, {}, { params });
  }

  submitExam(data: any): Observable<any> {
    return this.http.post(`${this.resultUrl}/submit-exam`, data);
  }

}
