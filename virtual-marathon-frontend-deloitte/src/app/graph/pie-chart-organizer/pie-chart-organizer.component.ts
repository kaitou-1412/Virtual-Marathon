import { Component, Input, OnInit } from '@angular/core';
import { UserService } from './../../services/user.service';
import { ApiService } from './../../api.service';
import { ChartType, ChartOptions } from 'chart.js';
import {
  SingleDataSet,
  Label,
  monkeyPatchChartJsLegend,
  monkeyPatchChartJsTooltip,
} from 'ng2-charts';

@Component({
  selector: 'app-pie-chart-organizer',
  templateUrl: './pie-chart-organizer.component.html',
  styleUrls: ['./pie-chart-organizer.component.scss'],
})
export class PieChartOrganizerComponent implements OnInit {
  @Input() eventId!: any;

  userId: any;
  allTrackingDetailsOfAEvent: any;
  totalEvents = 0;
  completedEvents = 0;
  completedEventsPercentage = 0;
  notCompletedEventsPercentage = 0;
  completedEventParticipant = 0;
  totalParticipants = 0;
  completedParticipantsPercentage = 0;
  notCompletedParticipantsPercentage = 0;

  pieChartOptions: ChartOptions = {};
  pieChartLabels: Label[] = [];
  pieChartData: SingleDataSet = [];
  pieChartType: ChartType = 'pie';
  pieChartLegend: boolean = true;
  pieChartPlugins: any;
  pieChartColors: Array<any> = [];
  constructor(
    private userService: UserService,
    private apiService: ApiService
  ) {
    monkeyPatchChartJsTooltip();
    monkeyPatchChartJsLegend();
    this.userId = this.userService.getSignedinUser();
  }

  getDetailsAndRenderPieChart() {
    // console.log('eventid', this.eventId);
    this.apiService
      .readTrackingDetailsByEventId(this.eventId)
      .subscribe((data) => {
        // console.log('allTrackingDetailsOfAEvent', data);
        this.allTrackingDetailsOfAEvent = data;

        this.totalParticipants = this.allTrackingDetailsOfAEvent.length;
        if (this.totalParticipants !== 0) {
          for (let trackingDetail of this.allTrackingDetailsOfAEvent) {
            if (trackingDetail.hasCompletedEvent) {
              this.completedEventParticipant += 1;
            }
          }
          this.completedParticipantsPercentage =
            (this.completedEventParticipant * 100) / this.totalParticipants;
          this.notCompletedParticipantsPercentage =
            100 - this.completedParticipantsPercentage;
        } else {
          this.completedParticipantsPercentage = 0;
          this.notCompletedParticipantsPercentage = 100;
        }
        this.pieChartOptions = {
          responsive: true,
        };
        this.pieChartLabels = [['Completed'], ['Partially Completed']];
        this.pieChartData = [
          this.completedParticipantsPercentage,
          this.notCompletedParticipantsPercentage,
        ];
        // this.pieChartData = [50, 50];
        this.pieChartType = 'pie';
        this.pieChartLegend = true;
        this.pieChartPlugins = [];
        this.pieChartColors = [
          {
            backgroundColor: ['#c1e7ff', '#346888'],
            borderColor: ['rgba(252, 235, 89, 0.2)', 'rgba(77, 152, 202, 0.2)'],
          },
        ];

        // console.log('t', this.totalEvents, 'c', this.completedEvents);
      });
  }

  ngOnInit(): void {
    // console.log('eventid pie-chart', this.eventId);
  }

  ngOnChanges() {
    // console.log('eventid pie-chart', this.eventId);
    this.getDetailsAndRenderPieChart();
  }
}
