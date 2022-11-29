import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpHeaders,
  HttpResponse,
  HttpParams,
} from '@angular/common/http';

import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}

  // url = 'http://localhost:8080';
  url = environment.url;


  //to get all events

  getAllEvents(type: any, status: any): Observable<any> {
    // console.log('getAllEvents');
    // console.log('type:', type, 'status:', status);
    if (type === 'Select' && status === 'Select') {
      return this.http.get<any>(this.url + '/readEvents').pipe(
        // tap((issues) => console.log(issues)),
        catchError(this.handleError)
      );
    } else if (status == 'Select') {
      return this.http
        .get<any>(this.url + '/readEvents' + '?type=' + type)
        .pipe(
          // tap((issues) => console.log(issues)),
          catchError(this.handleError)
        );
    } else if (type == 'Select') {
      return this.http
        .get<any>(this.url + '/readEvents' + '?status=' + status)
        .pipe(
          // tap((issues) => console.log(issues)),
          catchError(this.handleError)
        );
    }
    return this.http
      .get<any>(
        this.url + '/readEvents' + '?type=' + type + '&' + 'status=' + status
      )
      .pipe(
        // tap((issues) => console.log(issues)),
        catchError(this.handleError)
      );
  }

  // , { headers: headers }

  //to get events by event id
  getEventById(id: any): Observable<any> {
    return this.http.get<any>(this.url + '/readEvent' + '/' + id).pipe(
      // tap((user) => console.log(user)),
      catchError(this.handleError)
    );
  }

  //to get events by organizerId

  getEventByOrganizerId(organizerId: any): Observable<any> {
    return this.http
      .get<any>(this.url + '/readEventsByOrganizerName' + '/' + organizerId)
      .pipe(
        // tap((user) => console.log(user)),
        catchError(this.handleError)
      );
  }

  //to get specific user details by userid
  getUserById(id: any): Observable<any> {
    return this.http.get<any>(this.url + '/readUser' + '/' + id).pipe(
      // tap((user) => console.log(user)),
      catchError(this.handleError)
    );
  }

  //to get data of all the events of a particular participant
  getEventsByUserId(id: any): Observable<any> {
    return this.http
      .get<any>(this.url + '/readTrackingDetailsByParticipantName' + '/' + id)
      .pipe(
        // tap((user) => console.log(user)),
        catchError(this.handleError)
      );
  }

  createNewEvent(username: any, imageId: any, data: any): Observable<any> {
    // console.log('create new event');
    return this.http
      .post<any>(
        this.url + '/' + username + '/' + 'createEvent' + '/' + imageId,
        data
      )
      .pipe(
        // tap((data) => {
        //   console.log(data);
        // }),
        catchError(this.handleError)
      );
  }

  deleteEvent(organizerId: any, eventId: any): Observable<any> {
    return this.http
      .delete(
        this.url + '/' + organizerId + '/' + 'deleteEvent' + '/' + eventId
      )
      .pipe(catchError(this.handleError));
  }

  updateEvent(
    organizerId: any,
    eventId: any,
    imageId: any,
    data: any
  ): Observable<any> {
    console.log('update');
    return this.http
      .put(
        this.url +
          '/' +
          organizerId +
          '/' +
          'updateEvent' +
          '/' +
          eventId +
          '/' +
          imageId,
        data
      )
      .pipe(
        // tap((data) => console.log(data)),
        catchError(this.handleError)
      );
  }

  //add new tracking details
  registerUserInNewEvent(
    username: any,
    eventid: any,
    data: any
  ): Observable<any> {
    return this.http
      .post<any>(
        this.url +
          '/' +
          username +
          '/' +
          eventid +
          '/' +
          'addNewTrackingDetails',
        data
      )
      .pipe(
        // tap((user) => {
        //   console.log(user);
        // }),
        catchError(this.handleError)
      );
  }

  // to delete registration
  deleteEventRegistration(username: any, eventid: any) {
    return this.http
      .delete(
        this.url +
          '/' +
          username +
          '/' +
          eventid +
          '/' +
          'deleteTrackingDetails'
      )
      .pipe(
        // tap((data) => console.log(data)),
        catchError(this.handleError)
      );
  }

  //to update Tracking details

  updateTrackingDetails(username: any, eventid: any, data: any) {
    return this.http
      .put(
        this.url +
          '/' +
          username +
          '/' +
          eventid +
          '/' +
          'updateTrackingDetails',
        data
      )
      .pipe(
        // tap((data) => console.log(data)),
        catchError(this.handleError)
      );
  }

  //get tracking details by eventId
  readTrackingDetailsByEventId(eventid: any) {
    return this.http
      .get(this.url + '/' + 'readTrackingDetailsByEventId' + '/' + eventid)
      .pipe(
        // tap((user) => {
        //   console.log(user);
        // }),
        catchError(this.handleError)
      );
  }

  //get tracking details by ParticipantId and EventId
  readTrackingDetailsByParticipantNameAndEventId(username: any, eventid: any) {
    return this.http
      .get(
        this.url +
          '/' +
          'readTrackingDetailsByParticipantNameAndEventId' +
          '/' +
          username +
          '/' +
          eventid
      )
      .pipe(
        // tap((user) => {
        //   console.log(user);
        // }),
        catchError(this.handleError)
      );
  }

  //to check unique username
  checkUniqueUserName(id: any): Observable<any> {
    console.log(id);
    return this.http
      .get<any>(this.url + '/checkUniqueUserName' + '/' + id)
      .pipe(
        // tap((isUniqueuser) => {
        //   console.log(isUniqueuser);
        // }),
        catchError(this.handleError)
      );
  }

  //to update user Details
  updateUserDetails(username: any, data: any) {
    return this.http.put(this.url + '/updateUser/' + username, data).pipe(
      // tap((data) => console.log(data)),
      catchError(this.handleError)
    );
  }

  //to post image
  imageUploadAction(data: any): Observable<any> {
    return this.http
      .post<any>(this.url + '/upload/image/', data, {
        observe: 'response',
      })
      .pipe(
        // tap((user) => {
        //   console.log(user);
        // }),
        catchError(this.handleError)
      );
  }

  //to get image
  viewImage(imageId: any): Observable<any> {
    return this.http
      .get(this.url + '/get/image/info/' + imageId)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: any) {
    console.log(error);
    return throwError(error);
  }
}
