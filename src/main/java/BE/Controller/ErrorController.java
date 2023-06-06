package BE.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> exceptionHandler(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare tehnica");
    }
}
