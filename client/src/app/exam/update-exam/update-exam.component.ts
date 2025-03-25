import { Component } from '@angular/core';
import flatpickr from 'flatpickr';
import { CourseService } from '../../services/course.service';
import { ExamService } from '../../services/exam.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Exam } from '../../models/exam';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Course } from '../../models/course';
import { format } from 'date-fns';

@Component({
  selector: 'app-update-exam',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './update-exam.component.html',
  styleUrl: './update-exam.component.css'
})
export class UpdateExamComponent {
  thoiGianBatDau: string = '';
  thoiGianKetThuc: string = '';
  monHoc!: Course;
  selectedMonHoc: string = '';
  maKiThi: string = '';
  exam!: Exam;

  constructor(private courseService: CourseService, private examService: ExamService,
    private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.maKiThi = this.route.snapshot.paramMap.get('maKiThi') || '';
    this.examService.getExambyMa(this.maKiThi).subscribe((data) => {
      this.exam = data;
      if (this.exam && this.exam.maMonHoc) {
        this.courseService.getCoursebyId(this.exam.maMonHoc).subscribe((data) => {
          this.monHoc = data;
        });
      }
      setTimeout(() => { this.initFlatpickr(); }, 100);
    });
  }

  ngAfterViewInit() {
    setTimeout(() => { this.initFlatpickr(); }, 100);
  }

  initFlatpickr() {
    if (!document.querySelector('#time-start') || !document.querySelector('#time-end')) {
      console.warn("Không tìm thấy input thời gian.");
      return;
    }

    flatpickr('#time-start', {
      enableTime: true,
      time_24hr: true,
      dateFormat: 'Y-m-d H:i:ss',
      defaultDate: this.exam?.thoiGianBatDau || '',
      onChange: (selectedDates, dateStr) => {
        if (this.exam) {
          this.exam.thoiGianBatDau = dateStr;
        }
      }
    });

    flatpickr('#time-end', {
      enableTime: true,
      time_24hr: true,
      dateFormat: 'Y-m-d H:i:ss',
      defaultDate: this.exam?.thoiGianKetThuc || '',
      onChange: (selectedDates, dateStr) => {
        if (this.exam) {
          this.exam.thoiGianKetThuc = dateStr;
        }
      }
    });
  }

  update() {
    if (this.exam) {
      this.exam.tenKiThi = (document.getElementById('name-exam') as HTMLInputElement).value;
      this.exam.thoiGianKetThuc = (document.getElementById('time-end') as HTMLInputElement).value;
      this.examService.updateExam(this.maKiThi, this.exam).subscribe((res) => {
        this.router.navigate(['/exams']);
      }, (error) => {
        alert('Cập nhật kỳ thi thất bại! Vui lòng thử lại.');
      });
    }
  }

  clickXemDA(){
    if(this.exam && !this.exam.hienThiBaiLam){
      this.exam.hienThiBaiLam = true;
    }
  }
}
