import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  // private baseUrl = 'http://localhost:8080';
  private baseUrl = environment.url;

  constructor(private http: HttpClient) {}
  getByOrganizerRole(): Observable<any> {
    return this.http
      .get<any>(this.baseUrl + '/forOrganizer', {
        responseType: 'text' as 'json',
      })
      .pipe(
        map((resp) => {
          return resp;
          console.log(resp.status);
        })
      );
  }

  getByParticipantRole(): Observable<any> {
    return this.http
      .get<any>(this.baseUrl + '/forParticipant', {
        responseType: 'text' as 'json',
      })
      .pipe(
        map((resp) => {
          return resp;
        })
      );
  }
}
