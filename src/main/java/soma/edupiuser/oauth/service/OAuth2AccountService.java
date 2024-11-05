package soma.edupiuser.oauth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.oauth.exception.OAuth2AuthenticationProcessingException;
import soma.edupiuser.oauth.models.OAuth2Provider;
import soma.edupiuser.oauth.models.OAuth2UserUnlinkManager;
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
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

    public OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    public void handleLogin(OAuth2UserPrincipal principal, HttpServletResponse response) {
        String email = principal.getUserInfo().getEmail();
        String name = principal.getUserInfo().getName();
        String provider = principal.getUserInfo().getProvider().name().toLowerCase();

        if (!metaServerApiClient.isExistsEmailByProvider(email, provider)) {
            if (metaServerApiClient.isExistsEmail(email)) {
                throw new OAuth2AuthenticationProcessingException(ErrorEnum.OAUTH2_EXCEPTION.getDetail());
            }
            log.info("handleLogin - signup, email={}", email);
            // DB에 회원 저장
            metaServerApiClient.saveAccountWithOauth(SignupOAuthRequest.builder()
                .email(email)
                .name(name)
                .provider(provider)
                .build());
        }
        log.info("handleLogin - login, email={}", email);
        Account account = metaServerApiClient.oauthLogin(new EmailRequest(email));
        String token = tokenProvider.generateToken(account);

        CookieUtils.addCookie(response, "token", token, 60 * 60);
    }

    public void handleUnlink(OAuth2UserPrincipal principal) {
        if (principal == null) {
            return;
        }
        log.info("handleUnlink - userInfo={}", principal.getUserInfo());
        String accessToken = principal.getUserInfo().getAccessToken();
        OAuth2Provider provider = principal.getUserInfo().getProvider();
        oAuth2UserUnlinkManager.unlink(provider, accessToken);
    }


}