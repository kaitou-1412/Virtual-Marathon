import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';
import { ApiService } from 'src/app/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-bar-graph',
  templateUrl: './bar-graph.component.html',
  styleUrls: ['./bar-graph.component.scss'],
})
export class BarGraphComponent implements OnInit {
  userId: any;
  allEventsOfUser: any = [];
  distanceOfAllUsers: any = [];
  barChartOptions: ChartOptions = {};
  barChartLabels: Label[] = [];
  barChartType: ChartType = 'bar';
  barChartLegend = true;
  barChartPlugins = [];
  barChartData: ChartDataSets[] = [];

  constructor(
    private userService: UserService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.userId = this.userService.getSignedinUser();
    this.getDetailsAndRenderBarGraph();
  }

  getDetailsAndRenderBarGraph() {
    this.apiService.getEventsByUserId(this.userId).subscribe((data) => {
      this.allEventsOfUser = data;

      for (let trackingDetail of this.allEventsOfUser) {
        // console.log(trackingDetail.distance);

        this.distanceOfAllUsers.push(trackingDetail.distance);
        this.barChartLabels.push(trackingDetail.event.title);
      }
    });

    this.barChartOptions = {
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

    this.barChartType = 'bar';
    this.barChartLegend = true;
    this.barChartPlugins = [];

    this.barChartData = [{ data: this.distanceOfAllUsers, label: 'Distance' }];
  }
}
