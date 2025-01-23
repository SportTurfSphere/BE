package java.com.truf.common.exception.handler;

import com.rs.cm.application.service.dto.common.ResultInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CompletionException;

@lombok.Data
@Getter
@Setter
public class ValidationCompletionException extends CompletionException {
   private final ResultInfo resultInfo;

    public ValidationCompletionException(ResultInfo errorInfo) {
        this.resultInfo = errorInfo;
    }

}