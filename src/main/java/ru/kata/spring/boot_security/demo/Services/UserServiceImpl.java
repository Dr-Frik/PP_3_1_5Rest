package ru.kata.spring.boot_security.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void saveNewUser(UserDTO userDTO) {
        Set<Role> newRoleSet = new HashSet<>();
        for  (String id : userDTO.getRoleSet()) {
            newRoleSet.add(roleService.getById(Integer.parseInt(id)));
        }
        User newUser = new User(userDTO.getName(), userDTO.getLastName(), userDTO.getAddress());
        newUser.setPassword( passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRoleSet(newRoleSet);
        userRepository.save(newUser);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO, long id) {
        User userToUpdate = userRepository.getById(id);
        userToUpdate.setName(userDTO.getName());
        userToUpdate.setLastName(userDTO.getLastName());
        userToUpdate.setAddress(userDTO.getAddress());
        if (!userDTO.getPassword().equals("")) {
            userToUpdate.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (!userDTO.getRoleSet().isEmpty()) {
            Set<Role> newRoleSet = new HashSet<>();
            for  (String roleId : userDTO.getRoleSet()) {
                newRoleSet.add(roleService.getById(Integer.parseInt(roleId)));
            }
            userToUpdate.setRoleSet(newRoleSet);
        }
    }
}
