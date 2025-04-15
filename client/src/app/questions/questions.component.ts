import { Component, OnInit } from '@angular/core';
import { QuestionService } from '../services/question.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Question } from '../models/question'; // Điều chỉnh đường dẫn cho phù hợp
import * as bootstrap from 'bootstrap';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-questions',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './questions.component.html',
  styleUrls: ['./questions.component.css']
})
export class QuestionsComponent implements OnInit {
  questions: any[] = [];
  filteredQuestions: any[] = [];
  currentPage = 1;
  itemsPerPage = 10;
  totalItems = 0;
  isLoading = false;
  searchKeyword = '';
  selectedSubject = '';
  editingQuestionId: string | null = null;
  // Thêm thuộc tính này để quản lý subscriptions
  private subscriptions: Subscription[] = [];

  // Danh sách môn học
  subjects = [
    { code: '84104', name: 'Lập trình hướng đối tượng' },
    { code: '84105', name: 'Lập trình Java' },
    { code: '84106', name: 'Lập trình ứng dụng mạng' }
  ];

  // Form thêm câu hỏi
  newQuestion = {
    noidung: '',
    mamonhoc: '',
    dapan: [] as { noidung: string, ladapan: boolean }[],
    correctAnswerIndex: -1
  };

  constructor(
    private questionService: QuestionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadQuestions();
    this.initModalEvents();
  }

