import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HasRoleDirective } from '../directives/has-role.directive';
import { NotificationsService } from '../services/notifications.service';
import { Notification } from '../models/notification';

@Component({
  selector: 'app-header',
  imports: [
    CommonModule,
    NgbModule,
    HasRoleDirective,
    RouterModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  user: any = null;
  notifications: Notification[] = [];


  constructor(public accountService: AccountService, private notificationsService: NotificationsService, private router: Router) { }

  ngOnInit() {
    this.accountService.userInfo$.subscribe(user => {
      this.user = user;
    });
    this.notificationsService.getNotificationbyId(this.user.id).subscribe((data) => {
      this.notifications = data;
    });
  }

  logout() {
    this.accountService.logout().subscribe({
      next: () => {
        this.router.navigate(['']);
      }
    });
  }
}
