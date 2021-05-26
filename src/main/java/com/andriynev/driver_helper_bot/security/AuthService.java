package com.andriynev.driver_helper_bot.security;

import com.andriynev.driver_helper_bot.dao.ModeratorRepository;
import com.andriynev.driver_helper_bot.dto.JwtAuthRequest;
import com.andriynev.driver_helper_bot.dto.JwtTokenResponse;
import com.andriynev.driver_helper_bot.dto.Moderator;
import com.andriynev.driver_helper_bot.telegram_bot.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final ModeratorRepository moderatorRepository;
    private final BotConfig botConfig;

    @Autowired
    public AuthService(ModeratorRepository moderatorRepository, JwtTokenUtil jwtTokenUtil, BotConfig botConfig) {
        this.moderatorRepository = moderatorRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.botConfig = botConfig;
    }

    public JwtTokenResponse processCredentials(JwtAuthRequest request) throws BadCredentialsException {
        if (!validateRequest(request)) {
            throw new BadCredentialsException("Given not Telegram message");
        }

        Optional<Moderator> userDetails = moderatorRepository.findByUsername(request.getUsername());
        if (userDetails.isPresent()) {
            return new JwtTokenResponse(generateToken(userDetails.get()));
        }

        Moderator moderator = new Moderator(
                request.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getUsername()
        );

        moderator = moderatorRepository.save(moderator);
        return new JwtTokenResponse(generateToken(moderator));
    }

    private String generateToken(UserDetails userDetails) {
        return jwtTokenUtil.generateToken(userDetails);
    }

    private boolean validateRequest(JwtAuthRequest request) {
        return true;
    }
}
