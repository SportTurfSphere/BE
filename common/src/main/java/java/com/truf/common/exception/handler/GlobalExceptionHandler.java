package java.com.truf.common.exception.handler;

import com.truf.common.exception.*;
import com.truf.common.util.ResultUtil;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.truf.common.constants.ResultInfoConstants.*;


@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class
    })
    @ResponseBody
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex) {
        log.error(getStackTraceAsString(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseBody
    protected ResponseEntity<Object> handleConflict(Throwable ex) {
        log.error(getStackTraceAsString(ex));
        log.error("GlobalExceptionHandling:{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(SYSTEM_ERROR, null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error(getStackTraceAsString(ex));
        log.info("Caught resource not found exception : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(NOT_FOUND, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<Object> onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(getStackTraceAsString(ex));
        val resultInfo = REQ_FIELD_VALIDATION_ERROR;
        val message = new StringBuilder();
        for (val fieldError : ex.getBindingResult().getFieldErrors()) {
            if (message.length() > 0) {
                message.append(", ");
            }
            message.append(fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage()
                    : fieldError.getField() + " is required");
        }
        resultInfo.setMessage(message.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(resultInfo, null));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<Object> validationException(ValidationException ex) {
        log.info("ValidationException :{}", ex.getResultInfo());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(ex.getResultInfo(), null));
    }

    @ExceptionHandler(value = {ValidationCompletionException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleConflict(ValidationCompletionException ex) {
        log.error(getStackTraceAsString(ex));
        log.error("GlobalExceptionHandler to handle CompletionException:{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(ex.getResultInfo(), null));
    }

    @ExceptionHandler(TypeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<Object> typeNotFoundException(TypeNotFoundException ex) {
        log.error(getStackTraceAsString(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(ex.getResultInfo(), null));
    }

    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<Object> uniqueConstrainViolation(ConstraintViolationException constraintViolationException,
                                                    DataIntegrityViolationException integrityViolationException) {
        log.error(getStackTraceAsString(constraintViolationException));
        log.error(getStackTraceAsString(integrityViolationException));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(SOMETHING_WENT_WRONG, null));
    }

    @ExceptionHandler(FileProcessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<Object> fileReadWriteException(FileProcessException ex) {
        log.error(getStackTraceAsString(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(ex.getResultInfo(), null));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    ResponseEntity<Object> authenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultUtil.buildResult(ex.getResultInfo(), null));
    }


    // Remove logging stack track in production
    private static String getStackTraceAsString(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
