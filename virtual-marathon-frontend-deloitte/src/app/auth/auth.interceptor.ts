import { ApiService } from './../api.service';
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HTTP_INTERCEPTORS,
  HttpHeaders,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';

import { UserService } from '../services/user.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private userService: UserService,
    private router: Router,
    private apiService: ApiService
  ) {}

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if (this.userService.isUserSignedin() && this.userService.getToken()) {
      const request = req.clone({
        headers: new HttpHeaders({
          Authorization: this.userService.getToken(),
        }),
      });
      return next.handle(request).pipe(
        catchError((err) => {
          if (err instanceof HttpErrorResponse && err.status === 401) {
            this.userService.signout();
          }
          return throwError(err);
        })
      );
    }

    return next.handle(req);
  }
}
