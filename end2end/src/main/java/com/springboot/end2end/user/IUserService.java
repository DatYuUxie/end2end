package com.springboot.end2end.user;

import com.springboot.end2end.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUser();
    User registerUser(RegistrationRequest registrationRequest);
    User findByEmail(String email);
}
