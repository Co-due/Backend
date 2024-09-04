package soma.haeya.edupi_user.client;


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
import soma.haeya.edupi_user.client.config.MemberApiRestClientConfig;
import soma.haeya.edupi_user.domain.Member;
import soma.haeya.edupi_user.domain.Role;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignUpRequest;
import soma.haeya.edupi_user.dto.response.Response;
import soma.haeya.edupi_user.dto.response.SignUpResponse;

@SpringBootTest
@Import(MemberApiRestClientConfig.class)
class MemberApiClientTest {

    @Autowired
    private ObjectMapper mapper;

    private MockWebServer mockWebServer;
    private MemberApiClient memberApiClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // memberApiClient에 mockWebServer를 적용해 테스트
        MemberApiRestClientConfig config = new MemberApiRestClientConfig();
        memberApiClient = config.memberApiClient(
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
        Member expectedResponse = new Member(1L, "asdf@naver.com", "", "홍길동", Role.ROLE_USER);

        // mockWebServer 응답 설정
        mockWebServer.enqueue(new MockResponse()
            .setBody(mapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));

        MemberLoginRequest request = MemberLoginRequest.builder()
            .email("asdf@naver.com")
            .password("password").build();

        Member result = memberApiClient.findMemberByEmailAndPassword(request);

        Assertions.assertThat(result.getEmail()).isEqualTo(expectedResponse.getEmail());
        Assertions.assertThat(result.getName()).isEqualTo(expectedResponse.getName());
    }

    @Test
    @DisplayName("회원 가입 요청")
    void saveMember() throws JsonProcessingException {
        Response mockResponse = new Response("회원가입 성공");

        // mockWebServer 응답 설정
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(mapper.writeValueAsString(mockResponse))
            .addHeader("Content-Type", "application/json"));

        SignUpRequest signupRequest = SignUpRequest.builder()
            .email("valid-email@naver.com")
            .name("Any")
            .password("qpwoeiruty00@")
            .build();

        ResponseEntity<SignUpResponse> result = memberApiClient.saveMember(signupRequest);

        // Then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}