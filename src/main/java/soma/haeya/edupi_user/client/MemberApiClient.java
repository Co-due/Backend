package soma.haeya.edupi_user.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import soma.haeya.edupi_user.domain.Member;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignupRequest;
import soma.haeya.edupi_user.dto.response.Response;

@Component
@HttpExchange("/member")
public interface MemberApiClient {

    @PostExchange("/findByEmailAndPassword")
    Member findMemberByEmailAndPassword(@RequestBody MemberLoginRequest memberLoginRequest);

    @PostExchange("/signup")
    ResponseEntity<Response> saveMember(@RequestBody SignupRequest signupRequest);
}
