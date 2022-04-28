package com.project.ksiazeczkazdrowiadlazwierzat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.Registration;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.UserPersonalData;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.UserShortData;
import com.project.ksiazeczkazdrowiadlazwierzat.service.UserService;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.MissingNameException;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.PasswordMismatchException;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.UserExistException;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.VetCodeException;

import static com.project.ksiazeczkazdrowiadlazwierzat.controller.util.UserUtils.getUsername;
import static com.project.ksiazeczkazdrowiadlazwierzat.controller.util.UserUtils.isLoggedAsVet;
import static com.project.ksiazeczkazdrowiadlazwierzat.service.UserService.USER;
import static com.project.ksiazeczkazdrowiadlazwierzat.service.UserService.VET;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login() {
        if (isLoggedAsVet()) {
            return VET;
        } else {
            return USER;
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody Registration registration) {
        String status;
        try {
            userService.register(registration);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (PasswordMismatchException ex) {
            status = "NOT_SAME";
        } catch (UserExistException ex) {
            status = "USER_EXISTS";
        } catch (VetCodeException ex) {
            status = "VET_CODE";
        } catch (MissingNameException ex) {
            status = "MISSING_NAME";
        }
        return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users")
    public List<UserShortData> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/vet")
    public List<UserShortData> getVets() {
        return userService.getAllVets();
    }

    @GetMapping("/user")
    public UserPersonalData getUser() {
        return userService.getUserData(getUsername());
    }

    @PutMapping("/user")
    public String updateUser(@RequestBody UserPersonalData userPersonalData) {
        return userService.updateUserData(getUsername(), userPersonalData);
    }
}
