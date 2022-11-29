import { RoleService } from './../services/role.service';
import { UserService } from './../services/user.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../api.service';

import {
  faCalendar,
  faPersonBiking,
  faPersonRunning,
  faLockOpen,
  faLock,
  faClock,
  faPenToSquare,
  faTrashCan,
} from '@fortawesome/free-solid-svg-icons';
// import { EventsService } from '../services/events.service';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss'],
})
export class EventDetailsComponent implements OnInit {
  constructor(
    private apiService: ApiService,
    private route: ActivatedRoute,
    private userService: UserService,
    private roleService: RoleService,
    private router: Router
  ) {}
  active = 1;
  statusCode: null | undefined;
  eventName: string = '';
  nameOfOrganizer: string = '';
  organizerIdOfSelectedEvent: string = '';
  startDate: string = '';
  endDate: string = '';
  monthStartDate: any;
  dateStartDate: any;
  yearStartDate: any;
  monthEndDate: any;
  dateEndDate: any;
  yearEndDate: any;
  type: string = '';
  imageUrl: string = '';
  faqs: any = [];
  event: any;
  status: any;
  id!: string | null;
  userName: string = '';
  data: any;
  userEvents: any;
  state = 1;
  i: any;
  trackingDetailsArrayLength: any;
  signedinUser: string = '';
  participant: boolean = false;
  organizer: boolean = false;
  isSignedin: boolean = false;
  success: any;
  currentCoordinates: any;
  error: any;
  hasStartedEvent = false;
  trackingDetails: any;
  imageId: any;
  postResponse: any;
  dbImage: any;
  faPenToSquare = faPenToSquare;
  faTrashCan = faTrashCan;
  faPersonBiking = faPersonBiking;
  faPersonRunning = faPersonRunning;
  faLockOpen = faLockOpen;
  faLock = faLock;
  faClock = faClock;

  monthNames = [
    'Jan',
    'Feb',
    'Mar',
    'Apr',
    'May',
    'June',
    'July',
    'Aug',
    'Sep',
    'Oct',
    'Nov',
    'Dec',
  ];

  ngOnInit(): void {
    // console.log('testing');
    // console.log('Before testing');
    // console.log('state in event-detail', this.state);
    // console.log('hasStartedEvent ', this.hasStartedEvent);

    // this.state = this.hasStartedEvent;

    this.route.paramMap.subscribe((params) => {
      this.id = params.get('id');
      // console.log(this.id);
      this.getEventById(this.id);
      this.isSignedin = this.userService.isUserSignedin();
      this.signedinUser = this.userService.getSignedinUser();
    });

    this.userName = this.userService.getSignedinUser();
    this.getEventsByUserId(this.userName);
    this.readTrackingDetailsByEventId();
    this.readTrackingDetailsByParticipantNameAndEventId();
    this.getUserById();
    // console.log('After testing');
    // console.log('state in event-detail', this.state);
    // console.log('hasStartedEvent ', this.hasStartedEvent);
  }

  onRegisterParticipant() {
    this.data = {
      time: 0,
      distance: 0,
      speed: 0,
      hasCompletedEvent: false,
      difficulty: 0,
      satisfaction: 0,
      ease: 0,
    };
    // console.log(this.data);
    this.apiService
      .registerUserInNewEvent(this.userName, this.id, this.data)
      .subscribe((data: any) => {
        // console.log(data);
      });
    this.state = 0;
  }

  currLocation = () => {
    let options = {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0,
    };
    this.success = (pos: any) => {
      let crd = pos.coords;

      this.currentCoordinates = crd;
    };
    this.error = (err: { code: any; message: any }) => {
      console.warn(`ERROR(${err.code}): ${err.message}`);
    };
    navigator.geolocation.getCurrentPosition(this.success, this.error, options);
  };

  goToRaceButton() {
    this.currLocation();
    this.userService.setActivityPageState('1');
    this.router.navigate(['/activity', this.id]);
  }

  onCancelParticipant() {
    this.apiService
      .deleteEventRegistration(this.userName, this.id)
      .subscribe((data: any) => console.log(data));
    this.state = 1;
  }

  onUpdateOrganizer() {
    // console.log('update');

    this.router.navigate(['createEvent'], {
      queryParams: { eventId: this.id, createOrUpdateState: 2 },
    });
  }

  //to get role of user
  getUserById() {
    this.apiService.getUserById(this.signedinUser).subscribe((data: any) => {
      // console.log(data);
      this.participant = data.role[0].roleName === 'participant';
      // console.log(this.participant);
      this.organizer = !this.participant;
    });
  }

  async onDeleteOrganizer() {
    // console.log('delete');
    this.apiService
      .deleteEvent(this.signedinUser, this.id)
      .subscribe((data: any) => console.log(data));
    await new Promise((resolve) => setTimeout(resolve, 2000));
    this.router.navigateByUrl('/events');
  }

  getEventsByUserId(userid: any) {
    this.apiService.getEventsByUserId(userid).subscribe((data: any) => {
      this.userEvents = data;
      // console.log(this.userEvents.length);
      //added to see if user has alreadt registered to a event the register button is not displayed
      for (this.i = 0; this.i < this.userEvents.length; this.i++) {
        // console.log(this.userEvents[this.i]);
        if (this.userEvents[this.i].event.id == this.id) {
          this.state = 0;
        }
      }
    });
  }

  getEventById(id: any) {
    this.apiService.getEventById(id).subscribe((data: any) => {
      // console.log(data),
      (this.eventName = data.title),
        (this.nameOfOrganizer = data.organizer.userFullName),
        (this.startDate = data.startDate),
        (this.endDate = data.endDate),
        (this.type = data.type),
        (this.imageUrl = data.imageUrl),
        (this.event = data),
        (this.faqs = data.faq),
        (this.organizerIdOfSelectedEvent = data.organizer.userName),
        (this.imageId = this.event.image.id),
        (this.status = this.event.status),
        (errorCode: any) => (this.statusCode = errorCode),
        this.viewImage();
      this.monthStartDate =
        this.monthNames[Number(this.event.startDate?.split('/')[1]) - 1];
      this.dateStartDate = this.event.startDate?.split('/')[0];
      this.yearStartDate = this.event.startDate?.split('/')[2];
      this.monthEndDate =
        this.monthNames[Number(this.event.endDate?.split('/')[1]) - 1];
      this.dateEndDate = this.event.endDate?.split('/')[0];
      this.yearEndDate = this.event.endDate?.split('/')[2];
    });
  }

  readTrackingDetailsByEventId() {
    this.apiService
      .readTrackingDetailsByEventId(this.id)
      .subscribe((data: any) => {
        // console.log('tracking details by event id', data);

        this.trackingDetails = data;
        this.trackingDetails = this.trackingDetails.filter(
          (item: any) => item.hasCompletedEvent
        );
        this.trackingDetailsArrayLength = this.trackingDetails.length;
      });
  }

  readTrackingDetailsByParticipantNameAndEventId() {
    this.apiService
      .readTrackingDetailsByParticipantNameAndEventId(this.userName, this.id)
      .subscribe((data: any) => {
        // console.log('data.time', data.time);
        this.hasStartedEvent = data.time !== 0;
        // console.log(
        //   'readTrackingDetailsByParticipantNameAndEventId',
        //   data.time !== 0
        // );
      });
  }

  viewImage() {
    // this.imageId = this.event.image.id;
    this.apiService.viewImage(this.imageId).subscribe((res) => {
      this.postResponse = res;
      // console.log('post', this.postResponse);
      this.dbImage = 'data:image/jpeg;base64,' + this.postResponse.image;
    });
  }
}
