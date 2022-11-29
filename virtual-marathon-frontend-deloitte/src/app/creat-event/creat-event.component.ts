import { UserService } from './../services/user.service';
import { ApiService } from './../api.service';
import { Component, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DateValidator } from './date.validator';
import { CurrentDateValidator } from './current-date.validator';

@Component({
  selector: 'app-creat-event',
  templateUrl: './creat-event.component.html',
  styleUrls: ['./creat-event.component.scss'],
})
export class CreatEventComponent implements OnInit {
  organizerId: string = '';
  eventId: any;
  data: any;
  isEndDateDisabled: boolean = true;

  uploadedImage!: File;
  dbImage: any;
  postResponse: any;
  successResponse: string = '';
  image: any;
  imageId: any;
  createOrUpdateState = 0;
  // faqHtml: any;
  // c = 0;

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log(this.eventForm?.get('startDate')?.value === '');
    this.organizerId = this.userService.getSignedinUser();
    this.route.queryParams.subscribe((params) => {
      this.eventId = params['eventId'];
      this.createOrUpdateState = Number(params['createOrUpdateState']);
      // console.log('state', this.createOrUpdateState);
      // console.log('type ', typeof this.createOrUpdateState);
    });

    if (this.eventId) {
      this.apiService.getEventById(this.eventId).subscribe(
        (res: any) => {
          this.data = res;
          // console.log('data', this.data);
          // this.projectId = res.projectID;
          // console.log('info', res);
          // console.log('data', this.data.faq);
          // this.faqHtml = this.data.faq;
          // console.log('faqhtml', this.faqHtml);
          this.eventForm.patchValue({
            title: this.data.title,
            imageId: this.data.imageId,
            startDate: this.data.startDate.split('/').reverse().join('-'),
            endDate: this.data.endDate.split('/').reverse().join('-'),
            type: this.data.type,
            status: this.data.status,
            description: this.data.description,
            distance: this.data.distance,
            // faq: this.,
            // faq: this.fb.array(
            //   this.data.faq.map((faqItem: any) => {
            //     this.buildFaqItem(faqItem);
            //   })
            // ),
          });

          this.eventForm.setControl('faq', this.fb.array(this.data.faq || []));
          // this.faq=this.eventForm.controls['faq']);
          // console.log('faq', this.eventForm.controls['faq']);
          // this.faqHtml = this.eventForm.controls['faq'];
          // console.log('faqhtml', this.faqHtml);
        },
        () => {
          alert('Some Error Occured!!!');
        }
      );
    }
  }

  buildFaqItem(faqItem: any) {
    return this.fb.group({
      question: new FormControl(faqItem.question),
      answer: new FormControl(faqItem.answer),
    });
  }

  eventForm = this.fb.group(
    {
      title: ['', [Validators.required, Validators.maxLength(40)]],
      imageId: [''],
      startDate: [
        '',
        [Validators.required, CurrentDateValidator.LessThanToday],
      ],
      // , disabled: true
      endDate: [
        { value: '' },
        [Validators.required, CurrentDateValidator.LessThanToday],
      ],
      type: ['', Validators.required],
      status: ['upcoming'],
      description: ['', Validators.required],
      distance: ['', Validators.required],
      faq: this.fb.array([]),
    },
    {
      validator: [DateValidator.dateLessThan('startDate', 'endDate')],
    }
  );

  startDateChange() {
    if (!this.eventForm.get('startDate')?.valid)
      this.eventForm.controls['endDate'].disable();
    else {
      this.eventForm.controls['endDate'].enable();
    }
  }

  //To upload image

  public onImageUpload(event: any) {
    this.uploadedImage = event.target.files[0];
  }

  imageUploadAction() {
    const imageFormData = new FormData();
    imageFormData.append('image', this.uploadedImage, this.uploadedImage.name);

    this.apiService.imageUploadAction(imageFormData).subscribe((response) => {
      if (response.status === 200) {
        this.postResponse = response;
        this.successResponse = this.postResponse.body.message;
        this.imageId = this.postResponse.body.imageId;
      } else {
        this.successResponse = 'Image not uploaded due to some error!';
      }
    });
  }

  async onEventFormSubmit() {
    this.imageUploadAction();
    await new Promise((resolve) => setTimeout(resolve, 2000));
    let event = this.eventForm.value;
    let currentDate = new Date();
    let formStartDate = new Date(this.eventForm.value.startDate);
    console.log("type of startDate from form", typeof formStartDate);
    console.log("type of currentDate from form", typeof currentDate);
    console.log("sdfedwsf", this.checkDatesEqual(formStartDate, currentDate));
    if(this.checkDatesEqual(formStartDate, currentDate)) {
      this.eventForm.value.status = 'open';
    }
    console.log('event', event);
    // console.log('im1', this.imageId);
    // console.log('eim1', event.imageId);
    event.imageId = this.imageId;
    this.eventForm.value.imageId = this.imageId;
    // console.log('im2', this.imageId);
    // console.log('eim2', event.imageId);
    // console.log('type', typeof event.imageId);
    // console.log(event);
    // console.log(this.eventForm.value);

    event.startDate = event.startDate?.split('-').reverse().join('/');
    event.endDate = event.endDate?.split('-').reverse().join('/');
    // console.log(event.startDate);

    if (this.eventId) {
      this.apiService
        .updateEvent(
          this.organizerId,
          this.eventId,
          this.imageId,
          this.eventForm.value
        )
        .subscribe((data) => console.log(data));
    } else {
      this.apiService
        .createNewEvent(this.organizerId, event.imageId, this.eventForm.value)
        .subscribe((data) => console.log(data));
    }
    // to clear form after submit
    this.eventForm.reset();
    this.router.navigateByUrl('/events').then(() => {
      window.location.reload();
    });
  }

  get faq() {
    // console.log('faq', this.eventForm?.controls['faq'].value);
    return this.eventForm?.controls['faq'] as FormArray;
  }
  addFaq() {
    // console.log(this.eventForm.value);
    // console.log('add faq before', this.eventForm.controls['faq'].value);
    const faqForm = this.fb.group({
      question: ['', Validators.required],
      answer: ['', Validators.required],
    });
    // console.log('add faq after', this.faq.value);
    this.faq.push(faqForm);
  }
  checkDatesEqual(date1: Date, date2: Date) {
    return date1.getFullYear() === date2.getFullYear() 
    && date1.getMonth() === date2.getMonth() 
    && date1.getDate() === date2.getDate();
  }
}
