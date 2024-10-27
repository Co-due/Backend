package soma.edupiuser.oauth.handler;

import static soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import soma.edupiuser.oauth.service.OAuth2AccountService;
import soma.edupiuser.oauth.service.OAuth2UserPrincipal;
import soma.edupiuser.oauth.utils.CookieUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2AccountService oAuth2AccountService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        try {
            String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(getDefaultTargetUrl());
            OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

            if (principal == null) {
                redirectWithError(request, response, targetUrl);
                return;
            }

            handleMode(request, response, principal);

            if (response.isCommitted()) {
                logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                return;
            }

            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            log.error("OAuth2 Success Handler 에러 발생", e);
            throw e;
        }
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String targetUrl)
        throws IOException {
        String errorUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", "Login failed")
            .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, errorUrl);
    }

    private void handleMode(HttpServletRequest request, HttpServletResponse response, OAuth2UserPrincipal principal) {
        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse("");

        if ("login".equalsIgnoreCase(mode)) {
            oAuth2AccountService.handleLogin(principal, response);

        } else if ("unlink".equalsIgnoreCase(mode)) {
            oAuth2AccountService.handleUnlink(principal);
        }
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}