import { Component } from '@angular/core';
import { Course } from '../../models/course';
import { CourseService } from '../../services/course.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationsService } from '../../services/notifications.service';
import { Notification } from '../../models/notification';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-update-notification',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './update-notification.component.html',
  styleUrl: './update-notification.component.css'
})
export class UpdateNotificationComponent {
  selectedMonHoc: string = '';
  course: Course | null = null;
  maThongBao: string = '';
  notification: Notification | null = null;

  constructor(private notificationsService: NotificationsService, private courseService: CourseService,
    private route: ActivatedRoute, private router: Router) { }

  ngOnInit(){
    this.maThongBao = this.route.snapshot.paramMap.get('maThongBao') || '';
    this.notificationsService.getNotificationbyMaThongBao(this.maThongBao).subscribe((data) => {
      this.notification = data;
      if(this.notification && this.notification.maThongBao){
        this.courseService.getCoursebyId(this.notification.maMonHoc).subscribe((data) => {
          this.course = data;
        });
      }
    })
  }

  update(form: NgForm) {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }
    if (this.notification) {
      this.notificationsService.updateNotification(this.maThongBao, this.notification).subscribe((res) => {
        this.router.navigate(['/notifications']);
      });
    }
  }
}
