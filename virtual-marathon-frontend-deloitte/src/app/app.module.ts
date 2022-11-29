import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { AppRoutingModule, routingComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomePageComponent } from './home-page/home-page.component';
import { HeaderComponent } from './header/header.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FooterComponent } from './footer/footer.component';
import { EventsPageComponent } from './events-page/events-page.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ParticipantDetailsPageComponent } from './participant-details-page/participant-details-page.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { EventDetailsComponent } from './event-details/event-details.component';
import { CreatEventComponent } from './creat-event/creat-event.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from './services/user.service';
import { AuthInterceptor } from './auth/auth.interceptor';
import { NotfoundComponent } from './landing-page/notfound.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ActivityPageComponent } from './activity-page/activity-page.component';
import { CardComponent } from './card/card.component';
import { ChartsModule } from 'ng2-charts';
import { PieChartComponent } from './graph/pie-chart/pie-chart.component';
import { LineGraphComponent } from './graph/line-graph/line-graph.component';
import { BarGraphComponent } from './graph/bar-graph/bar-graph.component';
import { PieChartOrganizerComponent } from './graph/pie-chart-organizer/pie-chart-organizer.component';
import { BarGraphFeedbackOrganizerComponent } from './graph/bar-graph-feedback-organizer/bar-graph-feedback-organizer.component';

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    HeaderComponent,
    FooterComponent,
    routingComponents,
    EventsPageComponent,
    ParticipantDetailsPageComponent,
    LoginComponent,
    SignupComponent,
    EventDetailsComponent,
    CreatEventComponent,
    NotfoundComponent,
    ActivityPageComponent,
    CardComponent,
    PieChartComponent,
    LineGraphComponent,
    BarGraphComponent,
    PieChartOrganizerComponent,
    BarGraphFeedbackOrganizerComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    NgbModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    ChartsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
