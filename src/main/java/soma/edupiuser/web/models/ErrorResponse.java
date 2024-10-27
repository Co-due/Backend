package soma.edupiuser.web.models;

import java.util.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String detail;
    private Object result = Collections.EMPTY_MAP;

    public ErrorResponse(String code, String detail, Object result) {
        this.code = code;
        this.detail = detail;
        this.result = result;
    }
}
