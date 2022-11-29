import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarGraphFeedbackOrganizerComponent } from './bar-graph-feedback-organizer.component';

describe('BarGraphFeedbackOrganizerComponent', () => {
  let component: BarGraphFeedbackOrganizerComponent;
  let fixture: ComponentFixture<BarGraphFeedbackOrganizerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BarGraphFeedbackOrganizerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BarGraphFeedbackOrganizerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
