import { ApiService } from './../api.service';
import { UserService } from './../services/user.service';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-activity-page',
  templateUrl: './activity-page.component.html',
  encapsulation: ViewEncapsulation.None,
  styles: [
    `
      .success-modal .modal-body {
        font-size: 20px;
      }
      .success-modal .modal-content {
        background-color: #d1e7dd;
        color: #0f5132;
      }
      .success-modal .close {
        color: white;
      }

      .success-modal .modal-header {
        border-bottom: 1px solid #0f5132;
      }
      .success-modal .modal-footer {
        border-top: 1px solid #0f5132;
      }

      .fail-modal .modal-body {
        font-size: 20px;
      }
      .fail-modal .modal-content {
        background-color: #fff3cd;
        color: #664d03;
      }
      .fail-modal .close {
        color: white;
      }
      .fail-modal .modal-header {
        border-bottom: 1px solid #664d03;
      }
      .fail-modal .modal-footer {
        border-top: 1px solid #664d03;
      }
    `,
  ],
  styleUrls: ['./activity-page.component.scss'],
})
export class ActivityPageComponent implements OnInit {
  //using angular
  startTime!: number;
  endTime!: number;
  time!: number;
  currentCoordinates: any;
  startCoordinates!: any;
  endCoordinates!: any;
  totalDistance!: number;
  speed!: number;
  toggle: any;
  currentDistanceCovered: any;
  currentTime: any;
  success: any;
  error: any;
  state = '1';
  eventId: any;
  participantId: any;
  eventDetails: any;
  distanceSetByOrganizer: any;
  hasCompletedEvent: any;
  hasParticipatedInEvent: any;
  typeOfEvent: any;
  difficulty = 0;
  satisfaction = 0;
  ease = 0;
  status: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private apiService: ApiService,
    private modalService: NgbModal
  ) {}
  ngOnInit(): void {
    // console.log(history.state.eventId);
    // this.eventId = history.state.eventId;
    this.state = this.userService.getActivityPageState();
    this.route.paramMap.subscribe((params) => {
      this.eventId = params.get('id');
      console.log(this.eventId);
    });

    this.participantId = this.userService.getSignedinUser();
    console.log(this.participantId);
    this.getEventById(this.eventId);
  }
  declare Promise: any;
  openVerticallyCentered(content: any) {
    const modalRef = this.modalService.open(
      content,

      {
        centered: true,

        windowClass: this.hasCompletedEvent ? 'success-modal' : 'fail-modal',
      }
    );
    modalRef.result.then(
      (result) => {
        console.log(result);
        this.userService.setActivityPageState('4');
        this.state = '4';
      },
      (reason) => {
        // console.log('Dismissed action: ' + reason);
        // console.log('before state ' + this.state);
        this.userService.setActivityPageState('4');
        this.state = '4';
        // console.log('after state: ' + this.state);
      }
    );
  }

  submitFeedback() {
    console.log('s', this.satisfaction);
    console.log('e', this.ease);
    console.log('d', this.difficulty);
    const updateFeedbackData = {
      speed: this.speed,
      time: this.time,
      distance: this.totalDistance,
      hasCompletedEvent: this.hasCompletedEvent,
      difficulty: this.difficulty,
      satisfaction: this.satisfaction,
      ease: this.ease,
    };
    this.updateTrackingDetails(updateFeedbackData);
    this.userService.setActivityPageState('1');
    this.state = '1';
    this.router.navigate(['/events', this.eventId]).then(() => {
      window.location.reload();
    });
  }

  getEventById(id: any) {
    this.apiService.getEventById(id).subscribe((data: any) => {
      (this.eventDetails = data),
        (this.distanceSetByOrganizer = this.eventDetails.distance),
        (this.typeOfEvent = this.eventDetails.type),
        (this.status = this.eventDetails.status);
      // console.log(this.status);
    });
  }

  updateTrackingDetails(data: any) {
    // console.log('before update details', data);
    this.apiService
      .updateTrackingDetails(this.participantId, this.eventId, data)
      .subscribe((data: any) => {
        this.eventDetails = data;
        // console.log('after update details', this.eventDetails);
      });
  }

  //**********TRACKING**********/
  refresh = async () => {
    this.currLocation();
    await new Promise((resolve) => setTimeout(resolve, 1000));
    // this.currLocation();
  };

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

  // Convert from degrees to radians
  degreesToRadians = (degrees: number) => {
    var radians = (degrees * Math.PI) / 180;
    return radians;
  };

  // Function takes two objects, that contain coordinates to a starting and destination location.
  calcDistance = (
    startingCoords: { latitude: any; longitude: any },
    destinationCoords: { latitude: any; longitude: any }
  ) => {
    let startingLat = this.degreesToRadians(startingCoords.latitude);
    let startingLong = this.degreesToRadians(startingCoords.longitude);
    let destinationLat = this.degreesToRadians(destinationCoords.latitude);
    let destinationLong = this.degreesToRadians(destinationCoords.longitude);
    // Radius of the Earth in kilometers
    let radius = 6371;
    // Haversine equation
    let distanceInKilometers =
      Math.acos(
        Math.sin(startingLat) * Math.sin(destinationLat) +
          Math.cos(startingLat) *
            Math.cos(destinationLat) *
            Math.cos(startingLong - destinationLong)
      ) * radius;
    let distanceInMeters = distanceInKilometers * 1000;
    return distanceInMeters;
  };

  //to get current distance after every 3 seconds
  calcRealTimeDistance = async (sourceCoordinates: any) => {
    this.currentDistanceCovered = 0;
    while (this.state === '2') {
      await new Promise((resolve) => setTimeout(resolve, 3000));
      this.currLocation();
      let destinationCoordinates = this.currentCoordinates;
      this.currentTime = new Date().getSeconds();
      this.currentDistanceCovered += this.calcDistance(
        sourceCoordinates,
        destinationCoordinates
      );
      if (this.currentDistanceCovered >= this.distanceSetByOrganizer)
        this.timerEnd();
      sourceCoordinates = destinationCoordinates;
    }
    return this.currentDistanceCovered;
  };

  timerStart = async () => {
    this.startTime = Date.parse(new Date().toString());
    this.userService.setActivityPageState('2');
    this.state = '2';
    console.log('s', this.startTime);
    await this.refresh();
    this.currLocation();
    this.startCoordinates = this.currentCoordinates;
    this.totalDistance = await this.calcRealTimeDistance(this.startCoordinates);
    console.log('start', this.startCoordinates);
  };
  timerEnd = async () => {
    this.endTime = Date.parse(new Date().toString());
    console.log('e', this.endTime);
    this.userService.setActivityPageState('2.5');
    this.state = '2.5';
    this.time = (this.endTime - this.startTime) / 1000;
    await new Promise((resolve) => setTimeout(resolve, 4000));
    this.speed = this.totalDistance / this.time;

    this.hasCompletedEvent = this.totalDistance >= this.distanceSetByOrganizer;
    console.log(' this.hasCompletedEvent', this.hasCompletedEvent);
    const updateTrackingDetailsData = {
      speed: this.speed,
      time: this.time,
      distance: this.totalDistance,
      hasCompletedEvent: this.hasCompletedEvent,
      difficulty: 0,
      satisfaction: 0,
      ease: 0,
    };
    this.updateTrackingDetails(updateTrackingDetailsData);
    this.userService.setActivityPageState('3');
    this.state = '3';

    // await new Promise((resolve) => setTimeout(resolve, 5000));
    // this.router.navigate(['/events', this.eventId]);
  };
}
