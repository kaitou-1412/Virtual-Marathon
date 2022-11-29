package com.virtualmarathon.core.controller;

import com.virtualmarathon.core.entity.User;
import com.virtualmarathon.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@CrossOrigin("${cors.urls}")
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRoleAndUser() { userService.initRoleAndUser(); }

    @PostMapping({"/registerNewOrganizer"})
    public User registerNewOrganizer(@RequestBody User user) {
        return userService.registerNewUser(user, "organizer");
    }

    @PostMapping({"/registerNewParticipant"})
    public User registerNewParticipant(@RequestBody User user) {
        return userService.registerNewUser(user, "participant");
    }

    @GetMapping("/readUsers")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public List<User> readAllUser() {
        return userService.showAllUsers();
    }

    @GetMapping("/readUser/{userName}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public User readUser(@PathVariable("userName") String userName) {
        return userService.showUserById(userName);
    }

    @PutMapping("/updateUser/{userName}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public User updateUser(@PathVariable("userName") String userName, @RequestBody User user) {
        return userService.changeUserDetails(userName, user);
    }

    @DeleteMapping("/deleteUser/{userName}")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public void deleteUser(@PathVariable("userName") String userName) {
        userService.removeUser(userName);
    }

    @DeleteMapping("/deleteAllUsers")
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public void deleteAllUsers() { userService.removeAllUsers(); }

    @GetMapping("/checkUniqueUserName/{userName}")
    public Boolean checkUniqueUserName(@PathVariable("userName") String userName) {
        return userService.checkUniqueUserName(userName);
    }

    @GetMapping({"/forOrganizer"})
    @PreAuthorize("hasRole('organizer')")
    public String forAdmin() {
        return "This URL is only accessible to the organizer";
    }

    @GetMapping({"/forParticipant"})
    @PreAuthorize("hasRole('participant')")
    public String forUser() {
        return "This URL is only accessible to the participant";
    }

}
