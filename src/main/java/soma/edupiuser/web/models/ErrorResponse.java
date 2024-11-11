package soma.edupiuser.web.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String code;
    private String detail;
    private Object result = Collections.EMPTY_MAP;


    public ErrorResponse(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public ErrorResponse(String code, String detail, Map<String, Object> result) {
        this.code = code;
        this.detail = detail;
        this.result = result;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("detail", detail);
        map.put("result", result);
        return map;
    }
}
