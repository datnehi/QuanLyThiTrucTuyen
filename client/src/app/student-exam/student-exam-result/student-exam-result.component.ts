import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ResultService } from '../../services/result.service';
import { AccountService } from '../../services/account.service';
import { Result } from '../../models/result';
import { ChitietbaithiService } from '../../services/chitietbaithi.service';
import { ChiTietBaiThi } from '../../models/chitietbaithi';
import { ExamService } from '../../services/exam.service';
import { CourseService } from '../../services/course.service';
import { Exam } from '../../models/exam';
import { Course } from '../../models/course';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-student-exam-result',
  imports: [
    CommonModule
  ],
  templateUrl: './student-exam-result.component.html',
  styleUrl: './student-exam-result.component.css'
})
export class StudentExamResultComponent {
  result: Result | null = null;
  maKiThi: string = "";
  user: any;
  examDetail: ChiTietBaiThi[] = [];
  exam: Exam | null = null;
  course: Course | null = null;

  constructor(private accountService: AccountService, private route: ActivatedRoute,
    private resultService: ResultService, private chiTietBaiThi: ChitietbaithiService,
    private examService: ExamService, private courseService: CourseService) { }

  ngOnInit() {
    this.maKiThi = this.route.snapshot.paramMap.get('maKiThi')!;
    this.accountService.userInfo$.subscribe(user => {
      if (user) {
        this.user = user;
        this.resultService.getResultByMaKiThiAndId(this.maKiThi, this.user.id).subscribe((data) => {
          this.result = data;
          if (this.result?.maKetQua) {
            this.chiTietBaiThi.getChiTietBaiLam(this.result.maKetQua).subscribe(data => {
              this.examDetail = data;
            });
          }
        });
      }
    });
    this.examService.getExambyMa(this.maKiThi).subscribe((data) => {
      this.exam = data;
      if (this.exam?.maMonHoc) {
        this.courseService.getCoursebyId(this.exam.maMonHoc).subscribe((data) => {
          this.course = data;
        });
      }
    });
  }
}
