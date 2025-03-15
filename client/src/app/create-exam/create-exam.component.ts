import { Component, OnInit } from '@angular/core';
import flatpickr from 'flatpickr';
import { CourseService } from '../services/course.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Course } from '../models/course';
import { ExamService } from '../services/exam.service';
import { Exam } from '../models/exam';
import { format } from 'date-fns';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-exam',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './create-exam.component.html',
  styleUrl: './create-exam.component.css'
})
export class CreateExamComponent implements OnInit{
  thoiGianBatDau: string = '';
  thoiGianKetThuc: string = '';
  monHocList: Course[] = [];
  selectedMonHoc: string = '';

  constructor(private courseService: CourseService, private examService: ExamService, private router: Router) {}

  ngOnInit() {
    this.courseService.getAll().subscribe((data) => {
      this.monHocList = data;
    });
    flatpickr('#time-start', {
      enableTime: true,
      time_24hr: true,
      dateFormat: 'Y-m-d H:i:ss',
      minDate: 'today',
      onReady: (selectedDates, dateStr, instance) => {
        const now = new Date();
        const hours = now.getHours();
        const minutes = now.getMinutes();
        instance.set('minTime', `${hours}:${minutes}`);
      },
      onChange: (selectedDates, dateStr) => {
        this.thoiGianBatDau = dateStr;
      }
    });
    flatpickr('#time-end', {
      enableTime: true,
      time_24hr: true,
      dateFormat: 'Y-m-d H:i:ss',
      minDate: 'today',
      onReady: (selectedDates, dateStr, instance) => {
        const now = new Date();
        const hours = now.getHours();
        const minutes = now.getMinutes();
        instance.set('minTime', `${hours}:${minutes}`);
      },
      onChange: (selectedDates, dateStr) => {
        this.thoiGianKetThuc = dateStr;
      }
    });
  }

  create() {
    const newExam: Exam = {
      maMonHoc: this.selectedMonHoc,
      tenKiThi: (document.getElementById('name-exam') as HTMLInputElement).value,
      thoiGianTao: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
      thoiGianBatDau: (document.getElementById('time-start') as HTMLInputElement).value,
      thoiGianKetThuc: (document.getElementById('time-end') as HTMLInputElement).value,
      thoiGianThi: parseInt((document.getElementById('exam-time') as HTMLInputElement).value),
      xemDiem: (document.getElementById('xemdiem') as HTMLInputElement).checked,
      xemDapAn: (document.getElementById('xemda') as HTMLInputElement).checked,
      hienThiBaiLam: (document.getElementById('xembailam') as HTMLInputElement).checked,
      soCau: parseInt((document.getElementById('socau') as HTMLInputElement).value),
      trangThai: false,
    };
    this.examService.createExam(newExam).subscribe((res) => {
      this.router.navigate(['/exams']);
    }, (error) => {
      alert('Tạo kỳ thi thất bại! Vui lòng thử lại.');
    });
  }

}
