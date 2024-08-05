package soma.haeya.edupi_user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import soma.haeya.edupi_user.auth.TokenProvider;
import soma.haeya.edupi_user.client.MemberApiClient;
import soma.haeya.edupi_user.domain.Member;
import soma.haeya.edupi_user.domain.Role;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignupRequest;
import soma.haeya.edupi_user.dto.response.Response;
import soma.haeya.edupi_user.exception.DbValidException;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

  @InjectMocks
  private MemberService memberService;

  @Mock
  private MemberApiClient memberRepository;

  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private ObjectMapper objectMapper;

  private MemberLoginRequest memberLoginRequest;


  @BeforeEach
  void init() {
    memberLoginRequest = MemberLoginRequest.builder()
        .email("asdf@naver.com")
        .password("asdf1234")
        .build();
  }


  @Test
  @DisplayName("아이디와 패스워드에 맞는 멤버가 있으면 token을 반환한다.")
  void memberLogin() {

    Member expectedMember = new Member("asdf@naver.com", "홍길동", Role.ROLE_USER);

    String expectedToken = "token";

    when(memberRepository.findMemberByEmailAndPassword(memberLoginRequest)).thenReturn(
        expectedMember);
    when(tokenProvider.generateToken(expectedMember)).thenReturn(expectedToken);

    String resultToken = memberService.login(memberLoginRequest);

    Assertions.assertThat(resultToken).isEqualTo(expectedToken);
  }

  @Test
  @DisplayName("아이디 패스워드에 맞는 멤버가 없으면 예외를 반환한다.")
  void memberLoginException() {

    when(memberRepository.findMemberByEmailAndPassword(memberLoginRequest)).thenThrow(
        new IllegalArgumentException("아이디 비밀번호가 일치하지 않습니다.")
    );

    Assertions.assertThatThrownBy(() -> memberService.login(memberLoginRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("아이디 비밀번호가 일치하지 않습니다.");
  }

  @Test
  @DisplayName("회워가입에 성공하면 OK")
  public void signUp_success() throws Exception {
    // Given
    SignupRequest signupRequest = SignupRequest.builder()
        .email("valid-email@example.com")
        .name("John Doe")
        .password("validPassword123!")
        .build();

    Response mockResponse = new Response("회원가입 성공");
    ResponseEntity<Response> responseEntity = ResponseEntity
        .status(HttpStatus.OK)
        .body(mockResponse);

    when(memberRepository.saveMember(signupRequest)).thenReturn(responseEntity);

    // When
    ResponseEntity<Response> result = memberService.signUp(signupRequest);

    // Then
    Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
  }

  @Test
  @DisplayName("회원가입 요청 중 client 에러 발생")
  public void signUp_clientError() throws JsonProcessingException {
    // Given
    SignupRequest signupRequest = SignupRequest.builder()
        .email("invalid-email@example.com")
        .name("John Doe")
        .password("validPassword123")
        .build();

    // JSON 문자열과 해당 문자열을 파싱한 결과 객체
    String errorResponse = "{\"message\":\"Invalid request\"}";
    Response mockResponse = new Response("Invalid request");

    // HttpClientErrorException을 모킹하여 예외의 응답 본문이 JSON 문자열로 반환되도록 설정
    HttpClientErrorException exception = mock(HttpClientErrorException.class);
    when(exception.getResponseBodyAsString()).thenReturn(errorResponse);
    when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

    // 예외를 던지도록 설정
    when(memberRepository.saveMember(signupRequest)).thenThrow(exception);

    // objectMapper의 readValue 메서드가 JSON 문자열을 Response 객체로 변환하도록 설정
    when(objectMapper.readValue(errorResponse, Response.class)).thenReturn(mockResponse);

    // When & Then
    DbValidException thrown = assertThrows(DbValidException.class, () -> memberService.signUp(signupRequest));
  }
}
