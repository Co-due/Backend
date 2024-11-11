package soma.edupiuser.account.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class SignupRequest {

    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8에서 20자 이내여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z].*)(?=.*\\d.*|.*[^A-Za-z0-9].*|.*[A-Za-z].*).*",
        message = "비밀번호는 문자, 숫자, 특수문자 중 두 가지 이상을 포함해야 합니다."
    )
    @Pattern(
        regexp = "(?!.*(.)\\1{2}).{8,}$",
        message = "비밀번호는 동일한 문자가 세 번 이상 반복될 수 없습니다."
    )
    private String password;

    private String name;

    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
        message = "잘못된 형식입니다.")
    private String phoneNumber;

    @Builder
    public SignupRequest(String email, String password, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}

