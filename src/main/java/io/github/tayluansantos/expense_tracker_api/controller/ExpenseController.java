package io.github.tayluansantos.expense_tracker_api.controller;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.service.IExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
@Tag(name = "Expense",description = "Endpoints para criação, atualização, listagem e remoção de despesas no sistema.")
public class ExpenseController {

    private final IExpenseService expenseService;

    public ExpenseController(IExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @Operation(summary = "Cadastrar uma nova despesa", description = "Recebe os dados de uma nova despesa e as cadastra no sistema, retornando os dados da despesa criada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os campos obrigatórios."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @PostMapping
    public ResponseEntity<ExpenseResponseDto> save(@RequestBody @Valid ExpenseRequestDto expenseRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.save(expenseRequest));
    }

    @Operation(summary = "Obter detalhes de uma despesa pelo ID", description = "Retorna os detalhes de uma despesa específica com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despesa encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.findById(id));
    }

    @Operation(summary = "Obter a lista de despesas cadastradas", description = "Retorna todos as despesas cadastradas no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de despesas retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> findAll () {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.findAll());
    }

    @Operation(summary = "Obter a lista de despesas com base em um intervalo de datas", description = "Retorna todos as despesas cadastradas no sistema com base no intervalo de datas fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de despesas retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Despesas não encontradas"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @GetMapping("/search-by-date")
    public ResponseEntity<List<ExpenseResponseDto>> findByDate (@RequestParam("startDate") LocalDate startDate,
                                                                @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.findAllByDateRange(startDate,endDate));
    }

    @Operation(summary = "Atualizar as informações de uma despesa", description = "Atualiza os dados de uma despesa existente com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida. Verifique os campos obrigatórios."),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> update(@RequestBody ExpenseRequestDto expenseRequest,
                                                     @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.update(expenseRequest,id));
    }

    @Operation(summary = "Remover uma despesa", description = "Exclui uma despesa do sistema com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Despesa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor interno")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        expenseService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
