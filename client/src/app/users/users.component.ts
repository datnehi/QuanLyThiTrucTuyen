import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-users',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  message: string = '';
  isSuccess: boolean = false;
  userId: string = '';

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) {
    // lấy id từ secsion storage
    const id = this.userService.getUserIdFromSession();
    if (id) {
      this.userId = id;
    } else {
      this.message = 'Không tìm thấy thông tin người dùng';
    }
  }


  onSubmit() {
    // Validate trước khi gửi request
    if (!this.currentPassword || !this.newPassword || !this.confirmPassword) {
      this.message = 'Vui lòng điền đầy đủ thông tin';
      this.isSuccess = false;
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.message = 'Mật khẩu mới và xác nhận mật khẩu không khớp';
      this.isSuccess = false;
      return;
    }

    if (this.newPassword.length > 50) {
      this.message = 'Mật khẩu mới không được vượt quá 50 ký tự';
      this.isSuccess = false;
      return;
    }

    if (this.currentPassword === this.newPassword) {
      this.message = 'Mật khẩu mới phải khác mật khẩu hiện tại';
      this.isSuccess = false;
      return;
    }

    // Gửi request phù hợp với API backend, lười chỉnh back nên dùng query param /api/user/{id}/password?currentPassword=...&newPassword=...
    this.http.put(`http://localhost:8080/api/user/${this.userId}/password`,
      null, // Không cần body
      {
        params: {
          currentPassword: this.currentPassword,
          newPassword: this.newPassword
        }
      }
    ).subscribe({
      next: () => {
        this.message = 'Đổi mật khẩu thành công';
        this.isSuccess = true;
        this.resetForm();
      },
      error: () => {
        this.message = 'Đổi mật khẩu thất bại';
        this.isSuccess = false;
      }
    });
  }

  resetForm() {
    this.currentPassword = '';
    this.newPassword = '';
    this.confirmPassword = '';
  }
}

