package soma.edupiuser.account;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import soma.edupiuser.account.exception.MetaServerException;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.service.AccountService;
import soma.edupiuser.account.service.EmailService;
import soma.edupiuser.web.exception.ErrorEnum;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)   // Spring Security 필터 비활성화
class AccountControllerTest {

    @MockBean
    AccountService accountService;

    @MockBean
    EmailService emailService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("로그인에 성공하면 jwt 토큰을 쿠키에 넣는다.")
    void login() throws Exception {
        AccountLoginRequest accountLoginRequest = AccountLoginRequest.builder()
            .email("asdf@naver.com")
            .password("asdf1234")
            .build();

        doReturn("token").when(accountService).login(accountLoginRequest);

        mockMvc.perform(post("/v1/account/login")
                .content(objectMapper.writeValueAsString(accountLoginRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
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
        when(accountService.signup(signupRequest)).thenReturn(
            ResponseEntity
                .status(HttpStatus.OK)
                .body(new SignupResponse("회원가입 성공"))
        );

        // When & Then
        mockMvc.perform(post("/v1/account/signup")
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
        when(accountService.signup(signupRequest))
            .thenThrow(new MetaServerException(ErrorEnum.META_SERVER_EXCEPTION));

        // When & Then
        mockMvc.perform(post("/v1/account/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest()); // 예외 처리 후 상태 코드가 400인지 검증
    }


}