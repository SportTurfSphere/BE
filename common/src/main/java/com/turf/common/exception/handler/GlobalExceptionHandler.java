package com.turf.common.exception.handler;

import com.turf.common.dto.GenericResponse;
import com.turf.common.dto.ResultInfo;
import com.turf.common.exception.*;
import com.turf.common.util.ResultUtil;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.turf.common.constants.ResultInfoConstants.FILE_SIZE_LIMIT_EXCEEDED;
import static com.turf.common.constants.ResultInfoConstants.SYSTEM_ERROR;


@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    static final String STATUS_FAILED = "F";
    static final String CODE_FAILED = "FAILED";
    static final String CODE_ID_BAD_REQ = "400";
    static final String CODE_ID_SYSTEM_ERROR = "11000002";
    static final String CODE_SYSTEM_ERROR = "SYSTEM_ERROR";

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class
    })
    @ResponseBody
    protected ResponseEntity<GenericResponse> handleIllegalArgumentException(RuntimeException ex) {
        log.error(getStackTraceAsString(ex));
        val resultInfo = new ResultInfo();
        resultInfo.setMessage(ex.getMessage());
        resultInfo.setCode(CODE_FAILED);
        resultInfo.setCodeId(CODE_ID_BAD_REQ);
        resultInfo.setStatus(STATUS_FAILED);
        return ResultUtil.generateErrorResponse(resultInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<GenericResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error(getStackTraceAsString(ex));
        log.info("Caught resource not found exception : {}", ex.getMessage());
        val resultInfo = new ResultInfo();
        resultInfo.setMessage(ex.getMessage());
        resultInfo.setCode(CODE_FAILED);
        resultInfo.setCodeId(String.valueOf(HttpStatus.NOT_FOUND.value()));
        resultInfo.setStatus(STATUS_FAILED);
        return ResultUtil.generateErrorResponse(resultInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<GenericResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        log.error(getStackTraceAsString(ex));
        return ResultUtil.generateErrorResponse(ex.getResultInfo(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<GenericResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException : {}", getStackTraceAsString(ex));
        val message = new StringBuilder();
        for (val fieldError : ex.getBindingResult().getFieldErrors()) {
            if (message.length() > 0) {
                message.append(", ");
            }
            message.append(fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage()
                    : fieldError.getField() + " is required");
        }
        val resultInfo = new ResultInfo();
        resultInfo.setMessage(message.toString());
        resultInfo.setCode(CODE_FAILED);
        resultInfo.setCodeId(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        resultInfo.setStatus(STATUS_FAILED);
        return ResultUtil.generateErrorResponse(resultInfo, HttpStatus.BAD_REQUEST);
    }

    private ResultInfo prepareResultInfo(Exception exception) {
        val resultInfo = new ResultInfo();
        String message = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
        val finalMsg = exception instanceof DataIntegrityViolationException ?
                extractDuplicateEntryMessage(message) : message;
        resultInfo.setMessage(finalMsg);
        resultInfo.setCodeId(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        resultInfo.setCode(CODE_FAILED);
        resultInfo.setStatus(STATUS_FAILED);
        return resultInfo;
    }

    private String extractDuplicateEntryMessage(String message) {
        if (message != null && message.contains("Duplicate entry")) {
            int startIndex = message.indexOf("Duplicate entry");
            int endIndex = message.indexOf(" for key");
            if (startIndex != -1 && endIndex != -1) {
                return message.substring(startIndex, endIndex);
            }
        }
        return "Data integrity violation";
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<GenericResponse> validationException(ValidationException ex) {
        log.info("ValidationException :{}", ex.getResultInfo());
        return ResultUtil.generateErrorResponse(ex.getResultInfo(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ValidationCompletionException.class})
    @ResponseBody
    protected ResponseEntity<GenericResponse> handleConflict(ValidationCompletionException ex) {
        log.error(getStackTraceAsString(ex));
        log.error("GlobalExceptionHandler to handle CompletionException:{}", ex.getMessage());
        return ResultUtil.generateErrorResponse(ex.getResultInfo(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<GenericResponse> typeNotFoundException(TypeNotFoundException ex) {
        log.error(getStackTraceAsString(ex));
        return ResultUtil.generateErrorResponse(ex.getResultInfo(), HttpStatus.BAD_REQUEST);
    }

    /*@ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<GenericResponse> dataIntegrityViolation(DataIntegrityViolationException integrityViolationException) {
        log.error("DataIntegrityViolationException: {}", getStackTraceAsString(integrityViolationException));
        return ResultUtil.generateErrorResponse(prepareResultInfo(integrityViolationException), HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(FileProcessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<GenericResponse> fileReadWriteException(FileProcessException ex) {
        log.error(getStackTraceAsString(ex));
        return ResultUtil.generateErrorResponse(ex.getResultInfo(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    ResponseEntity<GenericResponse> authenticationException(AuthenticationException ex) {
        return ResultUtil.generateErrorResponse(ex.getResultInfo(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<GenericResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ResultUtil.generateErrorResponse(FILE_SIZE_LIMIT_EXCEEDED, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // Remove logging stack track in production
    private static String getStackTraceAsString(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    @ExceptionHandler(value = {CustomException.class})
    @ResponseBody
    protected ResponseEntity<GenericResponse> handleCustomException(Throwable ex) {
        log.error(getStackTraceAsString(ex));
        log.error("CustomExceptionHandling :{}", ex.getMessage());
        val resultInfo = new ResultInfo();
        resultInfo.setMessage(ex.getMessage());
        resultInfo.setCode(CODE_SYSTEM_ERROR);
        resultInfo.setCodeId(CODE_ID_SYSTEM_ERROR);
        resultInfo.setStatus(STATUS_FAILED);

        return ResultUtil.generateErrorResponse(resultInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Throwable.class})
    @Order(Ordered.LOWEST_PRECEDENCE)
    @ResponseBody
    protected ResponseEntity<GenericResponse> handleConflict(Throwable ex) {
        log.error(getStackTraceAsString(ex));
        log.error("GlobalExceptionHandling :{}", ex.getMessage());
        return ResultUtil.generateErrorResponse(SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
