import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserService } from '../services/user.service';

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
  allActiveUsers: any[] = [];
  apiUrl = 'http://localhost:8080/api/user';
  formUser: any = {};
  selectedUser: any = null;
  keyword: string = '';
  searched: boolean = false;
  userId: string = '';
  modalInstance: any;

  constructor(private http: HttpClient, private userService: UserService) {
    // lấy id từ secsion storage
    const id = this.userService.getUserIdFromSession();
    if (id) {
      this.userId = id;
    }
  }

  ngOnInit(): void {
    this.getAllActiveUsers();
    const modalElement = document.getElementById('addUserModal');
    if (modalElement) {
      // Khởi tạo 1 lần duy nhất
      this.modalInstance = new (window as any).bootstrap.Modal(modalElement);
      modalElement.addEventListener('hidden.bs.modal', () => {
        this.resetForm();
      });
    }
  }

  getAllActiveUsers() {
    this.http.get<any[]>(`${this.apiUrl}/active`).subscribe({
      next: (data) => {
        this.allActiveUsers = data;
        this.nguoiDungList = data;
      },
      error: (err) => {
        console.error('Lỗi khi tải danh sách người dùng active:', err);
      }
    });
  }

  searchUsers() {
    const trimmedKeyword = this.keyword.trim().toLowerCase();

    if (!trimmedKeyword) {
      // Nếu bỏ trống, trả về toàn bộ user active
      this.nguoiDungList = this.allActiveUsers;
      this.searched = false;
      return;
    }

    this.nguoiDungList = this.allActiveUsers.filter(user => {
      return (user.name && user.name.toLowerCase().includes(trimmedKeyword))
        || (user.email && user.email.toLowerCase().includes(trimmedKeyword))
        || (user.soDienThoai && user.soDienThoai.includes(trimmedKeyword)) //vì sdt là số nên k cần phải dùng toLowercase để chuyển đổi iểu chữ
        || (user.id && user.id.includes(trimmedKeyword));
    });

    this.searched = true;
  }

  canDelete(user: any): boolean { //ktra ko phải id 1000001 thì trả về true có thể xóa
    return user.id !== this.userId;
  }

  deleteUser(id: string): Promise<void> {
    return new Promise((resolve, reject) => {
      if (confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
        this.http.delete(`${this.apiUrl}/${id}`).subscribe({
          next: () => {
            this.getAllActiveUsers();
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
    this.modalInstance?.show();
  }

  onSubmit() {
    const userToSend = {
      ...this.formUser,
      active: this.convertGenderToBoolean(this.formUser.active),
      gioiTinh: this.convertGenderToBoolean(this.formUser.gioiTinh)
    };

    if (this.selectedUser) {
      this.http.put(`${this.apiUrl}/${this.selectedUser.id}`, userToSend).subscribe({
        next: () => {
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
        next: () => {
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

  private convertGenderToBoolean(value: any): boolean {
    if (typeof value === 'boolean') return value;
    if (typeof value === 'string') {
      return value.toLowerCase() === 'nam' || value === 'true';
    }
    return false;
  }

  resetForm() {
    this.formUser = {};
    this.selectedUser = null;
  }

  closeModal() {
    this.modalInstance?.hide();
  }

  openAddUserModal() {
    this.resetForm();
    this.modalInstance?.show();
  }

}
