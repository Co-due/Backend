package soma.haeya.edupi_user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soma.haeya.edupi_user.dto.TokenInfo;
import soma.haeya.edupi_user.dto.request.EmailRequest;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignupRequest;
import soma.haeya.edupi_user.dto.response.SignupResponse;
import soma.haeya.edupi_user.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디 패스워드를 받아서 토큰을 발급하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 발급에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> login(@Valid @RequestBody MemberLoginRequest memberLoginRequest,
        HttpServletResponse response) {
        String token = memberService.login(memberLoginRequest);

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/info")
    @Operation(summary = "로그인", description = "발급받은 토큰의 정보를 조회하는 API ")
    @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<TokenInfo> loginInfo(@CookieValue("token") String token) {
        TokenInfo tokenInfo = memberService.findMemberInfo(token);

        return ResponseEntity.ok(tokenInfo);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "사용자 측에서 회원가입 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "회원가입에 실패했습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<SignupResponse> createPost(@Valid @RequestBody SignupRequest signupRequest)
        throws JsonProcessingException {
        return memberService.signUp(signupRequest);
    }

    @GetMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 측에서 로그아웃 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    @Operation(summary = "중복이메일 확인", description = "회원가입 때 중복 아이디를 확인하는 API")
    public ResponseEntity<String> checkEmailDuplication(EmailRequest emailRequest) {
        boolean isDuplicated = memberService.checkEmailDuplication(emailRequest.getEmail());

        if (isDuplicated) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("중복 이메일입니다.");
        } else {
            return ResponseEntity.ok()
                .body("사용 가능합니다.");
        }
    }

}
