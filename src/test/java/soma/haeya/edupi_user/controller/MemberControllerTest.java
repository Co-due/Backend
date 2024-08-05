package soma.haeya.edupi_user.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignupRequest;
import soma.haeya.edupi_user.dto.response.Response;
import soma.haeya.edupi_user.exception.DbValidException;
import soma.haeya.edupi_user.service.MemberService;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

  @MockBean
  MemberService memberService;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    // Mockito 초기화
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("로그인에 성공하면 jwt 토큰을 쿠키에 넣는다.")
  void login() throws Exception {
    MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
        .email("asdf@naver.com")
        .password("asdf1234")
        .build();

    doReturn("token").when(memberService).login(memberLoginRequest);

    mockMvc.perform(post("/member/login")
            .content(objectMapper.writeValueAsString(memberLoginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(cookie().exists("token"));

  }

  @Test
  @DisplayName("회원가입에 성공하면 OK를 반환한다.")
  void signUp() throws Exception {
    // given
    SignupRequest signupRequest = SignupRequest.builder()
        .email("aabbcc@naver.com")
        .name("김미미")
        .password("qpwoeiruty00@")
        .build();

    // Mocking
    when(memberService.signUp(signupRequest)).thenReturn(
        ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response("회원가입 성공"))
    );

    // When & Then
    mockMvc.perform(post("/member/signup")
        .content(objectMapper.writeValueAsString(signupRequest))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk());

  }

  @Test
  @DisplayName("회원가입 중 잘못된 요청으로 예외가 발생하면 BAD_REQUEST를 반환한다")
  void signUp_whenInvalidRequest_thenBadRequest() throws Exception {
    // Given
    SignupRequest signupRequest = SignupRequest.builder()
        .email("invalid-email")  // 유효하지 않은 이메일
        .name("김미미")
        .password("qpwoeiruty00@")
        .build();

    // Mocking
    when(memberService.signUp(signupRequest))
        .thenThrow(new DbValidException("Invalid email address"));

    // When & Then
    mockMvc.perform(post("/member/signup")
            .content(objectMapper.writeValueAsString(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()); // 예외 처리 후 상태 코드가 400인지 검증
  }


}