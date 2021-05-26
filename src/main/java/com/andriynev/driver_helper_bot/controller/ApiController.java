package com.andriynev.driver_helper_bot.controller;


import com.andriynev.driver_helper_bot.dto.JwtAuthRequest;
import com.andriynev.driver_helper_bot.dto.JwtTokenResponse;
import com.andriynev.driver_helper_bot.dto.User;
import com.andriynev.driver_helper_bot.security.AuthService;
import com.andriynev.driver_helper_bot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public ApiController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Authenticate moderator using Telegram login")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtTokenResponse.class)) }))
    @PostMapping("/telegram_auth")
    public ResponseEntity<JwtTokenResponse> auth(@RequestBody JwtAuthRequest authenticationRequest) {

        try {
            JwtTokenResponse response = authService.processCredentials(authenticationRequest);

            return ResponseEntity.ok()
                    .body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Get users list")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Users",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class))) }))
    @GetMapping("/users")
    public List<User> users() {
        return userService.getUsers();
    }
}
