package soma.edupi.user.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import soma.edupi.user.client.config.DbServerApiRestClientConfig;
import soma.edupi.user.domain.Account;
import soma.edupi.user.domain.Role;
import soma.edupi.user.dto.request.AccountLoginRequest;
import soma.edupi.user.dto.request.SignupRequest;
import soma.edupi.user.dto.response.SignupResponse;

@SpringBootTest
@Import(DbServerApiRestClientConfig.class)
class AccountApiClientTest {

    @Autowired
    private ObjectMapper mapper;

    private MockWebServer mockWebServer;
    private DbServerApiClient dbServerApiClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // dbApiClient에 mockWebServer를 적용해 테스트
        DbServerApiRestClientConfig config = new DbServerApiRestClientConfig();
        dbServerApiClient = config.dbServerApiClient(
            mockWebServer.url("/").toString());
    }

    @AfterEach
    void shutdown() throws IOException {
        if (mockWebServer != null) {
            this.mockWebServer.shutdown();
        }
    }

    @Test
    @DisplayName("이메일과 비밀번호로 회원을 찾는 Http 요청 테스트")
    void testFindMemberByEmailAndPassword() throws JsonProcessingException {
        Account expectedResponse = new Account(1L, "asdf@naver.com", "", "홍길동", Role.ROLE_USER);

        // mockWebServer 응답 설정
        mockWebServer.enqueue(new MockResponse()
            .setBody(mapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));

        AccountLoginRequest request = AccountLoginRequest.builder()
            .email("asdf@naver.com")
            .password("password").build();

        Account result = dbServerApiClient.login(request);

        Assertions.assertThat(result.getEmail()).isEqualTo(expectedResponse.getEmail());
        Assertions.assertThat(result.getName()).isEqualTo(expectedResponse.getName());
    }

    @Test
    @DisplayName("회원 가입 요청")
    void saveAccount() throws JsonProcessingException {
        SignupResponse mockResponse = new SignupResponse("회원가입 성공");

        // mockWebServer 응답 설정
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(mapper.writeValueAsString(mockResponse))
            .addHeader("Content-Type", "application/json"));

        SignupRequest signupRequest = SignupRequest.builder()
            .email("valid-email@naver.com")
            .name("Any")
            .password("qpwoeiruty00@")
            .build();

        ResponseEntity<SignupResponse> result = dbServerApiClient.saveAccount(signupRequest);

        // Then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}