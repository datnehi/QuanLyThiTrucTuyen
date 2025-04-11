import { Component } from '@angular/core';
import { Course } from '../../models/course';
import { Exam } from '../../models/exam';
import { ActivatedRoute, Router } from '@angular/router';
import { ExamService } from '../../services/exam.service';
import { CourseService } from '../../services/course.service';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../services/account.service';
import { ResultService } from '../../services/result.service';
import { Result } from '../../models/result';
import { StudentExamResultComponent } from "../student-exam-result/student-exam-result.component";

@Component({
  selector: 'app-student-exam-detail',
  imports: [
    CommonModule,
    StudentExamResultComponent
],
  templateUrl: './student-exam-detail.component.html',
  styleUrl: './student-exam-detail.component.css'
})
export class StudentExamDetailComponent {
  user: any;
  monHoc: Course | null = null;
  maKiThi: string = '';
  exam: Exam | null = null;
  result: Result | null = null;

  constructor(private courseService: CourseService, private examService: ExamService,
    private route: ActivatedRoute, private accountService: AccountService,
    private resultService: ResultService, private router: Router) { }

  ngOnInit() {
    this.maKiThi = this.route.snapshot.paramMap.get('maKiThi') || '';
    this.examService.getExambyMa(this.maKiThi).subscribe((data) => {
      this.exam = data;
      if (this.exam && this.exam.maMonHoc) {
        this.courseService.getCoursebyId(this.exam.maMonHoc).subscribe((data) => {
          this.monHoc = data;
        });
      }
    });
    this.accountService.userInfo$.subscribe(user => {
      this.user = user;
    });
    this.resultService.getResultByMaKiThiAndId(this.maKiThi, this.user.id).subscribe((data) => {
      this.result = data;
    })
  }

  getExamStatus(exam: Exam | null): string {
    if (!exam) return 'Chưa xác định'; // Xử lý trường hợp `exam` bị null

    const now = new Date();
    const start = new Date(exam.thoiGianBatDau);
    const end = new Date(exam.thoiGianKetThuc);

    if (now < start) {
      return 'Chưa đến thời gian làm bài';
    } else if (now >= start && now <= end) {
      if (this.result) {
        if (this.result.diem != null) {
          return 'Bài thi đã hoàn thành';
        }
        else {
          return 'Tiếp tục làm bài thi';
        }
      }
      else {
        return 'Bắt đầu thi';
      }
    } else {
      return 'Đã quá thời gian làm bài';
    }
  }

  startExam() {
    this.router.navigate(['/student-exams/start', this.maKiThi]);
  }
}
