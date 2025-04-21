import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NotificationsService } from '../../services/notifications.service';
import { FormsModule } from '@angular/forms';
import { Notification } from '../../models/notification';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [
    RouterModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  allNotifications: Notification[] = []; // Lưu tất cả thông báo
  filteredNotifications: Notification[] = []; // Chỉ chứa thông báo có trangThai = 0
  searchText: string = '';

  constructor(private notificationService: NotificationsService) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.notificationService.getNotification().subscribe({
      next: (data) => {
        this.allNotifications = data;
        this.filterActiveNotifications(); // Lọc ngay khi nhận dữ liệu
      },
      error: (err) => console.error('Lỗi khi tải thông báo:', err)
    });
  }

  // Lọc chỉ lấy thông báo có trangThai = 0
  filterActiveNotifications(): void {
    this.filteredNotifications = this.allNotifications.filter(
      noti => Number(noti.trangThai) === 0
    );
  }

  delete(maThongBao: string): void {
    if (confirm('Bạn có chắc muốn xoá thông báo này không?')) {
      this.notificationService.deleteNotification(maThongBao).subscribe({
        next: () => this.loadNotifications(),
        error: (err) => console.error('Lỗi khi xóa thông báo:', err)
      });
    }
  }
}