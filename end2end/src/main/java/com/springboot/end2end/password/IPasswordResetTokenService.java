package com.springboot.end2end.password;

import com.springboot.end2end.user.User;

import java.util.Optional;

public interface IPasswordResetTokenService {
    String validatePasswordResetToken(String theToken);

    void createPasswordResetForUser(User user, String passwordResetToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User theUser, String password);
}
