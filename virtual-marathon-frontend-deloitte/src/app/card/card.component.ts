import { ApiService } from 'src/app/api.service';
import { Component, Input, OnInit } from '@angular/core';
import {
  faArrowRight,
  faCalendar,
  faPersonBiking,
  faPersonRunning,
  faLockOpen,
  faLock,
  faClock,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
})
export class CardComponent implements OnInit {
  @Input() event!: any;
  faArrowRight = faArrowRight;
  faCalendar = faCalendar;
  faPersonBiking = faPersonBiking;
  faPersonRunning = faPersonRunning;
  faLockOpen = faLockOpen;
  faLock = faLock;
  faClock = faClock;
  date: any;
  month: any;
  year: any;
  // title: any;
  imageId: any;
  postResponse: any;
  dbImage: any;

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
  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.month =
      this.monthNames[Number(this.event.startDate.split('/')[1]) - 1];
    this.date = this.event.startDate.split('/')[0];
    this.year = this.event.startDate.split('/')[2];

    this.viewImage();
  }

  viewImage() {
    this.imageId = this.event.image.id;
    this.apiService.viewImage(this.event.image.id).subscribe((res) => {
      this.postResponse = res;
      // console.log('post', this.postResponse);
      this.dbImage = 'data:image/jpeg;base64,' + this.postResponse.image;
    });
  }
}
