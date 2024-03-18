package com.springboot.end2end.registration.token;

import com.springboot.end2end.user.User;
import com.springboot.end2end.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Calendar;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VerificationTokenService implements IVerificationTokenService {
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    @Override
    public String validateToken(String token) {
        Optional<VerificationToken> theToken =tokenRepository.findByToken(token);
        if(theToken.isEmpty()){
            return "Invalid";
        }
        Calendar calendar = Calendar.getInstance();
        if(theToken.get().getExpirationTime().getTime() -
                calendar.getTime().getTime() <= 0){
            return "expired";
        }
        User user = theToken.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        var verificationToken = new VerificationToken(token,user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteUserToken(Long id) {
        tokenRepository.deleteByUserId(id);
    }
}
