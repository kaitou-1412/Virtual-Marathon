import { UserService } from './../services/user.service';
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../api.service';
// import { EventsService } from '../services/events.service';
// import { UserService } from '../services/user.service';
import { faArrowRight, faUser } from '@fortawesome/free-solid-svg-icons';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-participant-details-page',
  templateUrl: './participant-details-page.component.html',
  styleUrls: ['./participant-details-page.component.scss'],
})
export class ParticipantDetailsPageComponent implements OnInit {
  isSignedin: any;
  participant: boolean = false;
  organizer: boolean = false;
  allEventsOfUser: any;
  allTheRegisteredEvents: any;
  statusCode: any;
  name: string = '';
  username: string = '';
  gmail: string = '';
  role: string = '';
  id: string | null = '';
  faIcon = faArrowRight;
  graphStateParticipant = 1;
  graphStateOrganizer = 1;
  eventId: any;
  eventTitle: any;
  active = 'profile';
  faUser = faUser;
  closeResult = '';

  constructor(
    private apiService: ApiService,
    private route: ActivatedRoute,
    private userService: UserService,
    private modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  // graphForm: FormGroup | undefined;
  // graphTypeo: number[] = [1, 2, 3];

  ngOnInit(): void {
    this.isSignedin = this.userService.isUserSignedin();
    if (this.isSignedin) {
      this.participant = this.userService.getRole() === 'participant';
      this.organizer = !this.participant;
    }

    this.route.paramMap.subscribe((params) => {
      this.id = params.get('id');
      // console.log('id recieved in details page ', this.id);
      this.getUserById(this.id);
      if (this.participant) this.getEventsByUserId(this.id);
      if (this.organizer) this.getEventByOrganizerId();
    });
  }

  profileUpdateForm = this.fb.group({
    userFullName: ['', Validators.required],
    userPassword: ['', Validators.required],
    gmail: ['', Validators.required],
  });

  onProfileUpdateFormSubmit() {
    // console.log(this.profileUpdateForm.value);
    this.updateUserDetails(this.id, this.profileUpdateForm.value);
    window.location.reload();
  }

  open(content: any) {
    this.modalService
      .open(content, { ariaLabelledBy: 'modal-basic-title' })
      .result.then(
        (result) => {
          this.closeResult = `Closed with: ${result}`;
        },
        (reason) => {
          // this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        }
      );
  }

  pieChart() {
    this.graphStateParticipant = 1;
  }

  lineGraph() {
    this.graphStateParticipant = 2;
  }

  barGraph() {
    this.graphStateParticipant = 3;
  }

  pieChartCompletionRate() {
    this.graphStateOrganizer = 1;
  }
  BarGraphFeedback() {
    this.graphStateOrganizer = 2;
  }

  selectedEvent(id: any) {
    this.eventId = id;
    // console.log(id);
    this.apiService
      .getEventById(this.eventId)
      .subscribe((data) => (this.eventTitle = data.title));
  }

  getEventByOrganizerId() {
    this.apiService
      .getEventByOrganizerId(this.userService.getSignedinUser())
      .subscribe(
        (data) => {
          // console.log(data);
          this.allTheRegisteredEvents = data;
          this.eventId = data[0].id;
          this.eventTitle = data[0].title;
          // console.log('eventid p', this.eventId);
        },
        (errorCode) => (this.statusCode = errorCode)
      );
  }

  getEventsByUserId(id: any) {
    this.apiService.getEventsByUserId(id).subscribe(
      (data) => {
        // console.log(data);
        this.allEventsOfUser = data;
      },
      (errorCode) => (this.statusCode = errorCode)
    );
  }

  getUserById(id: any) {
    this.apiService.getUserById(id).subscribe(
      (data) => (
        (this.name = data.userFullName),
        (this.username = data.userName),
        (this.gmail = data.gmail),
        (this.role = data.role[0].roleName),
        (errorCode: any) => (this.statusCode = errorCode),
        this.profileUpdateForm.patchValue({
          userFullName: data.userFullName,
          gmail: data.gmail,
        })
      )
    );
  }

  updateUserDetails(id: any, data: any) {
    this.apiService.updateUserDetails(id, data).subscribe();
  }
}
