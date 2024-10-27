package soma.edupiuser.account.service;

import static org.mockito.Mockito.when;

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
<<<<<<< HEAD
import org.springframework.web.client.HttpClientErrorException;
<<<<<<< HEAD
import soma.edupiuser.account.auth.TokenProvider;
import soma.edupiuser.account.client.MetaServerApiClient;
=======
>>>>>>> 8d65b35 ([#48]fiix: handler 함수 분리)
=======
>>>>>>> 85e1c7b ([#48]feat: 토큰 만료 예외, 예외 구조 추가)
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.account.service.domain.AccountRole;
<<<<<<< HEAD
import soma.edupiuser.web.exception.MetaValidException;
=======
import soma.edupiuser.web.auth.TokenProvider;
import soma.edupiuser.web.client.MetaServerApiClient;
<<<<<<< HEAD
import soma.edupiuser.web.exception.DbValidException;
>>>>>>> 8d65b35 ([#48]fiix: handler 함수 분리)
import soma.edupiuser.web.models.ErrorResponse;
=======
>>>>>>> 85e1c7b ([#48]feat: 토큰 만료 예외, 예외 구조 추가)

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
<<<<<<< HEAD
    private MetaServerApiClient metaServerApiClient;
=======
    private MetaServerApiClient dbServerApiClient;
>>>>>>> 8d65b35 ([#48]fiix: handler 함수 분리)

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private ObjectMapper objectMapper;

    private AccountLoginRequest accountLoginRequest;


    @BeforeEach
    void init() {
        accountLoginRequest = AccountLoginRequest.builder()
            .email("asdf@naver.com")
            .password("asdf1234")
            .build();
    }


    @Test
    @DisplayName("아이디와 패스워드에 맞는 멤버가 있으면 token을 반환한다.")
    void accountLogin() {

        Account expectedAccount = new Account("asdf@naver.com", "홍길동", AccountRole.USER);

        String expectedToken = "token";

        when(metaServerApiClient.login(accountLoginRequest)).thenReturn(
            expectedAccount);
        when(tokenProvider.generateToken(expectedAccount)).thenReturn(expectedToken);

        String resultToken = accountService.login(accountLoginRequest);

        Assertions.assertThat(resultToken).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("아이디 패스워드에 맞는 멤버가 없으면 예외를 반환한다.")
    void AccountLoginException() {

        when(metaServerApiClient.login(accountLoginRequest)).thenThrow(
            new IllegalArgumentException("아이디 비밀번호가 일치하지 않습니다.")
        );

        Assertions.assertThatThrownBy(() -> accountService.login(accountLoginRequest))
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

        ResponseEntity<SignupResponse> responseEntity = ResponseEntity
            .status(HttpStatus.OK)
            .body(new SignupResponse("회원가입 성공"));

        when(metaServerApiClient.saveAccount(signupRequest)).thenReturn(responseEntity);

        // When
        ResponseEntity<SignupResponse> result = accountService.signup(signupRequest);

        // Then
        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }
<<<<<<< HEAD

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
        ErrorResponse mockResponse = new ErrorResponse("Invalid request");

        // HttpClientErrorException을 모킹하여 예외의 응답 본문이 JSON 문자열로 반환되도록 설정
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getResponseBodyAsString()).thenReturn(errorResponse);
        when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        // 예외를 던지도록 설정
        when(metaServerApiClient.saveAccount(signupRequest)).thenThrow(exception);

        // objectMapper의 readValue 메서드가 JSON 문자열을 Response 객체로 변환하도록 설정
        when(objectMapper.readValue(errorResponse, ErrorResponse.class)).thenReturn(mockResponse);

        // When & Then
        assertThrows(MetaValidException.class, () -> accountService.signup(signupRequest));
    }
=======
>>>>>>> 85e1c7b ([#48]feat: 토큰 만료 예외, 예외 구조 추가)
}
