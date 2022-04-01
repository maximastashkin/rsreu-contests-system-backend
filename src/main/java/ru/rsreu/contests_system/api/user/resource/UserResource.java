package ru.rsreu.contests_system.api.user.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.api.user.resource.dto.check_mail.CheckMailMapper;
import ru.rsreu.contests_system.api.user.resource.dto.check_mail.CheckMailResponse;
import ru.rsreu.contests_system.api.user.resource.dto.signup.UserSignUpMapper;
import ru.rsreu.contests_system.api.user.resource.dto.signup.UserSignUpRequest;
import ru.rsreu.contests_system.api.user.resource.dto.users_info.UserInfoMapper;
import ru.rsreu.contests_system.api.user.resource.dto.users_info.UserInfoResponse;
import ru.rsreu.contests_system.api.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserResource {
    private final UserService userService;
    private final UserSignUpMapper userSignUpMapper;
    private final CheckMailMapper checkMailMapper;
    private final UserInfoMapper userInfoMapper;

    @Operation(summary = "${api.users.signup.operation}")
    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "${api.users.signup.response-codes.created}")
    )
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpRequest userSignUpRequest) {
        userService.save(userSignUpMapper.toUser(userSignUpRequest));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "${api.users.check-email.operation}")
    @GetMapping(path = "/check-mail", produces = "application/json")
    public ResponseEntity<CheckMailResponse> checkEmailUnique(@RequestParam @NotBlank @Email String email) {
        return new ResponseEntity<>(checkMailMapper.toResponse(userService.isEmailUnique(email)), HttpStatus.OK);
    }

    @Operation(summary = "${api.users.all.operation}")
    @GetMapping(path = "/{pageSize}/{pageNumber}", produces = "application/json")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers(@PathVariable @Min(1) int pageSize,
                                                              @Parameter(description = "${api.pageable_numbering.message}")
                                                               @PathVariable @Min(0) int pageNumber) {
        return new ResponseEntity<>(
                userService
                        .getAll(pageSize, pageNumber)
                        .stream().map(userInfoMapper::toResponse).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}