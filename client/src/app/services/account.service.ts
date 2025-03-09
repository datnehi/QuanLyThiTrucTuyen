import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { BehaviorSubject, map } from 'rxjs';
import { User } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  baseUrl = environment.apiUrl;
  private userTokenSubject = new BehaviorSubject<string | null>(sessionStorage.getItem('userToken'));
  userToken$ = this.userTokenSubject.asObservable();
  private userInfoSubject = new BehaviorSubject<any>(JSON.parse(sessionStorage.getItem('user') || 'null'));
  userInfo$ = this.userInfoSubject.asObservable();

  constructor(private http: HttpClient) { }

  login(model: any) {
    return this.http.post<any>(this.baseUrl + 'login', model).pipe(
      map(response => {
        if (response && response.userToken && response.user) {
          this.setUser(response.userToken, response.user);
        }
      })
    );
  }

  setUser(token: string, user: any) {
    sessionStorage.setItem('userToken', token);
    sessionStorage.setItem('user', JSON.stringify(user));
    this.userTokenSubject.next(token);
    this.userInfoSubject.next(user);
  }

  clearUser() {
    sessionStorage.removeItem('userToken');
    sessionStorage.removeItem('user');
    this.userTokenSubject.next(null);
    this.userInfoSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!sessionStorage.getItem('userToken');
  }

  logout() {
    return this.http.post(this.baseUrl + 'logout', {}).pipe(
      map(response => {
        if (response) {
          this.clearUser();
        }
      })
    );
  }

}
