import { Component } from '@angular/core';
import flatpickr from 'flatpickr';
import { CourseService } from '../../services/course.service';
import { ExamService } from '../../services/exam.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Exam } from '../../models/exam';
import { FormsModule, NgForm } from '@angular/forms';
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
  monHoc: Course | null = null;
  selectedMonHoc: string = '';
  maKiThi: string = '';
  exam: Exam | null = null;

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
      minDate: this.exam?.thoiGianBatDau || '',
      defaultDate: this.exam?.thoiGianKetThuc || '',
      onChange: (selectedDates, dateStr) => {
        if (this.exam) {
          this.exam.thoiGianKetThuc = dateStr;
        }
      }
    });
  }

  update(form: NgForm) {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }
    if (this.exam) {
      this.examService.updateExam(this.maKiThi, this.exam).subscribe((res) => {
        this.router.navigate(['/exams']);
      });
    }
  }

  clickXemDA(){
    if(this.exam && !this.exam.hienThiBaiLam){
      this.exam.hienThiBaiLam = true;
    }
  }
}
