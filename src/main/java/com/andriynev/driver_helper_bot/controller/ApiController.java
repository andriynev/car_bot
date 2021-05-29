package com.andriynev.driver_helper_bot.controller;


import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.security.AuthService;
import com.andriynev.driver_helper_bot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
    public JwtTokenResponse auth(@RequestBody JwtAuthRequest authenticationRequest) {
        return authService.processCredentials(authenticationRequest);
    }

    @Operation(summary = "Get users list", security = @SecurityRequirement(name = "jwtAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Users",
                    content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class))) }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            )}
    )
    @PreAuthorize("hasAuthority('users:read')")
    @GetMapping("/users")
    public List<User> users() {
        return userService.getUsers();
    }

    @Operation(summary = "Update user", security = @SecurityRequirement(name = "jwtAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)) }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            )}
    )
    @PreAuthorize("hasAuthority('users:write')")
    @PatchMapping("/users/{id}")
    public User partialUpdate(@RequestBody UserUpdateRequest partialUpdate, @PathVariable("id") String id) {
        return userService.partialUpdate(id, partialUpdate);
    }
}
