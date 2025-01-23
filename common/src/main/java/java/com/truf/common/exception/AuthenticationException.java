package java.com.truf.common.exception;

import com.truf.common.dto.ResultInfo;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationException extends RuntimeException{
    final ResultInfo resultInfo;

    public AuthenticationException(ResultInfo errorInfo) {
        this.resultInfo = errorInfo;
    }
}
