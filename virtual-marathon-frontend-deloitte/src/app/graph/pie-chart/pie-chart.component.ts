import { ApiService } from './../../api.service';
import { UserService } from './../../services/user.service';
import { Component, OnInit } from '@angular/core';
import { ChartType, ChartOptions } from 'chart.js';
import {
  SingleDataSet,
  Label,
  monkeyPatchChartJsLegend,
  monkeyPatchChartJsTooltip,
} from 'ng2-charts';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.scss'],
})
export class PieChartComponent implements OnInit {
  userId: any;
  allEventsOfUser: any;
  totalEvents = 0;
  completedEvents = 0;
  completedEventsPercentage = 0;
  notCompletedEventsPercentage = 0;

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
    this.getDetailsAndRenderPieChart();
  }

  getDetailsAndRenderPieChart() {
    this.apiService.getEventsByUserId(this.userId).subscribe((data) => {
      // console.log('hello', data);
      this.allEventsOfUser = data;
      this.totalEvents = this.allEventsOfUser.length;
      for (let trackingDetail of data) {
        if (trackingDetail.hasCompletedEvent) {
          this.completedEvents += 1;
        }
      }
      this.completedEventsPercentage =
        (this.completedEvents * 100) / this.totalEvents;
      this.notCompletedEventsPercentage = 100 - this.completedEventsPercentage;
      this.pieChartOptions = {
        responsive: true,
      };
      this.pieChartLabels = [['Completed'], ['Partially Completed']];
      this.pieChartData = [
        this.completedEventsPercentage,
        this.notCompletedEventsPercentage,
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
    // this.userId = this.userService.getSignedinUser();
    // this.getDetailsAndRenderPieChart();
  }
}
