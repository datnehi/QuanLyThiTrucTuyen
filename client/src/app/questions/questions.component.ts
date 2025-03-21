import { Component, Renderer2, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-questions',
  templateUrl: './questions.component.html',
  styleUrls: ['./questions.component.css']
})
export class QuestionsComponent {
  @ViewChild('addQuestionModal') addQuestionModal: any;

  constructor(private renderer: Renderer2, private el: ElementRef) {}

  openModal() {
    this.addQuestionModal.nativeElement.classList.add('show');
    this.addQuestionModal.nativeElement.setAttribute('aria-hidden', 'false');
    this.addQuestionModal.nativeElement.style.display = 'block';
  }

  closeModal() {
    this.addQuestionModal.nativeElement.classList.remove('show');
    this.addQuestionModal.nativeElement.setAttribute('aria-hidden', 'true');
    this.addQuestionModal.nativeElement.style.display = 'none';
  }

  addAnswer() {
    const answersContainer = document.getElementById('questionAnswers');
    const answerDiv = document.createElement('div');
    answerDiv.className = 'input-group mb-2';
    const answerId = `answer-${Date.now()}`;
    answerDiv.id = answerId;

    const input = this.renderer.createElement('input');
    this.renderer.setAttribute(input, 'type', 'text');
    this.renderer.setAttribute(input, 'class', 'form-control');
    this.renderer.setAttribute(input, 'placeholder', 'Đáp án');
    this.renderer.setAttribute(input, 'required', 'true');

    const radioDiv = this.renderer.createElement('div');
    this.renderer.setAttribute(radioDiv, 'class', 'input-group-text');

    const radio = this.renderer.createElement('input');
    this.renderer.setAttribute(radio, 'type', 'radio');
    this.renderer.setAttribute(radio, 'class', 'form-check-input');
    this.renderer.setAttribute(radio, 'name', 'correctAnswer');

    this.renderer.appendChild(radioDiv, radio);

    const deleteButton = this.renderer.createElement('button');
    this.renderer.setAttribute(deleteButton, 'type', 'button');
    this.renderer.setAttribute(deleteButton, 'class', 'btn btn-danger');
    const buttonText = this.renderer.createText('Xóa');
    this.renderer.appendChild(deleteButton, buttonText);

    this.renderer.listen(deleteButton, 'click', () => this.removeAnswer(answerId));

    this.renderer.appendChild(answerDiv, input);
    this.renderer.appendChild(answerDiv, radioDiv);
    this.renderer.appendChild(answerDiv, deleteButton);

    answersContainer?.appendChild(answerDiv);
  }

  removeAnswer(answerId: string) {
    const answerDiv = document.getElementById(answerId);
    if (answerDiv) {
      answerDiv.remove();
    }
  }

  saveQuestion() {
    const questionContent = (document.getElementById('questionContent') as HTMLTextAreaElement).value;
    const questionSubject = (document.getElementById('questionSubject') as HTMLSelectElement).value;
    const answers = Array.from(document.querySelectorAll('#questionAnswers input[type="text"]')).map(input => (input as HTMLInputElement).value);
    const correctAnswerIndex = Array.from(document.querySelectorAll('#questionAnswers input[type="radio"]')).findIndex(radio => (radio as HTMLInputElement).checked);

    if (questionContent && questionSubject && answers.length > 0 && correctAnswerIndex !== -1) {
      console.log({
        questionContent,
        questionSubject,
        answers,
        correctAnswer: answers[correctAnswerIndex]
      });
      alert('Câu hỏi đã được lưu!');
      this.closeModal();
    } else {
      alert('Vui lòng điền đầy đủ thông tin và chọn một đáp án đúng!');
    }
  }
}