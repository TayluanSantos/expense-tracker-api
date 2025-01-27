package io.github.tayluansantos.expense_tracker_api.controller;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserLoginDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.service.ICategoryService;
import io.github.tayluansantos.expense_tracker_api.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(IUserService userService, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> save(@RequestBody @Valid UserRequestDto userRequest, UriComponentsBuilder uriBuilder){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UserRequestDto userRequest, @PathVariable Long id){
        return ResponseEntity.ok().body(userService.update(userRequest,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginDto userLogin){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
        Authentication authenticate = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }
}
