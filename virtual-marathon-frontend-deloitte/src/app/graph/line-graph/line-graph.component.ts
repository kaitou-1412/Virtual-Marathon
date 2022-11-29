import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { Color, Label } from 'ng2-charts';
import { ApiService } from 'src/app/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-line-graph',
  templateUrl: './line-graph.component.html',
  styleUrls: ['./line-graph.component.scss'],
})
export class LineGraphComponent implements OnInit {
  userId: any;
  allEventsOfUser: any = [];
  speedOfAllUsers: any = [];

  lineChartData: ChartDataSets[] = [];
  lineChartLabels: Label[] = [];
  lineChartOptions = {};
  lineChartColors: Color[] = [];
  lineChartLegend: boolean = true;
  lineChartPlugins = [];
  lineChartType: ChartType = 'line';

  constructor(
    private userService: UserService,
    private apiService: ApiService
  ) {
    this.userId = this.userService.getSignedinUser();
    this.getDetailsAndRenderLineGraph();
  }

  ngOnInit(): void {}

  getDetailsAndRenderLineGraph() {
    this.apiService.getEventsByUserId(this.userId).subscribe((data) => {
      this.allEventsOfUser = data;

      for (let trackingDetail of this.allEventsOfUser) {
        // console.log(trackingDetail.speed);

        this.speedOfAllUsers.push(trackingDetail.speed);
        this.lineChartLabels.push(trackingDetail.event.title);
      }
    });

    this.lineChartData = [{ data: this.speedOfAllUsers, label: 'Speed' }];

    // this.lineChartLabels = [
    //   'January',
    //   'February',
    //   'March',
    //   'April',
    //   'May',
    //   'June',
    // ];

    this.lineChartOptions = {
      responsive: true,
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
            },
          },
        ],
      },
    };

    this.lineChartColors = [
      {
        borderColor: 'black',
        backgroundColor: 'rgba(255,255,0,0.28)',
      },
    ];

    this.lineChartLegend = true;
    this.lineChartPlugins = [];
    this.lineChartType = 'line';
  }
}
