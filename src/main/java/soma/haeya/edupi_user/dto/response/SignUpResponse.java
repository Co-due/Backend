package soma.haeya.edupi_user.dto.response;

import lombok.Getter;

@Getter
public class SignUpResponse {

    String message;
    Long memberId;

    public SignUpResponse(Long memberId, String message) {
        this.memberId = memberId;
        this.message = message;
    }
}
