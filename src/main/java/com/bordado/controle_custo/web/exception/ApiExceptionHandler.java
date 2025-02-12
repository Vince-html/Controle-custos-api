package com.bordado.controle_custo.web.exception;

import com.bordado.controle_custo.exceptions.ResourceNotFoundException;
import com.bordado.controle_custo.exceptions.SupplierNotFoundException;
import com.bordado.controle_custo.exceptions.UniqueNameViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({UniqueNameViolationException.class})
    public ResponseEntity<ErrorMessage> uniqueViolationException(RuntimeException ex,
                                                                 HttpServletRequest request
    ) {
        log.error("API Error - unique - ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(
                new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage())
        );

    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException ex,
//                                                              HttpServletRequest request
//    ) {
//        log.error("API Error - UserNameUniqueViolationException - ", ex);
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(
//                new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage())
//        );
//
//    }

    @ExceptionHandler({ResourceNotFoundException.class, SupplierNotFoundException.class})
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex,
                                                                HttpServletRequest request
    ) {
        log.error("API Error - EntityNotFoundException - ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage())
                );

    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest request,
                                                                        BindingResult result) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) invalido(s)", result));
    }
}
