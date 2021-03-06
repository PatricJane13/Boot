package spring.controller;

import spring.model.Role;
import spring.model.User;
import spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class AllUsersController {
    @Autowired
    UserService userService;

    @GetMapping(value = "all")
    public String loginJsp(Model model) {
        model.addAttribute("messages", userService.listUsers());
        return "users";
    }

    @GetMapping(value = "getUpdate")
    public String getUpdate(@RequestParam("id") String id,
                            Model model) {
        User user = userService.getUserById(Long.parseLong(id));
        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping(value = "add")
    public String addUser(@RequestParam("firstName") String firstName,
                          @RequestParam("lastName") String lastName,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("role") String role) throws IOException, SQLException {
        List<Role> list = new ArrayList<>();
        list.add(new Role(role.toUpperCase()));
        userService.add(new User(firstName, lastName, email, password, list));
        return "redirect:/admin/all";
    }

    @PostMapping(value = "update")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("newFirstName") String newFirstName,
                             @RequestParam("newLastName") String newLastName,
                             @RequestParam("newEmail") String newEmail,
                             @RequestParam("newPassword") String password) throws IOException {
        User user = new User(id, newFirstName, newLastName, newEmail, password);
        userService.updateUser(user);
        return "redirect:/admin/all";
    }

    @GetMapping(value = "delete")
    public String deleteUser(@RequestParam("id") String id) throws IOException {
        User user = userService.getUserById(Long.parseLong(id));
        userService.deleteUser(user);
        return "redirect:/admin/all";
    }

    @PostMapping(value = "updateRole")
    public String updateRole(@RequestParam("selectRole") String role,
                             @RequestParam("id") Long id) throws IOException {
        userService.updateUserRole(role, id);
        return "redirect:/admin/all";
    }
}
