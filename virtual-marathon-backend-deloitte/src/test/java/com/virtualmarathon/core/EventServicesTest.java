package com.virtualmarathon.core;

import com.virtualmarathon.core.dao.EventDao;
import com.virtualmarathon.core.dao.ImageDao;
import com.virtualmarathon.core.dao.RoleDao;
import com.virtualmarathon.core.dao.UserDao;
import com.virtualmarathon.core.entity.*;
import com.virtualmarathon.core.service.EventService;
import com.virtualmarathon.core.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class EventServicesTest {

    @MockBean
    private EventDao eventDao;

    @MockBean
    private UserDao userDao;

    @MockBean
    private ImageDao imageDao;

    @MockBean
    private RoleDao roleDao;

    @InjectMocks
    private EventService eventService;

    @InjectMocks
    private UserService userService;

    @MockBean
    @Autowired
    private PasswordEncoder passwordEncoder;


    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Event event1 = new Event();
    Event event2=new Event();
    Event eventupdated=new Event();
    User user1 = new User();
    Role adminRole = new Role();
    List<Event> eventList;
    Image image = new Image(1L,"Voucher","jpg",new byte[100011]);


    @BeforeEach
    public void setUp(){
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

        event2.setId(2);
        event2.setTitle("AFLI NDM - LIVE (Charity)");
        event2.setImage(image);
        event2.setDescription("New Delhi Marathon endorses this noble association with CHILDLINE India Foundation.");
        event2.setStartDate(sdf.parse("20/03/2022", new ParsePosition(0)));
        event2.setEndDate(sdf.parse("31/03/2022", new ParsePosition(0)));
        event2.setDistance(15.0);
        event2.setType("cycling");
        event2.setStatus("open");
        event2.setFaq(faq);
        event2.setOrganizer(user1);

        eventList=new ArrayList<>();
        eventList.add(event1);


    }
    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @AfterEach
    public void tearDown(){
        event1=null;
        event2=null;
        eventList=null;
    }

     /**
      * Test to return all events.
      */
     @Test
    public void ShouldReturnListOfAllEvents() {
        // Setup our mock repository
        doReturn(Arrays.asList(event1, event2)).when(eventDao).findAllByOrderByStartDateDesc(Sort.by("endDate").ascending());

        // Execute the service call
        List<Event> events = eventService.showAllEvents(null,null);

        // Assert the response
        Assertions.assertEquals(2, events.size(), "findAll should return 2 events");
    }


    /**
     *  Test code for save a user.
     */
    @Test
    void givenEventToAddShouldReturnAddedEvent(){

        doReturn(Optional.of(user1)).when(userDao).findById("admin123");
        doReturn(Optional.of(image)).when(imageDao).findById(any());
        eventService.createNewEvent(user1.getUserName(),image.getId(),event1);
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventDao).save(eventArgumentCaptor.capture());
        Event capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent).isEqualTo(event1);
    }


    /**
     *  Test to find an event by id.
     */
    @Test
    void FindEventById() {
        // Setup our mock repository
        doReturn(Optional.of(event1)).when(eventDao).findById(1);

        // Execute the service call
        Optional<Event> events = Optional.ofNullable(eventService.showEventById(1));

        // Assert the response
        Assertions.assertTrue(events.isPresent(), "events was found");
        Assertions.assertSame(events.get(), event1, "The events returned was the same as the mock");
    }


    /**
     * Test to find an event by particular organizer.
     */
    @Test
    void FindEventByOrganizer() {
        doReturn(Optional.of(user1)).when(userDao).findById("admin123");
        eventService.showEventByOrganizer(event1.getOrganizer().getUserName());
        ArgumentCaptor<User> eventArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(eventDao).findByOrganizer(eventArgumentCaptor.capture());
        User capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent).isEqualTo(event1.getOrganizer());
    }


    /**
     * Test to delete aN event by given id.
     */
    @Test
    public void whenGivenId_shouldDeleteEvent_ifFound(){
        when(eventDao.findById(event1.getId())).thenReturn(Optional.of(event1));
        eventService.removeEvent(event1.getOrganizer().getUserName(),event1.getId());
        verify(eventDao).deleteById(event1.getId());
    }


    /**
     *  Test to update an event if found.
     */

    @Test
    public void whenGivenId_shouldUpdateEvent_ifFound() {
        doReturn(Optional.of(user1)).when(userDao).findById("admin123");
        doReturn(Optional.of(event1)).when(eventDao).findById(1);
        doReturn(Optional.of(image)).when(imageDao).findById(1L);
        Event events= new Event();
        events = event1; when(userDao.findById(any())).thenReturn(Optional.ofNullable(user1));
        eventService.changeEventDetails(user1.getUserName(),event1.getId(),image.getId(),events);
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventDao).save(eventArgumentCaptor.capture());
        Event capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent.getId()).isEqualTo(events.getId());
    }


    /**
     * Test code to remove all events.
     */
    @Test
    public void removeallEvents(){
        eventService.removeAllEvents();
        verify(eventDao).deleteAll();
    }
}
