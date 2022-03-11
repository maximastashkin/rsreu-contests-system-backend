package ru.rsreu.contests_system.user.resource;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.user.resource.models.signup.UserSignUpMapper;
import ru.rsreu.contests_system.user.resource.models.signup.UserSignUpRequest;
import ru.rsreu.contests_system.user.resource.models.signup.UserSignUpResponse;
import ru.rsreu.contests_system.user.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserResource {
    private final UserService userService;
    private final UserSignUpMapper userSignUpMapper;

    @Autowired
    public UserResource(UserService userService, UserSignUpMapper userSignUpMapper) {
        this.userService = userService;
        this.userSignUpMapper = userSignUpMapper;
    }

    @Operation(summary = "Create new user by sign upping")
    @PostMapping(path = "/signup", produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserSignUpResponse> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        return new ResponseEntity<>(
                userSignUpMapper.toResponse(
                        userService.save(userSignUpMapper.toUser(userSignUpRequest))),
                HttpStatus.CREATED);
    }
}
