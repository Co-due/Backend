package soma.haeya.edupi_user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import soma.haeya.edupi_user.dto.TokenInfo;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignupRequest;
import soma.haeya.edupi_user.dto.response.Response;

@Tag(name = "Member", description = "Member API")
public interface MemberSpecification {

    @Operation(summary = "로그인", description = "아이디 패스워드를 받아서 토큰을 발급하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 발급에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Void> login(@Valid @RequestBody MemberLoginRequest memberLoginRequest, HttpServletResponse response);

    @Operation(summary = "로그인", description = "발급받은 토큰의 정보를 조회하는 API ")
    @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    ResponseEntity<TokenInfo> loginInfo(@CookieValue("token") String token);

    @Operation(summary = "회원가입", description = "사용자 측에서 회원가입 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "회원가입에 실패했습니다.", content = @Content(mediaType = "application/json"))
    })
    ResponseEntity<Response> createPost(@Valid @RequestBody SignupRequest signupRequest) throws JsonProcessingException;


    @Operation(summary = "로그아웃", description = "사용자 측에서 로그아웃 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    ResponseEntity<Void> logout(HttpServletResponse response);
}
