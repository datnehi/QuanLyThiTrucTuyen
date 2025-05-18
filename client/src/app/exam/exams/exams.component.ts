import { Component } from '@angular/core';
import { ExamService } from '../../services/exam.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Exam } from '../../models/exam';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-exams',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    NgbModule
  ],
  templateUrl: './exams.component.html',
  styleUrl: './exams.component.css'
})
export class ExamsComponent {
  exams: any[] = [];
  searchText: string = '';
  selectedStatus: string = 'Tất cả';

  constructor(private examService: ExamService) {}

  ngOnInit(): void {
    this.examService.getExamsWithCourses().subscribe((data) => {
      this.exams = data;
    });
  }

  get filteredExams() {
    return this.exams.filter(exam => {
      const status = this.getExamStatus(exam);
      const matchesStatus = this.selectedStatus === 'Tất cả' || status === this.selectedStatus;
      const matchesSearch = exam.tenKiThi.toLowerCase().includes(this.searchText.toLowerCase());
      return matchesStatus && matchesSearch;
    });
  }

  selectStatus(status: string): void {
    this.selectedStatus = status;
  }

  getExamStatus(exam: Exam): string {
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

  delete(maKiThi: string){
    Swal.fire({
      icon: 'info',
      title: 'Bạn có chắc chắn muốn xóa kì thi này?',
      showCancelButton: true,
      confirmButtonText: 'Vâng, chắc chắn!',
      cancelButtonText: 'Huỷ',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
            this.examService.deleteExam(maKiThi).subscribe(() => {
              this.ngOnInit();
            });
          }
        });
  }
}
