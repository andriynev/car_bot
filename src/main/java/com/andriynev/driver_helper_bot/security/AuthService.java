package com.andriynev.driver_helper_bot.security;

import com.andriynev.driver_helper_bot.dto.JwtAuthRequest;
import com.andriynev.driver_helper_bot.dto.JwtTokenResponse;
import com.andriynev.driver_helper_bot.dto.Moderator;
import com.andriynev.driver_helper_bot.services.ModeratorService;
import com.andriynev.driver_helper_bot.telegram_bot.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final ModeratorService moderatorService;
    private final BotConfig botConfig;

    @Autowired
    public AuthService(ModeratorService moderatorService, JwtTokenUtil jwtTokenUtil, BotConfig botConfig) {
        this.moderatorService = moderatorService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.botConfig = botConfig;
    }

    public JwtTokenResponse processCredentials(JwtAuthRequest request) throws BadCredentialsException {
        if (!validateRequest(request)) {
            throw new BadCredentialsException("Given not Telegram message");
        }

        Optional<Moderator> userDetails = moderatorService.findModeratorByTelegramId(request.getId());
        if (userDetails.isPresent()) {
            return new JwtTokenResponse(generateToken(userDetails.get()));
        }

        Moderator moderator = new Moderator(
                request.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getUsername()
        );

        moderator = moderatorService.save(moderator);
        return new JwtTokenResponse(generateToken(moderator));
    }

    private String generateToken(UserDetails userDetails) {
        return jwtTokenUtil.generateToken(userDetails);
    }

    private boolean validateRequest(JwtAuthRequest request) {
        Date date = new Date();
        long diff = date.getTime() - (long)request.getAuthDate() * 1000;
        if (diff > 86400) {
            throw new BadCredentialsException("Date is outdated");
        }

        try {
            String data = request.generateCheckString();
            String generatedHash = hmacSha256(botConfig.getBotToken(), data);
            return generatedHash.equals(request.getHash());
        } catch (Exception e) {
            throw new BadCredentialsException("Cannot check hash");
        }
    }

    private String hmacSha256(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secret_key = new SecretKeySpec(encodedHash, "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return new String(Hex.encode(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8))));
    }
}
