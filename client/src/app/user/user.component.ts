import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    RouterModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {
  nguoiDungList: any[] = [];
  apiUrl = 'http://localhost:8080/api/nguoidung';

  formUser: any = {};
  selectedUser: any = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getAllActiveUsers();
  }

  getAllActiveUsers() {
    this.http.get<any[]>(`${this.apiUrl}/active`).subscribe({
      next: (data) => {
        this.nguoiDungList = data;
      },
      error: (err) => {
        console.error('Lỗi khi tải danh sách người dùng active:', err);
      }
    });
  }

  deleteUser(id: string): Promise<void> {
    return new Promise((resolve, reject) => {
      if (confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
        this.http.delete(`${this.apiUrl}/${id}`).subscribe({
          next: () => {
            this.getAllActiveUsers(); // Sửa lại thành getAllActiveUsers
            resolve();
          },
          error: (err) => {
            console.error(err);
            reject(err);
          }
        });
      } else {
        reject('User cancelled');
      }
    });
  }

  editUser(user: any) {
    this.selectedUser = user;
    this.formUser = { ...user };
    const modal = new (window as any).bootstrap.Modal(document.getElementById('addUserModal'));
    modal.show();
  }

  onSubmit() {
    // Tạo bản sao của formUser và chuyển đổi các trường cần thiết
    const userToSend = {
      ...this.formUser,
      active: this.convertGenderToBoolean(this.formUser.active),
      gioiTinh: this.convertGenderToBoolean(this.formUser.gioiTinh)
    };

    if (this.selectedUser) {
      this.http.put(`${this.apiUrl}/${this.selectedUser.id}`, userToSend).subscribe({
        next: (data) => {
          this.getAllActiveUsers();
          this.resetForm();
          this.closeModal();
        },
        error: (err) => {
          console.error('Lỗi khi cập nhật người dùng:', err);
          console.log('Dữ liệu gửi đi:', userToSend);
        }
      });
    } else {
      this.http.post(this.apiUrl, userToSend).subscribe({
        next: (data) => {
          this.getAllActiveUsers();
          this.resetForm();
          this.closeModal();
        },
        error: (err) => {
          console.error('Lỗi khi thêm người dùng mới:', err);
          console.log('Dữ liệu gửi đi:', userToSend);
        }
      });
    }
  }
  
  // Hàm chuyển đổi giá trị sang boolean
  private convertGenderToBoolean(gender: any): boolean {
    if (typeof gender === 'boolean') return gender;
    if (typeof gender === 'string') {
      return gender.toLowerCase() === 'nam' || gender === 'true';
    }
    return false; 
  }
  resetForm() {
    this.formUser = {};
    this.selectedUser = null;
  } 

  closeModal() {
    const modalElement = document.getElementById('addUserModal');
    const modalInstance = (window as any).bootstrap.Modal.getInstance(modalElement);
    if (modalInstance) {
      modalInstance.hide();
    }
  }
}