// import { EventsService } from './../services/events.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ApiService } from '../api.service';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-events-page',
  templateUrl: './events-page.component.html',
  styleUrls: ['./events-page.component.scss'],
})
export class EventsPageComponent implements OnInit {
  constructor(
    private apiService: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  allEvents: any;
  statusCode!: null;
  faIcon = faArrowRight;

  param1: any;
  // param2: string = '';

  ngOnInit(): void {
    this.getAllEvents();

    // this.route.queryParamMap.subscribe((params) => {
    //   this.param1 = params.get('status');
    //   console.log(this.param1);
    // });
  }

  displayEventStatus = 'Select';
  displayEventType = 'Select';

  changeEventStatus(selectedItem: string) {
    this.displayEventStatus = selectedItem;
    // console.log(selectedItem);
    // this.router.navigate(['/events'], {
    //   queryParams: { status: selectedItem },
    //   queryParamsHandling: 'merge',
    // });
    this.apiService
      .getAllEvents(this.displayEventType, this.displayEventStatus)
      .subscribe(
        (data) => {
          this.allEvents = data;
          // console.log(this.allEvents);
        },

        (errorCode) => (this.statusCode = errorCode)
      );
  }

  changeEventType(selectedItem: string) {
    this.displayEventType = selectedItem;
    this.apiService
      .getAllEvents(this.displayEventType, this.displayEventStatus)
      .subscribe(
        (data) => {
          this.allEvents = data;
          // console.log(this.allEvents);
        },

        (errorCode) => (this.statusCode = errorCode)
      );
  }

  getAllEvents() {
    this.apiService
      .getAllEvents(this.displayEventType, this.displayEventStatus)
      .subscribe(
        (data) => {
          this.allEvents = data;
          // console.log(this.allEvents);
        },

        (errorCode) => (this.statusCode = errorCode)
      );
  }
}
