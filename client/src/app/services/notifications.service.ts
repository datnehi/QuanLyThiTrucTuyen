import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Notification } from '../models/notification';
import { environment } from '../../environments/environment';
import { forkJoin, map, Observable } from 'rxjs';
import { Course } from '../models/course';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {
  baseUrl = environment.apiUrl + 'notifications';
  courseUrl = environment.apiUrl + 'courses'

  constructor(private http: HttpClient) { }

  getAll(): Observable<Notification[]> {
    return this.http.get<{ data: Notification[] }>(this.baseUrl).pipe(
      map(res => res.data)
    );
  }

  getNotificationbyMaThongBao(maThongBao: string): Observable<Notification> {
    return this.http.get<{ data: Notification }>(`${this.baseUrl}/${maThongBao}`).pipe(
      map(res => res.data)
    );
  }

  getNotificationbyMaMonHoc(maMonHoc: string): Observable<Notification[]> {
    return this.http.get<{ data: Notification[] }>(`${this.baseUrl}/mamonhoc/${maMonHoc}`).pipe(
      map(res => res.data)
    );
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

  getNotificationWithCourses(): Observable<Notification[]> {
    return forkJoin({
      notifications: this.http.get<{ data: Notification[] }>(this.baseUrl).pipe(
        map(res => res.data)
      ),
      courses: this.http.get<{ data: Course[] }>(this.courseUrl).pipe(
        map(res => res.data)
      )
    }).pipe(
      map(({ notifications, courses }) => {
        const courseMap = new Map(courses.map(s => [s.maMonHoc, s.tenMonHoc]));
        return notifications.map(notification => ({
          ...notification,
          tenMonHoc: courseMap.get(notification.maMonHoc) || 'Không xác định'
        }));
      })
    );
  }
}
