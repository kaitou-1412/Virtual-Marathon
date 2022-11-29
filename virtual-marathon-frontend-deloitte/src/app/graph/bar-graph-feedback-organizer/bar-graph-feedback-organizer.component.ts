import { Component, Input, OnInit } from '@angular/core';
import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';
import { ApiService } from 'src/app/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-bar-graph-feedback-organizer',
  templateUrl: './bar-graph-feedback-organizer.component.html',
  styleUrls: ['./bar-graph-feedback-organizer.component.scss'],
})
export class BarGraphFeedbackOrganizerComponent implements OnInit {
  @Input() eventId!: any;
  userId: any;
  allFeedbackOfEvent: any = [];
  difficultyOfEvent: any = [];
  satisfactionOfEvent: any = [];
  easeOfEvent: any = [];
  barChartOptions: ChartOptions = {};
  barChartLabels: Label[] = [];
  barChartType: ChartType = 'bar';
  barChartLegend = true;
  barChartPlugins = [];
  barChartData1: ChartDataSets[] = [];
  barChartData2: ChartDataSets[] = [];
  barChartData3: ChartDataSets[] = [];

  constructor(
    private userService: UserService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.userId = this.userService.getSignedinUser();
  }

  ngOnChanges() {
    // console.log('eventid pie-chart', this.eventId);
    this.getDetailsAndRenderBarGraph();
  }

  getDetailsAndRenderBarGraph() {
    this.difficultyOfEvent = [];
    this.satisfactionOfEvent = [];
    this.easeOfEvent = [];
    this.barChartLabels = [];

    this.apiService
      .readTrackingDetailsByEventId(this.eventId)
      .subscribe((data) => {
        this.allFeedbackOfEvent = data;

        for (let feedback of this.allFeedbackOfEvent) {
          this.difficultyOfEvent.push(feedback.difficulty);
          this.satisfactionOfEvent.push(feedback.satisfaction);
          this.easeOfEvent.push(feedback.ease);
          this.barChartLabels.push(feedback.participant.userFullName);
        }
      });

    this.barChartOptions = {
      responsive: true,
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
              max: 5
            },
          },
        ],
      },
    };

    this.barChartType = 'bar';
    this.barChartLegend = true;
    this.barChartPlugins = [];

    this.barChartData1 = [
      { data: this.difficultyOfEvent, label: 'Difficulty' },
    ];
    this.barChartData2 = [
      { data: this.satisfactionOfEvent, label: 'Satisfaction' },
    ];
    this.barChartData3 = [{ data: this.easeOfEvent, label: 'Ease of Access ' }];
  }
}
