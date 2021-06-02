package com.andriynev.driver_helper_bot.controller;


import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.security.AuthService;
import com.andriynev.driver_helper_bot.services.MessageService;
import com.andriynev.driver_helper_bot.services.ModeratorService;
import com.andriynev.driver_helper_bot.services.TutorialService;
import com.andriynev.driver_helper_bot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final AuthService authService;
    private final UserService userService;
    private final ModeratorService moderatorService;
    private final MessageService messageService;
    private final TutorialService tutorialService;

    @Autowired
    public ApiController(AuthService authService, UserService userService, ModeratorService moderatorService, MessageService messageService, TutorialService tutorialService) {
        this.authService = authService;
        this.userService = userService;
        this.moderatorService = moderatorService;
        this.messageService = messageService;
        this.tutorialService = tutorialService;
    }

    @Operation(summary = "Authenticate moderator using Telegram login", tags = "auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtTokenResponse.class)) }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)) }
            )
    })
    @PostMapping("/telegram_auth")
    public JwtTokenResponse auth(@RequestBody JwtAuthRequest authenticationRequest) {
        return authService.processCredentials(authenticationRequest);
    }

    @Operation(summary = "Get users list", security = @SecurityRequirement(name = "jwtAuth"), tags = "users")
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

    @Operation(summary = "Update user", security = @SecurityRequirement(name = "jwtAuth"), tags = "users")
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
    public User partialUserUpdate(@RequestBody UserUpdateRequest partialUpdate, @PathVariable("id") String id) {
        return userService.partialUpdate(id, partialUpdate);
    }

    @Operation(summary = "Get moderators list", security = @SecurityRequirement(name = "jwtAuth"), tags = "moderators")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Moderators",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Moderator.class))) }
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
    @PreAuthorize("hasAuthority('moderators:read')")
    @GetMapping("/moderators")
    public List<Moderator> moderators() {
        return moderatorService.getModerators();
    }

    @Operation(summary = "Get moderator", security = @SecurityRequirement(name = "jwtAuth"), tags = "moderators")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Moderator",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Moderator.class)) }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            )}
    )
    @PreAuthorize("hasAnyAuthority('moderators:read', 'moderators:read_all')")
    @GetMapping("/moderators/{id}")
    public Moderator getById(@PathVariable("id") String id) {

        Optional<Moderator> moderator = moderatorService.findModeratorById(id);
        if (!moderator.isPresent()) {
            throw new ResourceNotFoundException("Moderator not found with ID: " + id);
        }

        return moderator.get();
    }

    @Operation(summary = "Update moderator", security = @SecurityRequirement(name = "jwtAuth"), tags = "moderators")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Moderator",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Moderator.class)) }
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
    @PreAuthorize("hasAuthority('moderators:write_all') or (hasAuthority('moderators:write') and !#id.equals(authentication.name))")
    @PatchMapping("/moderators/{id}")
    public Moderator partialModeratorUpdate(@RequestBody ModeratorUpdateRequest partialUpdate, @PathVariable("id") String id) {
        return moderatorService.partialUpdate(id, partialUpdate);
    }

    @Operation(summary = "Delete moderator", security = @SecurityRequirement(name = "jwtAuth"), tags = "moderators")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Moderator",
                    content = { @Content(mediaType = "application/json") }
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
    @PreAuthorize("hasAuthority('moderators:delete') and !#id.equals(authentication.name)")
    @DeleteMapping("/moderators/{id}")
    public void deleteModerator(@PathVariable("id") String id) {
        moderatorService.delete(id);
    }

    @Operation(summary = "Send message", security = @SecurityRequirement(name = "jwtAuth"), tags = "messages")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json") }
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
    @PreAuthorize("hasAuthority('messages:write')")
    @PostMapping("/send_message")
    public void sendMessage(@RequestBody SendMessageRequest request) {
        messageService.sendMessage(request);
    }

    @Operation(summary = "Get tutorials list", security = @SecurityRequirement(name = "jwtAuth"), tags = "tutorials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Moderators",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Tutorial.class))) }
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
    @PreAuthorize("hasAuthority('tutorials:read')")
    @GetMapping("/tutorials")
    public List<Tutorial> tutorials() {
        return tutorialService.getTutorials();
    }

    @Operation(summary = "Get tutorial", security = @SecurityRequirement(name = "jwtAuth"), tags = "tutorials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Tutorial",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tutorial.class)) }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "403", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            ),
            @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)) }
            )}
    )
    @PreAuthorize("hasAuthority('tutorials:read')")
    @GetMapping("/tutorials/{id}")
    public Tutorial getTutorialById(@PathVariable("id") String id) {

        Optional<Tutorial> tutorial = tutorialService.findTutorialById(id);
        if (!tutorial.isPresent()) {
            throw new ResourceNotFoundException("Tutorial not found with ID: " + id);
        }

        return tutorial.get();
    }

    @Operation(summary = "Update tutorial", security = @SecurityRequirement(name = "jwtAuth"), tags = "tutorials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Tutorial",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tutorial.class)) }
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
    @PreAuthorize("hasAuthority('tutorials:write')")
    @PatchMapping("/tutorials/{id}")
    public Tutorial tutorialUpdate(@RequestBody TutorialRequest partialUpdate, @PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return tutorialService.update(authentication.getName(), id, partialUpdate);
    }

    @Operation(summary = "Create tutorial", security = @SecurityRequirement(name = "jwtAuth"), tags = "tutorials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Tutorial",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tutorial.class)) }
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
    @PreAuthorize("hasAuthority('tutorials:write')")
    @PostMapping("/tutorials")
    public Tutorial tutorialCreate(@RequestBody TutorialRequest partialUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return tutorialService.create(authentication.getName(), partialUpdate);
    }

    @Operation(summary = "Delete tutorial", security = @SecurityRequirement(name = "jwtAuth"), tags = "tutorials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json") }
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
    @PreAuthorize("hasAuthority('tutorials:delete')")
    @DeleteMapping("/tutorials/{id}")
    public void deleteTutorial(@PathVariable("id") String id) {
        tutorialService.delete(id);
    }
}
