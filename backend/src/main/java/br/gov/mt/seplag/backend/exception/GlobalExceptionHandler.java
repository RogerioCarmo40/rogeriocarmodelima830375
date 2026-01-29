package br.gov.mt.seplag.backend.exception;

import br.gov.mt.seplag.backend.dto.ErroRespostaDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request
    ) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> tratarValidacao(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        String mensagem = ex.getBindingResult()
            .getFieldErrors()
            .get(0)
            .getDefaultMessage();

    ErroRespostaDTO erro = new ErroRespostaDTO(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            mensagem,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}

