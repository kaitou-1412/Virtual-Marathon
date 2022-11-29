import { ApiService } from './../api.service';
import { Router } from '@angular/router';
import { UserService } from './../services/user.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Request } from '../model/request.model';
import { RequestSignUp } from './../model/requestSignup.model';

interface Alert {
  type: string;
  message: string;
}

const ALERTS: Alert[] = [
  {
    type: 'warning',
    message: 'Username already taken',
  },
];

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent implements OnInit {
  role: string = '';
  constructor(
    private userService: UserService,
    private router: Router,
    private apiService: ApiService
  ) {
    this.reset();
  }
  userName: string = '';
  userPassword: string = '';
  userFullName: string = '';
  gmail: string = '';

  user_roles: any = [
    { name: 'organizer', value: 'organizer', selected: false },
    { name: 'participant', value: 'participant', selected: false },
  ];

  selectedRoles: string[] = [];

  error: string = '';
  success: string = '';
  uniqueUser: boolean = true;
  alerts: Alert[] = [];

  ngOnInit(): void {}

  signupForm = new FormGroup({
    userName: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
    ]),
    userPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
    ]),
    userFullName: new FormControl('', Validators.required),
    gmail: new FormControl('', [Validators.required, Validators.email]),
  });

  onSubmit() {
    console.log(this.signupForm.value);
  }

  selectedCategory(event: any) {
    this.role = event.target.value;
    console.log(this.role);
    this.selectedRoles.push(this.role);
  }

  // checkUniqueUserName() {
  //   this.apiService
  //     .checkUniqueUserName(this.userName)
  //     .subscribe((isUniqueUserName) => console.log(isUniqueUserName));
  // }

  close(alert: Alert) {
    this.alerts.splice(this.alerts.indexOf(alert), 1);
  }

  reset() {
    this.alerts = Array.from(ALERTS);
  }

  doSignup() {
    console.log(this.signupForm.value);
    this.userName = this.signupForm.value.userName;
    this.apiService.checkUniqueUserName(this.userName).subscribe((res) => {
      console.log('unique', res);
      this.uniqueUser = res;
      // if (!res) alert('Username already taken!');
    });
    this.userPassword = this.signupForm.value.userPassword;
    this.userFullName = this.signupForm.value.userFullName;
    this.gmail = this.signupForm.value.gmail;
    if (
      this.userName !== '' &&
      this.userName !== null &&
      this.userPassword !== '' &&
      this.userPassword !== null &&
      this.selectedRoles.length > 0
    ) {
      console.log('generating request');
      const request: RequestSignUp = {
        userName: this.userName,
        userPassword: this.userPassword,
        userFullName: this.userFullName,
        gmail: this.gmail,
      };
      console.log('signup request', request);
      this.role = this.selectedRoles[0];
      this.userService.signup(request, this.role).subscribe(
        (result) => {
          console.log(result);
          //this.success = 'Signup successful';
          const signinRequest: Request = {
            userName: request.userName,
            userPassword: request.userPassword,
          };
          if (this.uniqueUser) {
            this.userService.signin(signinRequest).subscribe((result) => {
              this.userService.setRole(result.user.role[0].roleName);
              this.router
                .navigate(['home'], {
                  state: {
                    userName: this.userName,
                    userPassword: this.userPassword,
                  },
                })
                .then(() => {
                  window.location.reload();
                });
            });
          }
          // this.router.navigate(['/login']);
          // this.router.navigate(['home']).then(() => {
          //   window.location.reload();
          // });
          this.success = result;
        },
        (err) => {
          //console.log(err);
          this.error = 'Something went wrong during signup';
        }
      );
    } else {
      this.error = 'All fields are mandatory';
    }
  }
}
