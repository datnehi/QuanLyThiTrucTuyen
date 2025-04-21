import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Notification } from '../models/notification';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {
  baseUrl = environment.apiUrl + 'notifications';

  constructor(private http: HttpClient) { }

  getNotification(): Observable<Notification[]>{
     return this.http.get<Notification[]>(this.baseUrl);
  }
  
  getNotificationbyId(maThongBao: string): Observable<Notification[]>{
      return this.http.get<Notification[]>(`${this.baseUrl}/${maThongBao}`);
  }
  createNotification(Notification: Notification): Observable<Notification> {
    return this.http.post<Notification>(this.baseUrl, Notification);
  }

  updateNotification(maThongBao: string, Notification: Notification): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${maThongBao}`, Notification);
  }

  deleteNotification(maThongBao: string): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${maThongBao}`);
  }
}
