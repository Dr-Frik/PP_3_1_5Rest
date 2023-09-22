package ru.kata.spring.boot_security.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }
    @Transactional
    @Override
    public void saveNewUser(User user, ArrayList<String> listRoleId) {
        Set<Role> userRole = new HashSet<>();
        for (String roleId : listRoleId) {
            Role role = roleService.getById(Integer.parseInt(roleId));
            userRole.add(role);
        }
        user.setRoleSet(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(int id, User user, ArrayList<String> roleSet) {
        User userToUpdate = userRepository.getById(id);
        userToUpdate.setName(user.getName());
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setLastName(user.getLastName());
        if (!user.getPassword().equals("")) {
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        Set<Role> userRole = new HashSet<>();
        for (String roleId : roleSet) {
            Role role = roleService.getById(Integer.parseInt(roleId));
            userRole.add(role);
        }
        userToUpdate.setRoleSet(userRole);
    }
}
