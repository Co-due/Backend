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
import soma.edupiuser.account.exception.MetaServerException;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.account.service.domain.AccountRole;
import soma.edupiuser.web.auth.TokenProvider;
import soma.edupiuser.web.client.MetaServerApiClient;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private MetaServerApiClient metaServerApiClient;

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
            .isInstanceOf(MetaServerException.class);
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
}
