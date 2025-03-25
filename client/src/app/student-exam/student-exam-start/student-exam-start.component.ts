import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AccountService } from '../../services/account.service';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';
import { ExamService } from '../../services/exam.service';
import { ResultService } from '../../services/result.service';
import { ChitietbaithiService } from '../../services/chitietbaithi.service';
import { Exam } from '../../models/exam';
import { Result } from '../../models/result';

@Component({
  selector: 'app-student-exam-start',
  imports: [
    CommonModule
  ],
  templateUrl: './student-exam-start.component.html',
  styleUrl: './student-exam-start.component.css'
})
export class StudentExamStartComponent {
  user: any;
  questions: any[] = []; // Danh sách câu hỏi
  selectedAnswers: { [key: string]: string } = {}; // Lưu đáp án đã chọn
  maKiThi: string = '';
  exam!: Exam;
  time!: number;
  result!: Result;

  constructor(private resultService: ResultService, private accountService: AccountService, private route: ActivatedRoute,
    private router: Router, private examService: ExamService) { }

  ngOnInit() {
    this.maKiThi = this.route.snapshot.paramMap.get('maKiThi') || '';
    this.accountService.userInfo$.subscribe(user => {
      this.user = user;
    });

    this.examService.getExambyMa(this.maKiThi).subscribe((data) => {
      this.exam = data;
    });

    this.loadExamData();

    this.loadTime();
    // Khôi phục đáp án đã chọn từ sessionStorage
    const savedAnswers = sessionStorage.getItem(`selectedAnswers_${this.maKiThi}`);
    if (savedAnswers) {
      this.selectedAnswers = JSON.parse(savedAnswers);
    }
  }

  loadExamData() {
    this.resultService.createResult(this.maKiThi, this.user.id).subscribe(data => {
      this.questions = data;
    });
  }

  loadTime(){
    this.resultService.getResultByMaKiThiAndId(this.maKiThi, this.user.id).subscribe((data) => {
      this.result = data;
      // Lấy thời gian bắt đầu từ API
      const startTime = new Date(this.result?.thoiGianVaoThi).getTime() / 1000; // Chuyển sang giây
      const currentTime = Math.floor(Date.now() / 1000);
      const examDuration = this.exam.thoiGianThi * 60; // Tổng thời gian thi (phút → giây)

      // Tính thời gian còn lại
      this.time = Math.max(0, startTime + examDuration - currentTime);

      this.startCountdown();
    })
  }

  startCountdown() {
    const interval = setInterval(() => {
      if (this.time > 0) {
        this.time--;
      } else {
        clearInterval(interval);
        this.submitExam();
      }
    }, 1000); // Giảm mỗi giây
  }


  formatTime(seconds: number): string {
    const hours = Math.floor(seconds / 3600); // Tính giờ
    const minutes = Math.floor((seconds % 3600) / 60); // Tính phút (sau khi trừ giờ)
    const sec = seconds % 60; // Số giây còn lại

    if (hours > 0) {
      return `${hours}:${minutes < 10 ? '0' : ''}${minutes}:${sec < 10 ? '0' : ''}${sec}`;
    } else {
      return `${minutes}:${sec < 10 ? '0' : ''}${sec}`;
    }
  }


  selectAnswer(questionId: string, answerId: string) {
    this.selectedAnswers[questionId] = answerId;
    sessionStorage.setItem(`selectedAnswers_${this.maKiThi}`, JSON.stringify(this.selectedAnswers));
  }

  submitExam() {
    if (this.time > 0) {
      Swal.fire({
        icon: 'info',
        title: 'Bạn có chắc chắn muốn nộp bài?',
        text: 'Khi xác nhận nộp bài, bạn sẽ không thể sửa lại bài thi.',
        showCancelButton: true,
        confirmButtonText: 'Vâng, chắc chắn!',
        cancelButtonText: 'Huỷ',
        reverseButtons: true
      }).then((result) => {
        if (result.isConfirmed) {
          this.finalSubmit();
        }
      });
    } else {
      this.finalSubmit();
    }
  }

  finalSubmit() {
    const resultData = {
      maKiThi: this.maKiThi,
      id: this.user.id,
      timeUsed: (this.exam.thoiGianThi * 60 - this.time) / 60, // Chuyển đổi về phút
      answers: this.selectedAnswers
    };

    this.resultService.submitExam(resultData).subscribe(
      (response) => {
        sessionStorage.removeItem(`selectedAnswers_${this.maKiThi}`);

        setTimeout(() => { // Đảm bảo xóa hoàn toàn trước khi điều hướng
          this.router.navigate(['/student-exams/result', this.maKiThi]);
        }, 100);
      },
      (error) => {
        Swal.fire('Lỗi!', 'Có lỗi khi nộp bài. Vui lòng thử lại.', 'error');
      }
    );
  }

}
