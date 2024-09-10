package soma.haeya.edupi_user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuccessResponse {

    String message;

    public SuccessResponse(String message) {
        this.message = message;
    }
}
