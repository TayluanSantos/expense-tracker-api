package io.github.tayluansantos.expense_tracker_api.controller;

import io.github.tayluansantos.expense_tracker_api.dto.token.TokenDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserLoginDto;
import io.github.tayluansantos.expense_tracker_api.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication",description = "Endpoints para a autenticação de usuários.")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Autenticar um usuário", description = "Autentica um usuário no sistema, com base no email e senha fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os campos obrigatórios."),
            @ApiResponse(responseCode = "400", description = "Erro ao autenticar usuário. Verifique o email e senha"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLogin) {

        try {

            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
            Authentication authenticate = this.authenticationManager.authenticate(usernamePassword);
            String token = tokenService.generateToken((User) authenticate.getPrincipal());

            return ResponseEntity.ok().body(new TokenDto(token));

        } catch (BadCredentialsException ex) {
           throw new BadCredentialsException("Invalid or inexisted user");
        }

    }
}
