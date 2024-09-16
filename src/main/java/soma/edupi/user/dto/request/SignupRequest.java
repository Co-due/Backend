package soma.edupi.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8에서 20자 이내여야 합니다.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$", message = "비밀번호는 최소 1개의 특수기호를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 공백이 아니어야 합니다.")
    private String name;

    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
        message = "잘못된 형식입니다.")
    private String phoneNumber;
}

