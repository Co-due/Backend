package soma.haeya.edupi_user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRequest {

    @NotNull
    @NotBlank
    private String email;
}
