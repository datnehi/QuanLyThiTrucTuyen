import { Component } from '@angular/core';
import { ExamService } from '../../services/exam.service';
import { Exam } from '../../models/exam';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-student-exams',
  imports: [
    RouterModule,
    CommonModule,
    FormsModule,
    NgbModule,
  ],
  templateUrl: './student-exams.component.html',
  styleUrl: './student-exams.component.css'
})
export class StudentExamsComponent {
  exams: any[] = [];
  user: any;
  searchText: string = '';
  selectedStatus: string = 'Tất cả';

  constructor(private examService: ExamService, private accountService: AccountService) { }

  ngOnInit() {
    this.accountService.userInfo$.subscribe(user => {
      this.user = user;
      this.examService.getExamsWithCoursesById(user.id).subscribe((data) => {
        this.exams = data;
      });
    })
  }

  get filteredExams() {
    return this.exams.filter(exam => {
      const status = this.getExamStatus(exam);
      const matchesStatus = this.selectedStatus === 'Tất cả' || status === this.selectedStatus;
      const matchesSearch = exam.tenKiThi.toLowerCase().includes(this.searchText.toLowerCase());
      return matchesStatus && matchesSearch;
    });
  }

  selectStatus(status: string) {
    this.selectedStatus = status;
  }

  getExamStatus(exam: Exam) {
    const now = new Date();
    const start = new Date(exam.thoiGianBatDau);
    const end = new Date(exam.thoiGianKetThuc);

    if (now < start) {
      return 'Chưa mở';
    } else if (now >= start && now <= end) {
      return 'Đang mở';
    } else {
      return 'Đã đóng';
    }
  }
}
