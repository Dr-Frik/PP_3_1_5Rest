package ru.kata.spring.boot_security.demo.Services;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface UserService {

    void saveNewUser(User user, ArrayList<String> listRoleId);


    public List<User> getAllUsers();

    User getById(int id);

    void deleteUser(int id);

    void updateUser(int id, User user, ArrayList <String> roleSet );
}
