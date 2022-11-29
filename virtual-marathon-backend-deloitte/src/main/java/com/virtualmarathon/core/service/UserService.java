package com.virtualmarathon.core.service;

import com.virtualmarathon.core.dao.RoleDao;
import com.virtualmarathon.core.dao.UserDao;
import com.virtualmarathon.core.entity.Role;
import com.virtualmarathon.core.entity.User;
import com.virtualmarathon.core.exception.user.UserNameExistsException;
import com.virtualmarathon.core.exception.user.UserNotFoundException;
import com.virtualmarathon.core.exception.user.WrongGmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user, String roleName) {
        Role role = roleDao.findById(roleName).get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        if(userDao.existsById(user.getUserName())) {
            throw new UserNameExistsException("Given username :" + user.getUserName() + " already exists");
        }
        if(!(user.getGmail().endsWith("@gmail.com") && user.getGmail().length() >= 11)) {
            throw new WrongGmailException("The given gmail id: " + user.getGmail() + " is wrong!");
        }
        return userDao.save(user);
    }

    public List<User> showAllUsers() {
        return userDao.findAll();
    }

    public User showUserById(String userName) {
        Optional<User> optionalUser = userDao.findById(userName);
        if(optionalUser.isPresent()) return optionalUser.get();
        else throw new UserNotFoundException("User not found for userName: " + userName);
    }

    public User changeUserDetails(String userName, User updatedUser) {
        if(userDao.findById(userName).isPresent()) {
            User existingUser = userDao.findById(userName).get();
            existingUser.setUserPassword(getEncodedPassword(updatedUser.getUserPassword()));
            existingUser.setUserFullName(updatedUser.getUserFullName());
            if(!(updatedUser.getGmail().endsWith("@gmail.com") && updatedUser.getGmail().length() >= 11)) {
                throw new WrongGmailException("The given gmail id: " + updatedUser.getGmail() + " is wrong!");
            }
            existingUser.setGmail(updatedUser.getGmail());
            return userDao.save(existingUser);
        } else {
            throw new UserNotFoundException("User not found for userName: " + userName);
        }
    }

    public void removeUser(String userName) {
        if(userDao.findById(userName).isPresent()) {
            userDao.deleteById(userName);
        } else {
            throw new UserNotFoundException("User not found for userName: " + userName);
        }
    }

    public void removeAllUsers() {
        userDao.deleteAll();
    }

    public Boolean checkUniqueUserName(String userName) {
        return !userDao.findById(userName).isPresent();
    }

    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setRoleName("organizer");
        adminRole.setRoleDescription("organizer role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("participant");
        userRole.setRoleDescription("participant role");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setUserFullName("Kapil Deshmukh");
        adminUser.setGmail("ruban1work@gmail.com");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

        User organizer = new User();
        organizer.setUserName("organizer123");
        organizer.setUserPassword(getEncodedPassword("organizer@pass"));
        organizer.setUserFullName("Raj Shah");
        organizer.setGmail("ruban1work@gmail.com");
        Set<Role> organizerRoles = new HashSet<>();
        organizerRoles.add(adminRole);
        organizer.setRole(organizerRoles);
        userDao.save(organizer);

        User participant = new User();
        participant.setUserName("ruban123");
        participant.setUserPassword(getEncodedPassword("ruban@pass"));
        participant.setUserFullName("Ruban Sahoo");
        participant.setGmail("ruban1work@gmail.com");
        Set<Role> participantRoles = new HashSet<>();
        participantRoles.add(userRole);
        participant.setRole(participantRoles);
        userDao.save(participant);

        User participant2 = new User();
        participant2.setUserName("bhavika123");
        participant2.setUserPassword(getEncodedPassword("bhavika@pass"));
        participant2.setUserFullName("Bhavika Jain");
        participant2.setGmail("ruban1work@gmail.com");
        Set<Role> participantRoles2 = new HashSet<>();
        participantRoles2.add(userRole);
        participant2.setRole(participantRoles2);
        userDao.save(participant2);

    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
