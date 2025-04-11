import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Result } from '../models/result';
import { map, Observable } from 'rxjs';

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

    return this.http.get<any>(`${this.resultUrl}/getall`, { params }).pipe(
      map(response => {
        const results = response.data || [];
        return results.map((result: any) => ({
          ...result,
          id: result.id,
          hoten: result.hoten,
          diem: result.diem ?? null,
          thoiGianVaoThi: result.thoiGianVaoThi ?? null,
          thoiGianLamBai: result.thoiGianLamBai ?? null
        }));
      })
    );
  }

  getResultByMaKiThiAndId(maKiThi: string, id: string): Observable<Result>{
    return this.http.get<{data: Result}>(`${this.resultUrl}/${maKiThi}/${id}`).pipe(
      map(res => res.data)
    );
  }

  createResult(maKiThi: string, id: string): Observable<any[]> {
    const params = new HttpParams()
      .set('maKiThi', maKiThi)
      .set('id', id);
      return this.http.post<{data: any[]}>(`${this.resultUrl}/create-exam`, {}, { params }).pipe(
        map(res => res.data)
      );
  }

  submitExam(data: any): Observable<any> {
    return this.http.post(`${this.resultUrl}/submit-exam`, data);
  }

}
