import { RoleService } from './role.service';
import { RequestSignUp } from './../model/requestSignup.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, tap } from 'rxjs';
// import { UserAuthService } from './user-auth.service';
import { Request } from '../model/request.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  // private baseUrl = 'http://localhost:8080/';
  private baseUrl = environment.url + '/';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private roleService: RoleService
  ) {}

  signin(request: Request): Observable<any> {
    return this.http
      .post<any>(this.baseUrl + 'authenticate', request, {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      })
      .pipe(
        map((resp) => {
          window.sessionStorage.setItem('user', request.userName);
          window.sessionStorage.setItem('token', 'Bearer ' + resp.jwtToken);
          // for activity page
          window.sessionStorage.setItem('stateActivityPage', '1');
          window.sessionStorage.setItem('role', '');

          return resp;
        })
      );
  }

  signup(request: RequestSignUp, role: string): Observable<any> {
    //registerNewOrganizer

    if (role == 'organizer') {
      return this.http
        .post<any>(this.baseUrl + 'registerNewOrganizer', request, {
          headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
          responseType: 'text' as 'json',
        })
        .pipe(
          map((resp) => {
            return resp;
          })
        );
    }

    return this.http
      .post<any>(this.baseUrl + 'registerNewParticipant', request, {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
        responseType: 'text' as 'json',
      })
      .pipe(
        map((resp) => {
          return resp;
        })
      );
  }

  signout() {
    window.sessionStorage.removeItem('user');
    window.sessionStorage.removeItem('token');
    window.sessionStorage.removeItem('stateActivityPage');
    window.sessionStorage.removeItem('role');

    this.router.navigateByUrl('login');
  }

  isUserSignedin() {
    return sessionStorage.getItem('token') !== null;
  }

  getSignedinUser() {
    return sessionStorage.getItem('user') as string;
  }

  getToken() {
    let token = sessionStorage.getItem('token') as string;
    return token;
  }

  setActivityPageState(state: any) {
    window.sessionStorage.setItem('stateActivityPage', state);
  }

  getActivityPageState() {
    return sessionStorage.getItem('stateActivityPage') as string;
  }

  setRole(role: any) {
    window.sessionStorage.setItem('role', role);
  }

  getRole() {
    return sessionStorage.getItem('role') as string;
  }
}
