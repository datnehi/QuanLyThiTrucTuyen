import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NotificationsService } from '../../services/notifications.service';

import { FormsModule } from '@angular/forms';
import { Notification } from '../../models/notification';

@Component({
  selector: 'app-notifications',
  imports: [
    RouterModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent {
  notifications: Notification []=[];
  searchText: string = '';

  constructor(private notificationService: NotificationsService) {}

  ngOnInit(): void {
    this.notificationService.getNotification().subscribe((data) => {
      this.notifications = data;
    });
  }
  delete(maThongBao: string){
    this.notificationService.deleteNotification(maThongBao).subscribe(() => {
      this.ngOnInit();
    });
  }
}
