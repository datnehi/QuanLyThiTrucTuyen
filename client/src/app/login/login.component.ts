import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  model: any = {};
  idError: string = '';
  matkhauError: string = '';

  constructor(
    public accountService: AccountService,
    private router: Router
  ) {}

  login() {
    this.idError = '';
    this.matkhauError = '';

    let isValid = true;

    if (!this.model.id || !/^\d{7}$/.test(this.model.id)) {
      this.idError = 'Mã sinh viên phải gồm đúng 7 chữ số';
      isValid = false;
    }

    if (!this.model.matkhau) {
      this.matkhauError = 'Mật khẩu không được để trống';
      isValid = false;
    }

    if (!isValid) {
      console.warn('Form không hợp lệ');
      return;
    }

    // ✅ Gọi API nếu hợp lệ
    this.accountService.login(this.model).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: err => console.error('Lỗi đăng nhập:', err)
    });
  }
}
