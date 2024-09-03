package soma.haeya.edupi_user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import soma.haeya.edupi_user.auth.TokenProvider;
import soma.haeya.edupi_user.client.MemberApiClient;
import soma.haeya.edupi_user.domain.Member;
import soma.haeya.edupi_user.dto.TokenInfo;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignupRequest;
import soma.haeya.edupi_user.dto.response.Response;
import soma.haeya.edupi_user.exception.DbValidException;
import soma.haeya.edupi_user.exception.UnexpectedServerException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberApiClient memberApiClient;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public String login(MemberLoginRequest memberLoginRequest) {
        Member findMember = memberApiClient.findMemberByEmailAndPassword(memberLoginRequest);

        return tokenProvider.generateToken(findMember);
    }

    public ResponseEntity<Response> signUp(SignupRequest signupRequest) throws JsonProcessingException {
        try {

            // 회원가입 요청을 처리합니다.
            ResponseEntity<Response> responseEntity = memberApiClient.saveMember(signupRequest);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseEntity.getBody());

        } catch (HttpClientErrorException e) {
            Response response = objectMapper.readValue(e.getResponseBodyAsString(), Response.class);

            if (e.getStatusCode().is4xxClientError() || e.getStatusCode().is5xxServerError()) {
                throw new DbValidException(response.message());
            } else {
                throw new UnexpectedServerException("회원가입 중 예상치 못한 에러 발생 : " + e.getMessage());
            }
        }
    }

    public TokenInfo findMemberInfo(String token) {
        return tokenProvider.findUserInfoBy(token);
    }


}
