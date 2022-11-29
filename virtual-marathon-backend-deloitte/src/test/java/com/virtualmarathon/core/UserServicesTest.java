package com.virtualmarathon.core;

import com.virtualmarathon.core.dao.RoleDao;
import com.virtualmarathon.core.dao.UserDao;
import com.virtualmarathon.core.entity.Role;
import com.virtualmarathon.core.entity.User;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UserServicesTest {

    @MockBean
    private UserDao userDao;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    @Autowired
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

//    private User user1;
    List<User> userList;
    User user1 = new User();
    User user = new User();
    User updateduser = new User();
    Role adminRole = new Role();
    User users = new User();
    @BeforeEach
    public void setUp() {
        adminRole.setRoleName("organizer");
        adminRole.setRoleDescription("organizer role");
        roleDao.save(adminRole);
        userList = new ArrayList<>();


        user1.setUserName("admin123");
        user1.setUserPassword(getEncodedPassword("admin@pass"));
        user1.setUserFullName("admin");
        user1.setGmail("admin@gmail.com");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        user1.setRole(adminRoles);
        userList.add(user1);

    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @AfterEach
    public void tearDown() {
        user1 = null;
        userList = null;
    }

    // test code for save a user
    @Test
    void givenUserToAddShouldReturnAddedUser() {
        doReturn(Optional.of(adminRole)).when(roleDao).findById("organizer");
        userService.registerNewUser(user1,adminRole.getRoleName());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user1);
    }

    // test to return all users
    @Test
    public void GivenGetAllUsersShouldReturnListOfAllUsers() {
        userDao.save(user1);
        //stubbing mock to return specific data
        when(userDao.findAll()).thenReturn(userList);
        List<User> userList1 = userService.showAllUsers();
        assertEquals(userList1, userList);
        verify(userDao, times(1)).save(user1);
        verify(userDao, times(1)).findAll();
    }

    // test to find user by id
    @Test
    public void givenIdThenShouldReturnUserOfThatId() {
        // Setup our mock repository
        doReturn(Optional.of(user1)).when(userDao).findById("admin123");

        // Execute the service call
        Optional<User> users = Optional.ofNullable(userService.showUserById("admin123"));

        // Assert the response
        Assertions.assertTrue(users.isPresent(), "events was found");
        Assertions.assertSame(users.get(), user1, "The events returned was the same as the mock");
    }

    // test to delete a user if found
    @Test
    public void whenGivenId_shouldDeleteUser_ifFound() {
        Role adminRole = new Role();
        adminRole.setRoleName("organizer");
        adminRole.setRoleDescription("organizer role");
        roleDao.save(adminRole);
        User user = new User();
        user.setUserName("organizer1");
        user.setUserPassword(getEncodedPassword("organizer@pass"));
        user.setUserFullName("Sandeep Bhau");
        user.setGmail("organizer1@gmail.com");
        Set<Role> organizerRoles = new HashSet<>();
        organizerRoles.add(adminRole);
        user.setRole(organizerRoles);

        when(userDao.findById(user.getUserName())).thenReturn(Optional.of(user));
        userService.removeUser(user.getUserName());
        verify(userDao).deleteById(user.getUserName());
    }

    // test to update a user of particular id
    @Test
    public void whenGivenId_shouldUpdateUser_ifFound() {

        doReturn(Optional.of(adminRole)).when(roleDao).findById("organizer");

        doReturn(Optional.of(user1)).when(userDao).findById("admin123");

        users=user1;

        when(roleDao.findById(any())).thenReturn(Optional.ofNullable(adminRole));

        userService.changeUserDetails(user1.getUserName(),users);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getUserName()).isEqualTo(users.getUserName());

    }

   // test to find  a unique username
    @Test
    public  void checkuniqueusername(){
        //mock the repository
        doReturn(Optional.of(user1)).when(userDao).findById("admin123");
        assertTrue(userDao.findById("admin123").isPresent());
    }

    /**
     * Test to remove all users.
     */
    @Test
    public void removeallusers(){
        userService.removeAllUsers();
       verify(userDao).deleteAll();
    }

}

