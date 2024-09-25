package org.example.restassignment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestAssignmentController {
    private HashMap<Integer, RestAssignmentUser> usersHashMap = new HashMap<>();

    @GetMapping("/getUsers")
    public List<RestAssignmentUser> getUsers() {
        return new ArrayList<>(usersHashMap.values());
    }

    @GetMapping("/getUsers/{id}")
    public String getName(@PathVariable int id) {
        if (usersHashMap.containsKey(id)) {
            return usersHashMap.get(id).getName();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id);
        }
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody RestAssignmentUser user) {
        if (usersHashMap.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists with ID: " + user.getId());
        }
        usersHashMap.put(user.getId(), user);
        return "User has been created successfully";
    }

    @PostMapping("/createUsers")
    public String createUsers(@RequestBody List<RestAssignmentUser> users) {
        List<RestAssignmentUser> rejectedUsers = new ArrayList<>();
        for (RestAssignmentUser user : users) {
            if (usersHashMap.containsKey(user.getId())) {
                rejectedUsers.add(user);
            } else {
                usersHashMap.put(user.getId(), user);
            }
        }
        if (!rejectedUsers.isEmpty()) {
            StringBuilder rejected = new StringBuilder("The following users were not created due to duplicate IDs:\n");
            for (RestAssignmentUser rejectedUser : rejectedUsers) {
                rejected.append("ID: ").append(rejectedUser.getId()).append(", Name: ").append(rejectedUser.getName()).append("\n");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, rejected.toString());
        }
        return "All users were added successfully";
    }

    @PutMapping("/updateUser")
    public String updateUser(@RequestBody RestAssignmentUser user) {
        if (usersHashMap.containsKey(user.getId())) {
            usersHashMap.put(user.getId(), user);
            return "User has been updated successfully";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + user.getId());
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        if (usersHashMap.containsKey(id)) {
            usersHashMap.remove(id);
            return "User with ID " + id + " has been deleted successfully";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id);
    }
}
