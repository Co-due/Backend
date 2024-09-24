package soma.edupiuser.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.models.TokenInfo;

@Tag(name = "Account", description = "Account API")
public interface AccountOpenApi {

    @Operation(summary = "로그인", description = "아이디 패스워드를 받아서 토큰을 발급하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 발급에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Void> login(@Valid @RequestBody AccountLoginRequest accountLoginRequest,
        HttpServletResponse response);

    @Operation(summary = "로그인", description = "발급받은 토큰의 정보를 조회하는 API ")
    @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    ResponseEntity<TokenInfo> loginInfo(@CookieValue("token") String token);

    @Operation(summary = "회원가입", description = "사용자 측에서 회원가입 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "회원가입에 실패했습니다.", content = @Content(mediaType = "application/json"))
    })
    ResponseEntity<SignupResponse> createAccount(@Valid @RequestBody SignupRequest signupRequest)
        throws JsonProcessingException, MessagingException;


    @Operation(summary = "로그아웃", description = "사용자 측에서 로그아웃 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Void> logout(HttpServletResponse response);
}
