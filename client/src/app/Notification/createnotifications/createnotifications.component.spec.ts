import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatenotificationsComponent } from './createnotifications.component';

describe('CreatenotificationsComponent', () => {
  let component: CreatenotificationsComponent;
  let fixture: ComponentFixture<CreatenotificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatenotificationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatenotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