  ngOnDestroy(): void {
    // Hủy tất cả subscriptions khi component bị hủy
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  initModalEvents(): void {
    const modal = document.getElementById('addQuestionModal');
    if (modal) {
      modal.addEventListener('hidden.bs.modal', () => {
        this.resetForm();
        this.editingQuestionId = null; // Reset biến editing
      });
    }
  }

  loadQuestions(): void {
    this.isLoading = true;
    this.questionService.getQuestions().subscribe({
      next: (data) => {
        // Lọc chỉ hiển thị những câu hỏi có trangthai = false (0)
        this.questions = data.filter(q => q.trangthai === false);
        this.filteredQuestions = [...this.questions];
        this.totalItems = this.questions.length;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading questions:', error);
        this.isLoading = false;
      }
    });
  }

  filterBySubject(): void {
    if (!this.selectedSubject) {
      this.filteredQuestions = [...this.questions];
    } else {
      this.filteredQuestions = this.questions.filter(
        q => q.mamonhoc === this.selectedSubject
      );
    }
    this.totalItems = this.filteredQuestions.length;
    this.currentPage = 1;
  }
  //chỉnh sửa câu hỏi
  editQuestion(macauhoi: string): void {
    this.editingQuestionId = macauhoi;

    // Lấy thông tin câu hỏi từ danh sách
    const question = this.questions.find(q => q.macauhoi === macauhoi);
    if (!question) return;

    // Điền thông tin vào form
    this.newQuestion = {
      noidung: question.noidung,
      mamonhoc: question.mamonhoc,
      dapan: [],
      correctAnswerIndex: -1
    };

    // Lấy danh sách đáp án từ server
    this.questionService.getAnswersByQuestionId(macauhoi).subscribe({
      next: (answers) => {
        this.newQuestion.dapan = answers.map((a: any) => ({
          noidung: a.noidung,
          ladapan: a.ladapan
        }));

        // Tìm đáp án đúng
        this.newQuestion.correctAnswerIndex = answers.findIndex((a: any) => a.ladapan);

        // Mở modal
        const modal = new bootstrap.Modal(document.getElementById('addQuestionModal')!);
        modal.show();
      },
      error: (error) => {
        console.error('Error loading answers:', error);
      }
    });
  }

  // Thêm phương thức updateQuestion
  updateQuestion(): void {
    if (!this.editingQuestionId) return;

    // Kiểm tra dữ liệu hợp lệ
    if (!this.newQuestion.noidung || !this.newQuestion.mamonhoc ||
      this.newQuestion.dapan.length === 0 || this.newQuestion.correctAnswerIndex === -1) {
      alert('Vui lòng điền đầy đủ thông tin và ít nhất một đáp án đúng!');
      return;
    }

    // Chuẩn bị dữ liệu cập nhật
    const updatedQuestion: Question = {
      macauhoi: this.editingQuestionId,
      noidung: this.newQuestion.noidung,
      mamonhoc: this.newQuestion.mamonhoc,
      trangthai: false
    };

    // Gửi yêu cầu cập nhật
    this.questionService.updateQuestion(this.editingQuestionId, updatedQuestion).subscribe({
      next: () => {
        // Chuẩn bị dữ liệu câu trả lời
        const answers = this.newQuestion.dapan.map((answer, index) => ({
          macauhoi: this.editingQuestionId,
          noidung: answer.noidung,
          ladapan: index === this.newQuestion.correctAnswerIndex
        }));

        // Cập nhật các câu trả lời
        this.questionService.updateAnswers(this.editingQuestionId!, answers).subscribe({
          next: () => {
            alert('Câu hỏi và đáp án đã được cập nhật thành công!');
            this.resetForm();
            this.closeModal();
            this.loadQuestions();
          },
          error: (error) => {
            console.error('Lỗi khi cập nhật đáp án:', error);
            alert('Câu hỏi đã được cập nhật nhưng có lỗi khi cập nhật đáp án!');
          }
        });
      },
      error: (error) => {
        console.error('Lỗi khi cập nhật câu hỏi:', error);
        alert('Có lỗi xảy ra khi cập nhật câu hỏi!');
      }
    });
  }

  searchQuestions(): void {
    if (this.searchKeyword.trim()) {
      this.filteredQuestions = this.questions.filter(q =>
        q.noidung.toLowerCase().includes(this.searchKeyword.toLowerCase())
      );
    } else {
      this.filteredQuestions = [...this.questions];
    }
    this.totalItems = this.filteredQuestions.length;
    this.currentPage = 1;
  }

  addAnswer(): void {
    this.newQuestion.dapan.push({ noidung: '', ladapan: false });
  }

  removeAnswer(index: number): void {
    this.newQuestion.dapan.splice(index, 1);
    if (this.newQuestion.correctAnswerIndex === index) {
      this.newQuestion.correctAnswerIndex = -1;
    } else if (this.newQuestion.correctAnswerIndex > index) {
      this.newQuestion.correctAnswerIndex--;
    }
  }

  setCorrectAnswer(index: number): void {
    this.newQuestion.correctAnswerIndex = index;
    this.newQuestion.dapan.forEach((d, i) => {
      d.ladapan = i === index;
    });
  }

  saveQuestion(): void {
    if (this.editingQuestionId) {
      this.updateQuestion();
    } else {
      // Kiểm tra dữ liệu hợp lệ
      if (!this.newQuestion.noidung || !this.newQuestion.mamonhoc ||
        this.newQuestion.dapan.length === 0 || this.newQuestion.correctAnswerIndex === -1) {
        alert('Vui lòng điền đầy đủ thông tin và ít nhất một đáp án đúng!');
        return;
      }
      this.isLoading = true; // Bật trạng thái loading

      // Chuẩn bị dữ liệu gửi lên server
      const questionData: Question = {
        macauhoi: '', // Server sẽ tự tạo hoặc để rỗng nếu server tự sinh
        noidung: this.newQuestion.noidung,
        mamonhoc: this.newQuestion.mamonhoc,
        trangthai: false // Mặc định là false
      };

      // Sử dụng subscription và lưu vào mảng quản lý
      const createSub = this.questionService.createQuestion(questionData).subscribe({
        next: (createdQuestion) => {
          // Chuẩn bị dữ liệu câu trả lời
          const answers = this.newQuestion.dapan.map((answer, index) => ({
            macauhoi: createdQuestion.macauhoi,
            noidung: answer.noidung,
            ladapan: index === this.newQuestion.correctAnswerIndex
          }));

          // Lưu các câu trả lời
          const saveAnswersSub = this.questionService.saveAnswers(answers).subscribe({
            next: () => {
              alert('Câu hỏi và đáp án đã được lưu thành công!');
              this.closeModal();
              this.loadQuestions(); // Load lại danh sách
            },
            error: (error) => {
              this.isLoading = false;
              console.error('Lỗi khi lưu đáp án:', error);
              alert('Câu hỏi đã được lưu nhưng có lỗi khi lưu đáp án!');
            },
            complete: () => {
              this.isLoading = false;
            }
          });
          this.subscriptions.push(saveAnswersSub);
        },
        error: (error) => {
          this.isLoading = false;
          console.error('Lỗi khi lưu câu hỏi:', error);
          alert('Có lỗi xảy ra khi lưu câu hỏi!');
        }
      });
      this.subscriptions.push(createSub);
    }
  }

  resetForm(): void {
    this.newQuestion = {
      noidung: '',
      mamonhoc: '',
      dapan: [],
      correctAnswerIndex: -1
    };
  }

  closeModal(): void {
    const modal = document.getElementById('addQuestionModal');
    if (modal) {
      // Sử dụng Bootstrap để đóng modal đúng cách
      const modalInstance = bootstrap.Modal.getInstance(modal);
      if (modalInstance) {
        modalInstance.hide();
      } else {
        new bootstrap.Modal(modal).hide();
      }

      // Reset form khi đóng modal
      this.resetForm();
      this.editingQuestionId = null;

      // Xóa backdrop nếu có
      const backdrop = document.querySelector('.modal-backdrop');
      if (backdrop) {
        backdrop.remove();
      }

      // Xóa lớp modal-open từ body
      document.body.classList.remove('modal-open');
      document.body.style.paddingRight = '';
      document.body.style.overflow = '';
    }
  }

  deleteQuestion(macauhoi: string): void {
    if (confirm('Bạn có chắc chắn muốn xóa câu hỏi này?')) {
      this.questionService.deleteQuestion(macauhoi).subscribe({
        next: () => {
          this.loadQuestions();
        },
        error: (error) => {
          console.error('Error deleting question:', error);
        }
      });
    }
  }

  get paginatedQuestions(): any[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredQuestions.slice(startIndex, startIndex + this.itemsPerPage);
  }

  changePage(page: number): void {
    this.currentPage = page;
  }

  getSubjectName(mamonhoc: string): string {
    const subject = this.subjects.find(s => s.code === mamonhoc);
    return subject ? subject.name : 'Không xác định';
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