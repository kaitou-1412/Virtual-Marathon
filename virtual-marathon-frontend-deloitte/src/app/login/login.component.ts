import { ApiService } from './../api.service';
import { Component, Input, OnInit } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  AbstractControl,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { Request } from '../model/request.model';
// import { UserNameService } from '../services/user-name.service';

interface Alert {
  type: string;
  message: string;
}

const ALERTS: Alert[] = [
  {
    type: 'warning',
    message: 'Wrong Username or Password',
  },
];

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  // form: any = {};
  // isLoggedIn = false;
  // isLoginFailed = false;
  // errorMessage = '';
  // roles: string[] = [];

  username: string = '';
  userPassword: string = '';

  isSignedin = false;
  // _subscription_user_name;
  error: string = '';
  alerts: Alert[] = [];
  wrongCredentials: boolean = false;
  // role: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private apiService: ApiService
  ) {
    // this._subscription_user_name = this.userNameService.execChange.subscribe(
    //   (value) => {
    //     this.userName = value; // this.username will hold your value and modify it every time it changes
    //   }
    // );
    this.reset();
  }

  ngOnInit(): void {
    this.isSignedin = this.userService.isUserSignedin();
    // this.userNameService.userNameChange(this.userName);
    if (this.isSignedin) {
      // console.log('hiiiiiiiiiiiiiiiiiiii');
      console.log(this.username);

      // this.router.navigate(['/home'], { state: { data: this.username } });
      this.router.navigate(['/home'], {
        state: { userName: this.username, userPassword: this.userPassword },
      });
    }
  }

  close(alert: Alert) {
    this.alerts.splice(this.alerts.indexOf(alert), 1);
  }

  reset() {
    this.alerts = Array.from(ALERTS);
  }

  doSignin() {
    // console.log(this.loginForm.value);
    this.username = this.loginForm.value.userName;
    this.userPassword = this.loginForm.value.userPassword;
    // console.log(this.userName);
    if (
      this.username !== '' &&
      this.username !== null &&
      this.userPassword !== '' &&
      this.userPassword !== null
    ) {
      const request: Request = {
        userName: this.username,
        userPassword: this.userPassword,
      };
      // console.log(request);
      this.userService.signin(request).subscribe(
        (result) => {
          //this.router.navigate(['/home']);
          // console.log('hellooooooooooooooooooooo');

          this.userService.setRole(result.user.role[0].roleName);
          this.router
            .navigate(['home'], {
              state: {
                userName: this.username,
                userPassword: this.userPassword,
              },
            })
            .then(() => {
              window.location.reload();
            });
        },
        () => {
          this.wrongCredentials = true;
          // alert('wrong username or password');
          this.error = 'Either invalid credentials or something went wrong';
        }
      );
    } else {
      this.error = 'Invalid Credentials';
    }
  }

  loginForm = new FormGroup({
    userName: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
    ]),
    userPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
    ]),
  });

  // onSubmit() {
  //   console.log(this.loginForm.value);
  //   this.checkUserName(this.username);
  // }

  // selectedOption(event: any) {
  //   console.log(event.target.value);
  //   this.role = event.target.value;
  // }

  // get userName() {
  //   return this.loginForm.controls['userName'];
  // }
}
