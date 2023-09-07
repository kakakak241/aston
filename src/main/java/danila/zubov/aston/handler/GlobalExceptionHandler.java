package danila.zubov.aston.handler;

import danila.zubov.aston.exception.InsufficientBalanceException;
import danila.zubov.aston.exception.InvalidTransactionException;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseBody
  public ErrorResponse handleNotFoundException(NoSuchElementException ex) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidTransactionException.class)
  @ResponseBody
  public ErrorResponse handleInvalidTransaction(InvalidTransactionException ex) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InsufficientBalanceException.class)
  @ResponseBody
  public ErrorResponse handleInsufficientBalance(InsufficientBalanceException ex) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
  }

  @Data
  @AllArgsConstructor
  public static class ErrorResponse {

    private String code;
    private String message;
  }
}