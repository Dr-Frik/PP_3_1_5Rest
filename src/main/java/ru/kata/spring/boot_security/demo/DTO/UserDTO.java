package ru.kata.spring.boot_security.demo.DTO;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class UserDTO {
    @NotEmpty(message = "name cannot be empty")
    private String name;
    @NotEmpty(message = "Last Name cannot be empty")
    private String lastName;
    private String address;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotEmpty(message = "Password cannot be empty")
    private Set<String> roleSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<String> roleSet) {
        this.roleSet = roleSet;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", roleSet=" + roleSet +
                '}';
    }
}
