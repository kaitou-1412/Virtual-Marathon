import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticipantDetailsPageComponent } from './participant-details-page.component';

describe('ParticipantDetailsPageComponent', () => {
  let component: ParticipantDetailsPageComponent;
  let fixture: ComponentFixture<ParticipantDetailsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParticipantDetailsPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticipantDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
