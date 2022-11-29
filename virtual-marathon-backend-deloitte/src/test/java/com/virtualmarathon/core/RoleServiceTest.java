package com.virtualmarathon.core;

import com.virtualmarathon.core.dao.EventDao;
import com.virtualmarathon.core.dao.RoleDao;
import com.virtualmarathon.core.entity.Role;
import com.virtualmarathon.core.service.RoleService;
import com.virtualmarathon.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class RoleServiceTest {

    @MockBean
    private RoleDao roleDao;

    @InjectMocks
    private RoleService roleService;

    /**
     * Test to create a new role.
     */
    @Test
    void createNewRole() {
        Role role =new Role();
        role.setRoleName("organizer");
        role.setRoleDescription("organizer role");

        when(roleDao.save(any(Role.class))).thenReturn(role);

        Role savedRole = roleDao.save(role);
        assertThat(savedRole.getRoleName()).isNotNull();
    }

    }
