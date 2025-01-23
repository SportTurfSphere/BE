package java.com.truf.common.util;

import com.truf.common.dto.GenericResponse;
import com.truf.common.dto.ResultInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.truf.common.constants.ResultInfoConstants.MSG_PARSE_ERROR;


public class ResultUtil {


    private ResultUtil() {
        throw new IllegalStateException("Utility class");
    }
    public static ResponseEntity<Object> generateResponse(ResultInfo resultInfo, Object resObj) {
        return new ResponseEntity<>(GenericResponse.builder().resultInfo(resultInfo).data(resObj).build(), HttpStatus.OK);
    }

    public static String buildResult(ResultInfo resultInfo, Object data) {
        try {
            return ObjectMapperUtil.mapObjectToJson(GenericResponse.builder().resultInfo(resultInfo).data(data).build());
        } catch (IOException e) {
            return ResultUtil.buildResult(MSG_PARSE_ERROR, null);
        }
    }
}
