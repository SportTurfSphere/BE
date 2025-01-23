package java.com.truf.common.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultInfo implements Serializable {
    private static Map<String, ResultInfo> infoMap;
    private String codeId;
    private String code;
    private String message;
    private String status;

    public static ResultInfo get(String code) {
        if (infoMap == null) {
            return null;
        }
        return infoMap.get(code);
    }

    public static void setResultInfoMap(Map<String, ResultInfo> resultInfoMap) {
        infoMap = resultInfoMap;
    }
}