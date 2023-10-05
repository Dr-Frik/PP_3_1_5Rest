package ru.kata.spring.boot_security.demo.Services;


import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public interface UserService {

    void saveNewUser(UserDTO userDTO);


    List<User> getAllUsers();

    User getById(long id);

    void deleteUser(long id);


    void updateUser(UserDTO userDTO, long id);
}
