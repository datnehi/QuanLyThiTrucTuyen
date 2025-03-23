import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentExamStartComponent } from './student-exam-start.component';

describe('StudentExamStartComponent', () => {
  let component: StudentExamStartComponent;
  let fixture: ComponentFixture<StudentExamStartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentExamStartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentExamStartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
