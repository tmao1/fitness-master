package com.chyld.controllers;

import com.chyld.dtos.AuthDto;
import com.chyld.dtos.UserDto;
import com.chyld.entities.Role;
import com.chyld.entities.User;
import com.chyld.enums.RoleEnum;
import com.chyld.security.JwtToken;
import com.chyld.services.RoleService;
import com.chyld.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String EMAIL_EXISTS_MESSAGE = "This email is in use";

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(final UserService userService, final PasswordEncoder encoder, final RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = encoder;
        this.roleService = roleService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody AuthDto auth) {
        final String requestedEmail = auth.getUsername();

        UserDetails ud =  userService.loadUserByUsername(requestedEmail);
        if (ud != null) {
            return ResponseEntity.badRequest().body(EMAIL_EXISTS_MESSAGE);
        }

        User user = new User();
        user.setEnabled(true);
        user.setUsername(auth.getUsername());
        user.setPassword(passwordEncoder.encode(auth.getPassword()));

        user.setRoles(new ArrayList<>());
        Role r = roleService.findByRole(RoleEnum.USER);
        user.getRoles().add(r);

        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @RequestMapping(value = "/credentials", method = RequestMethod.GET)
    public UserDto credentials(Principal user) {
        int uid = ((JwtToken)user).getUserId();
        User u = userService.findUserById(uid);
        List<String> roles = u.getRoles().stream().map(r -> r.getRole().toString()).collect(Collectors.toList());
        UserDto userDto = new UserDto(u.getUsername(), roles);
        return userDto;
    }
}
