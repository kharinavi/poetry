package ru.kharina.study.poetry.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kharina.study.poetry.model.*;
import ru.kharina.study.poetry.repository.PoetRepository;
import ru.kharina.study.poetry.repository.RoleRepository;
import ru.kharina.study.poetry.repository.UserRepository;
import ru.kharina.study.poetry.service.CustomUsrDetails;
import ru.kharina.study.poetry.service.CustomUsrDetailsService;
import ru.kharina.study.poetry.service.TokenService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final CustomUsrDetailsService usrDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PoetRepository poetRepository;


    public AuthController(TokenService tokenService, AuthenticationManager authManager,
                          CustomUsrDetailsService usrDetailsService, UserRepository userRepository,
                          RoleRepository roleRepository, PoetRepository poetRepository) {
        super();
        this.tokenService = tokenService;
        this.authManager = authManager;
        this.usrDetailsService = usrDetailsService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.poetRepository = poetRepository;
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody LoginRequest request) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String hashedPassword = passwordEncoder.encode(request.password);

        User dbUser = new User();
        dbUser.setEmail(request.username);
        dbUser.setPassword(hashedPassword);
        Set<Role_Class> roles = new HashSet<>();
        Role_Class rc = roleRepository.getOne(1L);
        roles.add(rc);
        dbUser.setRoles(roles);
        dbUser.setStatus(Status.ACTIVE);

        userRepository.save(dbUser);
        User savedUser = userRepository.findByEmail(request.username).get();

        Poet newPoet = new Poet();
        newPoet.setSecurityId(Math.toIntExact(savedUser.getId()));
        newPoet.setEmail(request.username);
        poetRepository.save(newPoet);

        CustomUsrDetails user = new CustomUsrDetails(dbUser);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, request.password, Collections.EMPTY_LIST);

        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return new LoginResponse("User with email = "+ request.username + " successfully registered!"

                , access_token, refresh_token);
    }

    record LoginRequest(String username, String password) {};
    record LoginResponse(String message, String access_jwt_token, String refresh_jwt_token) {};
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username, request.password);
        Authentication auth = authManager.authenticate(authenticationToken);

        CustomUsrDetails user = (CustomUsrDetails) usrDetailsService.loadUserByUsername(request.username);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return new LoginResponse("User with email = "+ request.username + " successfully logined!"

                , access_token, refresh_token);
    }

    record RefreshTokenResponse(String access_jwt_token, String refresh_jwt_token) {};
    @GetMapping("/token/refresh")
    public RefreshTokenResponse refreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String refreshToken = headerAuth.substring(7, headerAuth.length());

        String email = tokenService.parseToken(refreshToken);
        CustomUsrDetails user = (CustomUsrDetails) usrDetailsService.loadUserByUsername(email);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return new RefreshTokenResponse(access_token, refresh_token);
    }
}
