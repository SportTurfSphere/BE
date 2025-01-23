package java.com.truf.common.constants;


import com.truf.common.dto.ResultInfo;

public class ResultInfoConstants {
    private ResultInfoConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String STATUS_FAILED = "F";
    public static final String RESULT_MSG_NOT_FOUND = "Result message not found";
    //Error Code Format - XXYYYZZZ where,
    //    XX -> Service Code. For application-settings, service code is 11
    //    YYY -> Feature code (service specific). For common service, feature code is 000
    //    ZZZ -> Sequence number should start from 001
    public static final ResultInfo SUCCESS = new ResultInfo("11000001", "SUCCESS", "Successful Request", "S");
    public static final ResultInfo SYSTEM_ERROR = new ResultInfo("11000002", "SYSTEM_ERROR", "Internal Server Error", "F");
    public static final ResultInfo BAD_REQUEST = new ResultInfo("11000003", "BAD_REQUEST", "Bad Request", "F");
    public static final ResultInfo MSG_PARSE_ERROR = new ResultInfo("11000004", "MSG_PARSE_ERROR", "Message Parsing Error", "F");
    public static final ResultInfo NOT_FOUND = new ResultInfo("11000005", "NOT_FOUND", "Requested resource not found", "F");
    public static final ResultInfo REQ_FIELD_VALIDATION_ERROR = new ResultInfo("11000006", "REQ_FIELD_VALIDATION_ERROR", "", "F");
    public static final ResultInfo SOMETHING_WENT_WRONG = new ResultInfo("11000009", "SOMETHING_WENT_WRONG", "Please try again", "F");

}