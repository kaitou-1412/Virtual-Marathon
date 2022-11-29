import { RoleService } from './../services/role.service';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../api.service';
import { UserService } from '../services/user.service';
// import { EventsService } from '../services/events.service';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {
  isSignedin = false;

  signedinUser: string = '';

  greeting: any[] = [];
  faIcon = faArrowRight;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private http: HttpClient,
    private userService: UserService,
    private roleService: RoleService
  ) {}
  allEvents: any;
  allUpcomingEvents: any;
  twoUpcomingEvents: any = [];
  statusCode!: null;
  organizer: boolean | undefined;
  participant: boolean | undefined;
  userId: string = '';
  data: any;

  ngOnInit(): void {
    // console.log('home page');
    // console.log(history.state.userName);
    // console.log(history.state.userPassword);
    // this.userId = history.state.data;
    this.getAllEvents();

    this.getUpcomingEvents();

    this.isSignedin = this.userService.isUserSignedin();
    this.signedinUser = this.userService.getSignedinUser();

    if (!this.userService.isUserSignedin()) {
      this.router.navigateByUrl('signin');
    }

    if (this.isSignedin) {
      this.participant = this.userService.getRole() === 'participant';
      this.organizer = !this.participant;
    }
  }

  doSignout() {
    this.userService.signout();
  }

  getAllEvents() {
    // type='Select'
    this.apiService.getAllEvents('Select', 'Select').subscribe(
      (data) => (this.allEvents = data),

      (errorCode) => (this.statusCode = errorCode)
    );
  }

  getUpcomingEvents() {
    this.apiService.getAllEvents('Select', 'Select').subscribe(
      (data) => {
        this.allEvents = data;
        // console.log('getupcomingeventsdata');

        this.allUpcomingEvents = this.allEvents.filter(
          (element: any) => element.status === 'upcoming'
        );
        // console.log(this.allUpcomingEvents);
        if (this.allUpcomingEvents.length <= 3) {
          this.twoUpcomingEvents = this.allUpcomingEvents;
        } else {
          this.twoUpcomingEvents.push(this.allUpcomingEvents[0]);
          this.twoUpcomingEvents.push(this.allUpcomingEvents[1]);
          this.twoUpcomingEvents.push(this.allUpcomingEvents[2]);
        }
        // console.log(this.twoUpcomingEvents);
      },

      (errorCode) => (this.statusCode = errorCode)
    );
  }
}
