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
  notifications: Notification[] = [];
  searchText: string = '';

  constructor(private notificationService: NotificationsService) {}

  ngOnInit(): void {
    this.notificationService.getNotification().subscribe((data) => {
      this.notifications = data;
    });
  }

  delete(maThongBao: string): void {
    if (confirm('Bạn có chắc muốn xoá thông báo này không?')) {
      this.notificationService.deleteNotification(maThongBao).subscribe(() => {
        this.ngOnInit();
      });
    }
  }
}
