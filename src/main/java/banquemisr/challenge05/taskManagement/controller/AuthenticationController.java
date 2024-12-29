package banquemisr.challenge05.taskManagement.controller;

import banquemisr.challenge05.taskManagement.model.dto.UserDto;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.request.CreateUserRequest;
import banquemisr.challenge05.taskManagement.model.request.LoginUserRequest;
import banquemisr.challenge05.taskManagement.model.response.LoginResponse;
import banquemisr.challenge05.taskManagement.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserRequest loginUserRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginUserRequest));
    }

    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PostMapping("/create-user")
    public ResponseEntity<UserDto> register(@RequestBody CreateUserRequest userRequest) {
        return ResponseEntity.ok(authenticationService.createUser(userRequest));
    }

    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @GetMapping("/users-list")
    public ResponseEntity<List<UserDto>> getUsersList() {
        return ResponseEntity.ok(authenticationService.getAllUsers());
    }

}
