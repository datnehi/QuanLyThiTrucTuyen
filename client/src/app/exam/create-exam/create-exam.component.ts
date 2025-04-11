import { Component, OnInit } from '@angular/core';
import flatpickr from 'flatpickr';
import { CourseService } from '../../services/course.service';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Course } from '../../models/course';
import { ExamService } from '../../services/exam.service';
import { Exam } from '../../models/exam';
import { format } from 'date-fns';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-exam',
  imports: [
    FormsModule,
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './create-exam.component.html',
  styleUrl: './create-exam.component.css'
})
export class CreateExamComponent implements OnInit {
  monHocList: Course[] = [];

  constructor(private courseService: CourseService, private examService: ExamService, private router: Router) { }

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
        this.examForm.get('thoiGianBatDau')?.setValue(dateStr);
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
        this.examForm.get('thoiGianKetThuc')?.setValue(dateStr);
      }
    });
  }

  validateTimeRange: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const group = control as FormGroup;
    const start = group.get('thoiGianBatDau')?.value;
    const end = group.get('thoiGianKetThuc')?.value;

    if (start && end && new Date(start) >= new Date(end)) {
      return { invalidTimeRange: true };
    }

    return null;
  }

  examForm = new FormGroup({
    tenKiThi: new FormControl('', Validators.required),
    thoiGianBatDau: new FormControl('', Validators.required),
    thoiGianKetThuc: new FormControl('', Validators.required),
    thoiGianThi: new FormControl('', [Validators.required, Validators.min(1)]),
    soCau: new FormControl('', [Validators.required, Validators.min(1)]),
    maMonHoc: new FormControl('', Validators.required),
    hienThiBaiLam: new FormControl(false),
    xemDiem: new FormControl(false),
    xemDapAn: new FormControl(false),
  }, { validators: this.validateTimeRange });

  create() {
    if (this.examForm.invalid) {
      this.examForm.markAllAsTouched();
      return;
    }

    const formData = this.examForm.value;

    const newExam: Exam = {
      maMonHoc: formData.maMonHoc!,
      tenKiThi: formData.tenKiThi!,
      thoiGianTao: format(new Date(), 'yyyy-MM-dd HH:mm:ss'),
      thoiGianBatDau: formData.thoiGianBatDau!,
      thoiGianKetThuc: formData.thoiGianKetThuc!,
      thoiGianThi: Number(formData.thoiGianThi),
      xemDiem: this.examForm.get('xemDiem')?.value || false,
      xemDapAn: this.examForm.get('xemDapAn')?.value || false,
      hienThiBaiLam: this.examForm.get('hienThiBaiLam')?.value || false,
      soCau: Number(formData.soCau),
      trangThai: false,
    };

    this.examService.createExam(newExam).subscribe(() => {
      this.router.navigate(['/exams']);
    });
  }

  clickXemDA() {
    const hienThiControl = this.examForm.get('hienThiBaiLam');
    if (hienThiControl && !hienThiControl.value) {
      hienThiControl.setValue(true);
    }
  }
}
