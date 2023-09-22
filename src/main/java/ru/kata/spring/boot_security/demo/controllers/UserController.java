package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Services.RoleService;
import ru.kata.spring.boot_security.demo.Services.UserService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roleSet", roleService.getAllRoles());
        return "/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") User user, @RequestParam ArrayList<String> roleSet) {
        Set<Role> userRole = new HashSet<>();
        for (String roleId : roleSet) {
            Role role = roleService.getById(Integer.parseInt(roleId));
            userRole.add(role);
        }
        user.setRoleSet(userRole);
        userService.saveNewUser(user, roleSet);
        return "redirect:/admin";
    }
    @PatchMapping("update/{id}")
    public String update(@ModelAttribute User user, @PathVariable int id, @RequestParam ArrayList<String> roleSet1) {
        userService.updateUser(id, user, roleSet1);
        return "redirect:/admin";
    }

    @GetMapping("/userinfo")
    public String userInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", authentication.getPrincipal());
        return "user_page";
    }
    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<User> userList = userService.getAllUsers();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", authentication.getPrincipal());
        model.addAttribute("userList", userList);
        model.addAttribute("roleSet1", roleService.getAllRoles());
        return "admin_new";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}
