import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailExamComponent } from './detail-exam.component';

describe('DetailExamComponent', () => {
  let component: DetailExamComponent;
  let fixture: ComponentFixture<DetailExamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailExamComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailExamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
