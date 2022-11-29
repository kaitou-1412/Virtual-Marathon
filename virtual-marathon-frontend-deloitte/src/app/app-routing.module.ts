import { HomePageComponent } from './home-page/home-page.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EventsPageComponent } from './events-page/events-page.component';
import { ParticipantDetailsPageComponent } from './participant-details-page/participant-details-page.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { CreatEventComponent } from './creat-event/creat-event.component';
import { EventDetailsComponent } from './event-details/event-details.component';
import { NotfoundComponent } from './landing-page/notfound.component';

import { ActivityPageComponent } from './activity-page/activity-page.component';
import { PieChartComponent } from './graph/pie-chart/pie-chart.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  {
    path: 'createEvent',

    component: CreatEventComponent,
  },
  { path: 'home', component: HomePageComponent },
  { path: 'activity/:id', component: ActivityPageComponent },
  { path: 'events', component: EventsPageComponent },
  {
    path: 'user',

    component: ParticipantDetailsPageComponent,
  },
  // { path: 'eventDetails', component: EventDetailsComponent },
  { path: 'events/:id', component: EventDetailsComponent },
  { path: 'user/:id', component: ParticipantDetailsPageComponent },

  { path: 'welcome', component: NotfoundComponent },
  { path: '**', redirectTo: 'welcome' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

export const routingComponents = [HomePageComponent, EventsPageComponent];
