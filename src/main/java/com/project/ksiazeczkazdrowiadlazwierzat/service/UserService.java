package com.project.ksiazeczkazdrowiadlazwierzat.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.Registration;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.UserPersonalData;
import com.project.ksiazeczkazdrowiadlazwierzat.controller.dto.UserShortData;
import com.project.ksiazeczkazdrowiadlazwierzat.data.collection.User;
import com.project.ksiazeczkazdrowiadlazwierzat.data.repository.UserRepository;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.MissingNameException;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.PasswordMismatchException;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.UserExistException;
import com.project.ksiazeczkazdrowiadlazwierzat.service.exception.VetCodeException;

import static com.project.ksiazeczkazdrowiadlazwierzat.service.common.Commons.TIMEOUT;

@Service
public class UserService {
    public static final String VET = "ROLE_VET";
    public static final String USER = "ROLE_USER";

    @Value("${vetCode}")
    private String vetCode;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(Registration registration) {
        if (!registration.getPassword().equals(registration.getRepeatedPassword())) {
            throw new PasswordMismatchException();
        }

        if (StringUtils.isAnyBlank(registration.getName(), registration.getSurname())) {
            throw new MissingNameException();
        }

        Optional<User> user = userRepository.findByUsername(registration.getUsername()).blockOptional(TIMEOUT);

        if (user.isPresent()) {
            throw new UserExistException();
        }

        String role;
        if (StringUtils.isNotBlank(registration.getVetCode())) {
            role = VET;
            if (!vetCode.equals(registration.getVetCode())) {
                throw new VetCodeException();
            }
        } else {
            role = USER;
        }

        User newUser = new User()
                .setUsername(registration.getUsername())
                .setPassword(passwordEncoder.encode(registration.getPassword()))
                .setName(registration.getName())
                .setSurname(registration.getSurname())
                .setRole(role);
        userRepository.save(newUser).block(TIMEOUT);
    }

    public UserPersonalData getUserData(String username) {
        if (StringUtils.isBlank(username)) {
            return new UserPersonalData();
        }
        return userRepository.findByUsername(username)
                .map(this::convert)
                .blockOptional(TIMEOUT)
                .orElse(new UserPersonalData());
    }

    public List<UserShortData> getAllUsers() {
        return userRepository.findAll()
                .map(this::convertToShort)
                .collectList()
                .block(TIMEOUT);
    }

    public List<UserShortData> getAllVets() {
        return userRepository.findAllByRole(VET)
                .map(this::convertToShort)
                .collectList()
                .block(TIMEOUT);
    }

    public String updateUserData(String username, UserPersonalData userPersonalData) {
        return userRepository.findByUsername(username)
                .blockOptional(TIMEOUT)
                .flatMap(user -> {
                    copyToUser(userPersonalData, user);
                    return userRepository.save(user).blockOptional(TIMEOUT);
                })
                .map(User::getId)
                .orElse(null);
    }

    public UserShortData getShortData(String username) {
        return userRepository.findByUsername(username)
                .blockOptional(TIMEOUT)
                .map(this::convertToShort)
                .orElse(new UserShortData());
    }

    public UserShortData convertToShort(User user) {
        return new UserShortData()
                .setUsername(user.getUsername())
                .setName(user.getName() + " " + user.getSurname());
    }

    private UserPersonalData convert(User user) {
        return new UserPersonalData()
                .setName(user.getName())
                .setSurname(user.getSurname())
                .setEmail(user.getEmail())
                .setPhoneNumber(user.getPhoneNumber())
                .setStreet(user.getStreet())
                .setCity(user.getCity())
                .setPostcode(user.getPostcode());
    }

    private void copyToUser(UserPersonalData userPersonalData, User user) {
        user.setName(StringUtils.isNotBlank(userPersonalData.getName()) ? userPersonalData.getName() : user.getName())
                .setSurname(StringUtils.isNotBlank(userPersonalData.getSurname()) ? userPersonalData.getSurname() : user.getSurname())
                .setEmail(userPersonalData.getEmail())
                .setPhoneNumber(userPersonalData.getPhoneNumber())
                .setStreet(userPersonalData.getStreet())
                .setCity(userPersonalData.getCity())
                .setPostcode(userPersonalData.getPostcode());
    }
}
