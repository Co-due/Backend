package soma.edupiuser.web.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@Component
@HttpExchange
public interface GoogleOAuth2ApiClient {

    @PostExchange("/revoke")
    void unlink(@RequestParam("token") String accessToken);
}