package ru.kata.spring.boot_security.demo.Services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    Role getById(int id);
    List<Role> getAllRoles();

}

