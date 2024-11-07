package soma.edupiuser.oauth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.oauth.exception.OAuth2AuthenticationProcessingException;
import soma.edupiuser.oauth.models.SignupOAuthRequest;
import soma.edupiuser.web.auth.TokenProvider;
import soma.edupiuser.web.client.MetaServerApiClient;
import soma.edupiuser.web.exception.ErrorEnum;
import soma.edupiuser.web.utils.CookieUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2AccountService {

    private final MetaServerApiClient metaServerApiClient;
    private final TokenProvider tokenProvider;

    public OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    public void signupAndLogin(OAuth2UserPrincipal principal, HttpServletResponse response) {
        String email = principal.getUserInfo().getEmail();
        String name = principal.getUserInfo().getName();
        String provider = principal.getUserInfo().getProvider().name().toLowerCase();

        if (!metaServerApiClient.isExistsEmailByProvider(email, provider)) {
            if (metaServerApiClient.isExistsEmail(email)) {
                throw new OAuth2AuthenticationProcessingException(ErrorEnum.OAUTH2_EXCEPTION.getDetail());
            }
            signup(email, name, provider);
        }
        login(response, email);
    }

    private void signup(String email, String name, String provider) {
        log.info("handleLogin - signup, email={}", email);
        // DB에 회원 저장
        metaServerApiClient.saveAccountWithOauth(SignupOAuthRequest.builder()
            .email(email)
            .name(name)
            .provider(provider)
            .build());
    }

    private void login(HttpServletResponse response, String email) {
        log.info("handleLogin - login, email={}", email);
        Account account = metaServerApiClient.oauthLogin(new EmailRequest(email));
        String token = tokenProvider.generateToken(account);

        CookieUtils.addCookie(response, "token", token, 60 * 60);
    }
}