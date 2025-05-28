import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import * as bootstrap from 'bootstrap';
import { CourseService } from '../services/course.service';
import { Course } from '../models/course';

@Component({
  selector: 'app-subjects',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.css']
})
export class SubjectsComponent implements OnInit {
  subjects: any[] = [];
  filteredSubjects: any[] = [];
  currentPage = 1;
  itemsPerPage = 10;
  totalItems = 0;
  isLoading = false;
  searchKeyword = '';
  editingSubjectId: string | null = null;

  // Form thêm môn học
  newSubject = {
    maMonHoc: '',
    tenMonHoc: '',
    giangVien: '',
    soTinChi: 0
  };

  constructor(private courseService: CourseService, private router: Router) { }

  ngOnInit(): void {
    this.loadSubjects();
    // Thêm sự kiện khi modal bị đóng
    const modal = document.getElementById('addSubjectModal');
    if (modal) {
      modal.addEventListener('hidden.bs.modal', () => {
        this.resetForm();
      });
    }
  }

  loadSubjects(): void {
    this.isLoading = true;
    this.courseService.getSubjects().subscribe({
      next: (data) => {
        this.subjects = data;
        this.filteredSubjects = [...data];
        this.totalItems = data.length;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading subjects:', error);
        this.isLoading = false;
      }
    });
  }

  searchSubjects(): void {
    if (this.searchKeyword.trim()) {
      this.courseService.searchSubjects(this.searchKeyword).subscribe({
        next: (data) => {
          this.filteredSubjects = data;
          this.totalItems = data.length;
          this.currentPage = 1;
        },
        error: (error) => {
          console.error('Error searching subjects:', error);
        }
      });
    } else {
      this.filteredSubjects = [...this.subjects];
      this.totalItems = this.subjects.length;
      this.currentPage = 1;
    }
  }

  editSubject(maMonHoc: string): void {
    this.editingSubjectId = maMonHoc;
    const subject = this.subjects.find(s => s.maMonHoc === maMonHoc);
    if (!subject) return;

    this.newSubject = {
      maMonHoc: subject.maMonHoc,
      tenMonHoc: subject.tenMonHoc,
      giangVien: subject.giangVien,
      soTinChi: subject.soTinChi
    };

    const modal = new bootstrap.Modal(document.getElementById('addSubjectModal')!);
    modal.show();
  }

  updateSubject(): void {
    if (!this.editingSubjectId) return;

    if (!this.newSubject.tenMonHoc || this.newSubject.soTinChi <= 0) {
      alert('Vui lòng điền đầy đủ thông tin và số tín chỉ phải lớn hơn 0!');
      return;
    }

    this.courseService.updateSubject(this.editingSubjectId, this.newSubject).subscribe({
      next: () => {
        alert('Môn học đã được cập nhật thành công!');
        this.resetForm();
        this.closeModal();
        this.loadSubjects();
      },
      error: (error) => {
        console.error('Error updating subject:', error);
        alert('Có lỗi xảy ra khi cập nhật môn học!');
      }
    });
  }

  // Cập nhật điều kiện validate trong saveSubject()
  saveSubject(): void {
    if (this.editingSubjectId) {
      this.updateSubject();
    } else {
      if (!this.newSubject.tenMonHoc || this.newSubject.soTinChi <= 0) {
        alert('Vui lòng điền đầy đủ thông tin bắt buộc (Tên môn học, Số tín chỉ phải lớn hơn 0)!');
        return;
      }

      this.courseService.createSubject(this.newSubject).subscribe({
        next: () => {
          alert('Môn học đã được thêm thành công!');
          this.resetForm();
          this.closeModal();
          this.loadSubjects();
        },
        error: (error) => {
          console.error('Error creating subject:', error);
          alert('Có lỗi xảy ra khi thêm môn học!');
        }
      });
    }
  }

  deleteSubject(mamonhoc: string): void {
    if (confirm('Bạn có chắc chắn muốn xóa môn học này?')) {
      this.courseService.deleteSubject(mamonhoc).subscribe({
        next: () => {
          this.loadSubjects();
        },
        error: (error) => {
          console.error('Error deleting subject:', error);
        }
      });
    }
  }

  // Cập nhật resetForm()
  resetForm(): void {
    this.newSubject = {
      maMonHoc: '',
      tenMonHoc: '',
      giangVien: '',
      soTinChi: 0
    };
    this.editingSubjectId = null;
  }

  closeModal(): void {
    const modal = document.getElementById('addSubjectModal');
    if (modal) {
      const modalInstance = bootstrap.Modal.getInstance(modal);
      if (modalInstance) {
        modalInstance.hide();
      } else {
        new bootstrap.Modal(modal).hide();
      }
      this.resetForm();

      const backdrop = document.querySelector('.modal-backdrop');
      if (backdrop) {
        backdrop.remove();
      }

      document.body.classList.remove('modal-open');
      document.body.style.paddingRight = '';
      document.body.style.overflow = '';
    }
  }

  get paginatedSubjects(): any[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredSubjects.slice(startIndex, startIndex + this.itemsPerPage);
  }

  changePage(page: number): void {
    this.currentPage = page;
  }

  getTotalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  getPageNumbers(): number[] {
    const totalPages = this.getTotalPages();
    const pagesToShow = 5;
    let startPage = Math.max(1, this.currentPage - Math.floor(pagesToShow / 2));
    let endPage = Math.min(totalPages, startPage + pagesToShow - 1);

    if (endPage - startPage + 1 < pagesToShow) {
      startPage = Math.max(1, endPage - pagesToShow + 1);
    }

    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  }
}
