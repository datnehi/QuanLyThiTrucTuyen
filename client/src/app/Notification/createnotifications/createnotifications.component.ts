import { NotificationsService } from './../../services/notifications.service';
import { Component } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { Course } from '../../models/course';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { format } from 'date-fns';
import { Router } from '@angular/router';
import { Notification } from '../../models/notification';

@Component({
  selector: 'app-createnotifications',
  imports: [
    FormsModule,
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './createnotifications.component.html',
  styleUrl: './createnotifications.component.css'
})
export class CreatenotificationsComponent {
  monHocList: Course[] = [];

  constructor(private courseService: CourseService, private notificationsService: NotificationsService, private router: Router){}

  ngOnInit(){
    this.courseService.getAll().subscribe((data) => {
      this.monHocList = data;
    });
  }

  notificationForm = new FormGroup({
    noiDung: new FormControl('', Validators.required),
    maMonHoc: new FormControl('', Validators.required),
  });

  create() {
      if (this.notificationForm.invalid) {
        this.notificationForm.markAllAsTouched();
        return;
      }

      const formData = this.notificationForm.value;

      const newNotification: Notification = {
        maMonHoc: formData.maMonHoc!,
        noiDung: formData.noiDung!,
        thoiGianTao: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
        trangThai: false,
      };

      this.notificationsService.createNotification(newNotification).subscribe(() => {
        this.router.navigate(['/notifications']);
      });
    }
}
