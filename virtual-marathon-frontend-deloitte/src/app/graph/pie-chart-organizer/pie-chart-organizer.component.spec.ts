import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PieChartOrganizerComponent } from './pie-chart-organizer.component';

describe('PieChartOrganizerComponent', () => {
  let component: PieChartOrganizerComponent;
  let fixture: ComponentFixture<PieChartOrganizerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PieChartOrganizerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PieChartOrganizerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
