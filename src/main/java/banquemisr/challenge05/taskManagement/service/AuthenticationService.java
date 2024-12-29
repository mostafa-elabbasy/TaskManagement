package banquemisr.challenge05.taskManagement.service;

import banquemisr.challenge05.taskManagement.exception.BusinessException;
import banquemisr.challenge05.taskManagement.mapper.UserMapper;
import banquemisr.challenge05.taskManagement.model.dto.UserDto;
import banquemisr.challenge05.taskManagement.model.entity.Role;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.request.CreateUserRequest;
import banquemisr.challenge05.taskManagement.model.request.LoginUserRequest;
import banquemisr.challenge05.taskManagement.model.response.LoginResponse;
import banquemisr.challenge05.taskManagement.repository.RoleRepository;
import banquemisr.challenge05.taskManagement.repository.UserRepository;
import banquemisr.challenge05.taskManagement.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public LoginResponse authenticate(LoginUserRequest loginUserRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequest.getEmail(),
                        loginUserRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
                .orElseThrow();
        String token = jwtService.generateToken(user);
        return new LoginResponse(token, jwtService.getExpirationTime());
    }

    public UserDto createUser(CreateUserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new BusinessException("email already exist");
        }
        Set<Role> roles = roleRepository.findByAuthorityIn(userRequest.getAuthorities());

        User user = User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .authorities(roles)
                .password(passwordEncoder.encode(userRequest.getPassword())).build();

        return userMapper.toDto(userRepository.save(user));
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }
}
