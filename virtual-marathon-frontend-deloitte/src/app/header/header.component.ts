import { RoleService } from './../services/role.service';
import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
// import { UserAuthService } from '../services/user-auth.service';
import { UserService } from '../services/user.service';
import { Location } from '@angular/common';
import { faPersonRunning } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  public isMenuCollapsed = true;
  isSignedin = false;
  signedinUser: string = '';
  organizer = false;
  participant = false;
  mySubscription: any;
  userName = '';
  faIcon = faPersonRunning;

  greeting: any[] = [];

  constructor(
    private router: Router,
    public userService: UserService,
    public roleService: RoleService,
    public location: Location
  ) {}

  ngOnInit(): void {
    this.isSignedin = this.userService.isUserSignedin();
    this.signedinUser = this.userService.getSignedinUser();
    // console.log('user service: ' + this.userService.isOrganizer());

    if (!this.userService.isUserSignedin()) {
      this.router.navigateByUrl('signin');
    }

    if (this.isSignedin) {
      this.participant = this.userService.getRole() === 'participant';
      this.organizer = !this.participant;
    }
  }

  public reload() {
    window.location.reload();
  }
  public isLoggedIn() {
    return this.userService.isUserSignedin();
  }

  public logout() {
    this.userService.signout();
    this.router.navigateByUrl('welcome');
  }
}
