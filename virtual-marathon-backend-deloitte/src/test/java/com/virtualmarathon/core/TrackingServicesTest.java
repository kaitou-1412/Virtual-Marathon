package com.virtualmarathon.core;

import com.sun.mail.util.MailConnectException;
import com.virtualmarathon.core.dao.*;
import com.virtualmarathon.core.entity.*;
import com.virtualmarathon.core.service.EmailSenderServices;
import com.virtualmarathon.core.service.EventService;
import com.virtualmarathon.core.service.TrackingDetailsService;
import com.virtualmarathon.core.service.UserService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.mail.MessagingException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class TrackingServicesTest {

    @MockBean
    private EventDao eventDao;

    @MockBean
    private UserDao userDao;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private ImageDao imageDao;

    @InjectMocks
    private EventService eventService;

    @InjectMocks
    private UserService userService;

    @MockBean
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TrackingDetailsDao trackingDetailsDao;

    @InjectMocks
    private TrackingDetailsService trackingDetailsService;

    @InjectMocks
    private EmailSenderServices senderServices;

    private TrackingDetails trackingDetails2;
    List<TrackingDetails> trackingDetailsList;


    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    TrackingDetails trackingDetails1 = new TrackingDetails();
    TrackingDetails trackingDetailsupdated = new TrackingDetails();
    Event event1 = new Event();
    User user1 = new User();
    User participant2 = new User();
    Role adminRole = new Role();
    Role userRole = new Role();
    Image image = new Image(1L,"Voucher","jpg",new byte[100011]);

    @BeforeEach
    public void setUp() {
        adminRole.setRoleName("organizer");
        adminRole.setRoleDescription("organizer role");
        roleDao.save(adminRole);


        user1.setUserName("admin123");
        user1.setUserPassword(getEncodedPassword("admin@pass"));
        user1.setUserFullName("admin");
        user1.setGmail("admin@gmail.com");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        user1.setRole(adminRoles);

        List<StringPair> faq = new ArrayList<>();
        StringPair sp1 = new StringPair();
        sp1.setQuestion("What is the registration fees?");
        sp1.setAnswer("Registration is free.");
        faq.add(sp1);

        event1.setId(1);
        event1.setTitle("World wide Marathon Virtual Training Run 1");
        event1.setImage(image);
        event1.setDescription("This is the World wide Marathon Virtual Training Run 1 to be held Virtually.");
        event1.setStartDate(sdf.parse("23/03/2022", new ParsePosition(0)));
        event1.setEndDate(sdf.parse("25/03/2022", new ParsePosition(0)));
        event1.setDistance(21.0);
        event1.setType("running");
        event1.setStatus("upcoming");
        event1.setFaq(faq);
        event1.setOrganizer(user1);

        userRole.setRoleName("participant");
        userRole.setRoleDescription("participant role");
        roleDao.save(userRole);


        participant2.setUserName("munni123");
        participant2.setUserPassword(getEncodedPassword("munni@pass"));
        participant2.setUserFullName("munni");
        participant2.setGmail("munni@gmail.com");
        Set<Role> participantRoles2 = new HashSet<>();
        participantRoles2.add(userRole);
        participant2.setRole(participantRoles2);
        userDao.save(participant2);


        trackingDetailsList = new ArrayList<>();
        trackingDetails1.setDistance(15.0);
        trackingDetails1.setSpeed(20.0);
        trackingDetails1.setTime(20.0);
        trackingDetails1.setHasCompletedEvent(false);
        trackingDetails1.setEvent(event1);
        trackingDetails1.setParticipant(participant2);
        trackingDetailsList.add(trackingDetails1);


    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @AfterEach
    public void tearDown() {
        trackingDetails1 = null;
        trackingDetailsList = null;

    }

    /**
     * Test to show added new tracking details
     */
    @Test
    void givenTrackingDetailsToAddShouldReturnAddedTrackingDetails() {
        //stubbing
        when(trackingDetailsDao.save(any())).thenReturn(trackingDetails1);
        trackingDetailsDao.save(trackingDetails1);
        verify(trackingDetailsDao, times(1)).save(any());
    }



    /**
     * Test to get all tracking details
     */
    @Test
    public void GivenGetAllTrackingDetailsShouldReturnListOfAllTrackingDetails() {
        trackingDetailsDao.save(trackingDetails1);
        //stubbing mock to return specific data
        when(trackingDetailsDao.findAll()).thenReturn(trackingDetailsList);
        List<TrackingDetails> trackingDetailsList1 = trackingDetailsService.showAllTrackingDetails();
        assertEquals(trackingDetailsList1, trackingDetailsList);
        verify(trackingDetailsDao, times(1)).save(trackingDetails1);
        verify(trackingDetailsDao, times(1)).findAll();
    }


    /**
     *   Test to show tracking details of particular id.
     */
    @Test
    public void givenIdThenShouldReturnTrackingDetailsOfThatId() {
        // Setup our mock repository
        doReturn(Optional.of(trackingDetails1)).when(trackingDetailsDao).findById(1);

        // Execute the service call
        Optional<TrackingDetails> trackingDetails = Optional.ofNullable(trackingDetailsService.showTrackingDetailsById(1));

        // Assert the response
        Assertions.assertTrue(trackingDetails.isPresent(), "events was found");
        Assertions.assertSame(trackingDetails.get(), trackingDetails1, "The events returned was the same as the mock");

    }

    /**
     * Test to return tracking details by participant name.
     */
    @Test
    public void showTrackingDetailsByParticipantName(){
        doReturn(Optional.of(participant2)).when(userDao).findById("munni123");
        trackingDetailsService.showTrackingDetailsByParticipantName(participant2.getUserName());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(trackingDetailsDao).findByParticipant(userArgumentCaptor.capture());
        User capturedEvent = userArgumentCaptor.getValue();
        AssertionsForClassTypes.assertThat(capturedEvent).isEqualTo(participant2);
    }

    /**
     *  Test to return tracking details by participant name and eventid.
     */
    @Test
    public  void showTrackingDetailsByParticipantNameAndEventId(){
        doReturn(Optional.of(participant2)).when(userDao).findById("munni123");
        doReturn(Optional.of(event1)).when(eventDao).findById(1);
        trackingDetailsService.showTrackingDetailsByParticipantNameAndEventId(participant2.getUserName(),event1.getId());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Event> eventArgumentCaptor= ArgumentCaptor.forClass(Event.class);
//        verify(trackingDetailsDao).findByParticipant(userArgumentCaptor.capture());
        verify(trackingDetailsDao).findByParticipantAndEvent(userArgumentCaptor.capture(),eventArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        Event capturedEvent = eventArgumentCaptor.getValue();
        AssertionsForClassTypes.assertThat(capturedEvent).isEqualTo(event1);
        AssertionsForClassTypes.assertThat(capturedUser).isEqualTo(participant2);
    }

    /**
     *  Test to return tracking details of any particular event.
     */
    @Test
    public void givenEventIdThenShouldReturnTrackingDetailsOfThatEventId() {

          doReturn(Optional.of(event1)).when(eventDao).findById(1);
          Optional<Event> optionalEvent = eventDao.findById(event1.getId());
          doReturn(Optional.of(trackingDetails1)).when(trackingDetailsDao).findById(1);
          Optional<List<TrackingDetails>> trackingDetails = Optional.ofNullable(trackingDetailsService.showTrackingDetailsByEventId(optionalEvent.get().getId()));
          Assertions.assertTrue(trackingDetails.isPresent());
    }

    /**
     *
     * Test to update tracking details of particular  id
     */
    @Test
    public void whenGivenId_shouldUpdateTrackingDetails_ifFound() throws Exception {
        doReturn(Optional.of(trackingDetails1)).when(trackingDetailsDao).findById(1);
        trackingDetailsupdated  = trackingDetailsService.showTrackingDetailsById(1);
        trackingDetailsupdated.setSpeed(20.0);
        trackingDetailsupdated.setDistance(15.0);
        trackingDetailsupdated.setSpeed(20.0);
        doReturn(Optional.of(trackingDetailsupdated)).when(trackingDetailsDao).findById(1);
        assertEquals(trackingDetailsupdated.getDistance(), 15.0);

    }

    /**
     * Test to delete tracking details of particular participant in any event.
     */
    @Test
    public void whenGivenId_shouldDeleteTracking_ifFound(){
        doReturn(Optional.of(participant2)).when(userDao).findById("munni123");
        doReturn(Optional.of(event1)).when(eventDao).findById(1);
        trackingDetailsService.removeTrackingDetails(participant2.getUserName(),event1.getId());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Event> eventArgumentCaptor= ArgumentCaptor.forClass(Event.class);
        verify(trackingDetailsDao).deleteByParticipantAndEvent(userArgumentCaptor.capture(),eventArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        Event capturedEvent = eventArgumentCaptor.getValue();
        AssertionsForClassTypes.assertThat(capturedEvent).isEqualTo(event1);
        AssertionsForClassTypes.assertThat(capturedUser).isEqualTo(participant2);
    }

    /**
     * Test to remove all tracking details.
     */
    @Test
    public void removealltrackingdetails(){
        trackingDetailsService.removeAllTrackingDetails();
        verify(trackingDetailsDao).deleteAll();
    }

//    @Test
//    public void sendMailAfterRegister(){
//        doReturn(Optional.of(participant2)).when(userDao).findById("munni123");
//        doReturn(Optional.of(event1)).when(eventDao).findById(1);
//        senderServices.sendEmail(participant2.getGmail(),"hello","hi");
//        trackingDetailsService.sendMailAfterRegistration(participant2.getUserName(),event1.getId());
//        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
//        ArgumentCaptor<Event> eventArgumentCaptor= ArgumentCaptor.forClass(Event.class);
//        verify(trackingDetailsDao).findByParticipantAndEvent(userArgumentCaptor.capture(),eventArgumentCaptor.capture());
//        User capturedUser = userArgumentCaptor.getValue();
//        Event capturedEvent = eventArgumentCaptor.getValue();
//        AssertionsForClassTypes.assertThat(capturedEvent).isEqualTo(event1);
//        AssertionsForClassTypes.assertThat(capturedUser).isEqualTo(participant2);
//    }
}

