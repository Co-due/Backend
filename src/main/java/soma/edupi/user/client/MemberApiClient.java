package soma.edupi.user.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import soma.edupi.user.domain.Member;
import soma.edupi.user.dto.request.EmailRequest;
import soma.edupi.user.dto.request.MemberLoginRequest;
import soma.edupi.user.dto.request.SignupRequest;
import soma.edupi.user.dto.response.SignupResponse;

@Component
@HttpExchange("/v1/member")
public interface MemberApiClient {

    @PostExchange("/findByEmailAndPassword")
    Member findMemberByEmailAndPassword(@RequestBody MemberLoginRequest memberLoginRequest);

    @PostExchange("/signup")
    ResponseEntity<SignupResponse> saveMember(@RequestBody SignupRequest signupRequest);

    @PostExchange("/activate")
    void activate(@RequestBody EmailRequest emailRequest);

}
