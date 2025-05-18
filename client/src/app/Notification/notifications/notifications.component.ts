import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NotificationsService } from '../../services/notifications.service';
import { FormsModule } from '@angular/forms';
import { Notification } from '../../models/notification';
import Swal from 'sweetalert2';

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
  notifications: any[] = [];
  searchText: string = '';

  constructor(private notificationService: NotificationsService) { }

  ngOnInit(): void {
    this.notificationService.getNotificationWithCourses().subscribe((data) => {
      this.notifications = data;
    });
  }

  get filteredNotifications() {
    return this.notifications.filter(noti => {
      const matchesSearch = noti.noiDung.toLowerCase().includes(this.searchText.toLowerCase());
      return matchesSearch;
    });
  }

  delete(maThongBao: string) {
    Swal.fire({
      icon: 'info',
      title: 'Bạn có chắc chắn muốn xóa thông báo này?',
      showCancelButton: true,
      confirmButtonText: 'Vâng, chắc chắn!',
      cancelButtonText: 'Huỷ',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.notificationService.deleteNotification(maThongBao).subscribe(() => {
          this.ngOnInit();
        });
      }
    });
  }
}
