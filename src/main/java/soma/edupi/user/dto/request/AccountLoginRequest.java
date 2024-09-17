package soma.edupi.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountLoginRequest {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @Builder
    public AccountLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
