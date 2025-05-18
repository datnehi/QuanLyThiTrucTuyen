import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Course } from '../models/course';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import * as bootstrap from 'bootstrap';
import { PhanmonService } from '../services/phanmon.service';
import { Router, RouterModule } from '@angular/router';
import { Exam } from '../models/exam';
import { ExamService } from '../services/exam.service';
import { NotificationsService } from '../services/notifications.service';
import { Notification } from '../models/notification';

@Component({
  selector: 'app-hocphan',
  imports: [
    CommonModule,
    FormsModule,
    NgbModule,
    RouterModule
  ],
  templateUrl: './hocphan.component.html',
  styleUrl: './hocphan.component.css'
})
export class HocphanComponent {
  user: any | null;
  danhSachMonHoc: Course[] = []
  searchText: string = '';
  mamoi: string = '';
  activeTab = 'so-settings';
  showOffcanvas: boolean = false;
  selectedMonHoc!: Course;
  exams: Exam[] = [];
  notifications: Notification[] = [];

  constructor(private accountService: AccountService, private notificationsService: NotificationsService,
    private phanMonService: PhanmonService, private examService: ExamService) { }

  ngOnInit() {
    this.accountService.userInfo$.subscribe(user => {
      this.user = user;
      this.phanMonService.getMonHocBySinhVien(user.id).subscribe((data) => {
        this.danhSachMonHoc = data;
      });
    });
  }

  get filteredMonHoc() {
    return this.danhSachMonHoc.filter(monhoc => {
      const matchesSearch = monhoc.tenMonHoc.toLowerCase().includes(this.searchText.toLowerCase());
      return matchesSearch;
    });
  }

  openAdd() {
    const modal = new bootstrap.Modal(document.getElementById('addModal')!);
    modal.show();
  }

  addHocPhan(id: string, maMonHoc: string) {
    this.phanMonService.addPhanMon(id, maMonHoc).subscribe({
      next: (res: any) => {
        if (res.success) {
          const modalElement = document.getElementById('addModal') as HTMLElement;
          const modal = bootstrap.Modal.getInstance(modalElement);
          if (modal) modal.hide();
          this.ngOnInit();
        } else {
          alert('Lỗi: ' + res.message);
        }
      },
      error: (err) => {
        alert('Lỗi hệ thống: ' + (err.error.message || 'Vui lòng thử lại!'));
      }
    });
  }
  setActiveTab(tabName: string) {
    this.activeTab = tabName;
  }

  openOffcanvas(monhoc: any) {
    this.selectedMonHoc = monhoc; // Gán dữ liệu học phần
    this.showOffcanvas = true; // Hiển thị offcanvas
    if (this.selectedMonHoc) {
      this.examService.getExamsByMaMonHoc(this.selectedMonHoc?.maMonHoc).subscribe((data) => {
        this.exams = data;
      });
      this.notificationsService.getNotificationbyMaMonHoc(this.selectedMonHoc?.maMonHoc).subscribe((data) => {
        this.notifications = data;
      });
    }
  }

  closeOffcanvas() {
    this.showOffcanvas = false; // Đóng offcanvas
  }
}
