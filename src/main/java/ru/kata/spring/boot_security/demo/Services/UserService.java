package ru.kata.spring.boot_security.demo.Services;


import ru.kata.spring.boot_security.demo.models.User;
import java.util.ArrayList;
import java.util.List;


public interface UserService {

    void saveNewUser(User user, ArrayList<String> listRoleId);


    public List<User> getAllUsers();

    User getById(int id);

    void deleteUser(int id);

    void updateUser(int id, User user, ArrayList <String> roleSet );
}
