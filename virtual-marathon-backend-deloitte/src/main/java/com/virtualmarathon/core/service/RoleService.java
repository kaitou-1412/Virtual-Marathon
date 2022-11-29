package com.virtualmarathon.core.service;

import com.virtualmarathon.core.dao.RoleDao;
import com.virtualmarathon.core.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }

}
