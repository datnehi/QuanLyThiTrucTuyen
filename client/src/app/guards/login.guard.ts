import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AccountService } from '../services/account.service';
import { map } from 'rxjs';

export const loginGuard: CanActivateFn = (route, state) => {
  if (typeof window !== 'undefined' && window.localStorage) {
    if (localStorage.getItem('isLoggedIn')) {
      return false; // Đã đăng nhập, không cho vào trang login
    }
  }
  return true; // Chưa đăng nhập, cho phép truy cập
};
