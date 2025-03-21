import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Exam } from '../../models/exam';
import { ExamService } from '../../services/exam.service';
import { ResultService } from '../../services/result.service';
import { CourseService } from '../../services/course.service';
import { Course } from '../../models/course';
import { Chart, registerables } from 'chart.js';
import * as bootstrap from 'bootstrap';
import { ChiTietBaiThi } from '../../models/chitietbaithi';
import { ChitietbaithiService } from '../../services/chitietbaithi.service';

@Component({
  selector: 'app-detail-exam',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
  ],
  templateUrl: './detail-exam.component.html',
  styleUrl: './detail-exam.component.css'
})
export class DetailExamComponent{
  maKiThi: string = '';
  selectedTab: string = 'bang-diem';
  selectedStatus: string = 'Tất cả';
  results: any[] = [];
  searchText: string = '';
  showOffcanvas = false;
  exam: Exam | null = null;
  course: Course | null = null;
  daNop = 0;
  chuaNop = 0;
  khongThi = 0;
  diemTrungBinh = 0;
  diemCaoNhat = 0;
  chart!: Chart;
  @ViewChild('myChartCanvas') canvasRef!: ElementRef<HTMLCanvasElement>;
  selectedStudent: any;
  @ViewChild('examDetailModal') examDetailModal!: ElementRef;
  selectedExamDetail: ChiTietBaiThi[] = [];

  constructor(private route: ActivatedRoute, private resultService: ResultService,
    private examService: ExamService, private courseService: CourseService,
    private chiTietBaiThi: ChitietbaithiService) {
      Chart.register(...registerables);
     }

  ngOnInit() {
    this.maKiThi = this.route.snapshot.paramMap.get('maKiThi') || '';
    this.resultService.getResultsWithCourses(this.maKiThi).subscribe((data) => {
      this.results = data;
      this.tinhThongKe();
    });
    this.examService.getExambyId(this.maKiThi).subscribe((data) => {
      this.exam = data;
      if (this.exam && this.exam.maMonHoc) {
        this.courseService.getCoursebyId(this.exam.maMonHoc).subscribe((data) => {
          this.course = data;
        });
      }
    });
  }

  ngAfterViewChecked() {
    if (!this.chart && this.canvasRef) {
      this.tinhBieuDo();
    }
  }

  selectTab(tab: string) {
    this.selectedTab = tab;
    if (tab === 'thong-ke') {
      setTimeout(() => this.tinhBieuDo(), 100); // Chờ DOM render rồi vẽ lại biểu đồ
    }
  }

  selectStatus(status: string): void {
    this.selectedStatus = status;
  }

  get filteredResults() {
    return this.results.filter(result => {
      const status = this.getResultStatus(result);
      const matchesStatus = this.selectedStatus === 'Tất cả' || status === this.selectedStatus;
      const matchesSearch = result.hoten.toLowerCase().includes(this.searchText.toLowerCase());
      return matchesStatus && matchesSearch;
    });
  }

  getResultStatus(result: any): string {
    if (result.diem !== null && result.diem !== undefined) {
      return 'Đã nộp bài';
    } else if (result.thoiGianVaoThi) {
      return 'Chưa nộp bài';
    } else {
      return 'Vắng thithi';
    }
  }

  openOffcanvas() {
    this.showOffcanvas = true;
  }

  closeOffcanvas() {
    this.showOffcanvas = false;
  }

  tinhThongKe() {
    this.daNop = this.results.filter(k => k.diem !== null && k.diem !== undefined).length;
    this.chuaNop = this.results.filter(k => k.diem === null && k.thoigianvaothi !== null).length;
    this.khongThi = this.results.filter(k => k.thoigianvaothi === null).length;

    let diemList = this.results.filter(k => k.diem !== null).map(k => k.diem);
    this.diemTrungBinh = diemList.length > 0 ? diemList.reduce((sum, d) => sum + d, 0) / diemList.length : 0;
    this.diemCaoNhat = diemList.length > 0 ? Math.max(...diemList) : 0;
  }

  tinhBieuDo() {
    let diemCounts = {
      "<=1": 0, "<=2": 0, "<=3": 0, "<=4": 0, "<=5": 0,
      "<=6": 0, "<=7": 0, "<=8": 0, "<=9": 0, "<=10": 0
    };

    this.results.forEach(k => {
      let diem = k.diem;
      if (diem !== null) {
        if (diem <= 1) diemCounts["<=1"]++;
        else if (diem <= 2) diemCounts["<=2"]++;
        else if (diem <= 3) diemCounts["<=3"]++;
        else if (diem <= 4) diemCounts["<=4"]++;
        else if (diem <= 5) diemCounts["<=5"]++;
        else if (diem <= 6) diemCounts["<=6"]++;
        else if (diem <= 7) diemCounts["<=7"]++;
        else if (diem <= 8) diemCounts["<=8"]++;
        else if (diem <= 9) diemCounts["<=9"]++;
        else diemCounts["<=10"]++;
      }
    });

    this.veBieuDo(diemCounts);
  };

  veBieuDo(diemCounts: any) {
    const canvas = this.canvasRef.nativeElement;
    const ctx = canvas.getContext("2d");
    if (!ctx) {
      console.error("Không lấy được context từ canvas!");
      return;
    }

    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart(ctx, {
      type: "bar",
      data: {
        labels: Object.keys(diemCounts),
        datasets: [{
          label: "Số lượng sinh viên",
          data: Object.values(diemCounts),
          backgroundColor: "blue",
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: { beginAtZero: true }
        },
        plugins: {
          legend: {
            position: "bottom"
          }
        }
      }
    });
  }

  openExamDetail(student: any) {
    this.selectedStudent = student;
    this.chiTietBaiThi.getChiTietBaiThi(this.selectedStudent.maKetQua).subscribe(data => {
      this.selectedExamDetail = data;
      const modal = new bootstrap.Modal(document.getElementById('examDetailModal')!);
      modal.show();
    });
  }

}
