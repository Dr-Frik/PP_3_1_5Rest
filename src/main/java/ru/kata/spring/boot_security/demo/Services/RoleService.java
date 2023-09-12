package ru.kata.spring.boot_security.demo.Services;

import ru.kata.spring.boot_security.demo.models.Role;

public interface RoleService {
    Role getById(int id);
}

