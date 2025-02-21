package io.github.tayluansantos.expense_tracker_api.controller;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User",description = "Endpoints para criação, atualização, listagem e remoção de usuários no sistema.")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Obter a lista de usuários cadastrados", description = "Retorna todos os usuários cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDto>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }


    @Operation(summary = "Obter detalhes de um usuário pelo ID", description = "Retorna os detalhes de um usuário específico com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }


    @Operation(summary = "Cadastrar um novo usuário", description = "Recebe os dados de um novo usuário e os cadastra no sistema, retornando os dados do usuário criado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os campos obrigatórios."),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,value = "/register")
    public ResponseEntity<UserResponseDto> save(@RequestBody @Valid UserRequestDto userRequest, UriComponentsBuilder uriBuilder){
        UserResponseDto userResponse = userService.save(userRequest);
        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponse.id()).toUri();
        return ResponseEntity.created(uri).body(userResponse);
    }

    @Operation(summary = "Atualizar as informações de um usuário", description = "Atualiza os dados de um usuário existente com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os campos obrigatórios."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,value = "/{id}")
    public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UserRequestDto userRequest, @PathVariable Long id){
        return ResponseEntity.ok().body(userService.update(userRequest,id));
    }

    @Operation(summary = "Remover um usuário", description = "Exclui um usuário do sistema com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
