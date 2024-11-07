package soma.edupiuser.oauth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import soma.edupiuser.oauth.service.OAuth2AccountService;
import soma.edupiuser.oauth.service.OAuth2UserPrincipal;
import soma.edupiuser.web.utils.CookieUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final String MODE_PARAM_COOKIE_NAME = "mode";

    private final OAuth2AccountService oAuth2AccountService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Value("${site-url.base}")
    private String baseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse(getDefaultTargetUrl());
        OAuth2UserPrincipal principal = oAuth2AccountService.getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            redirectWithError(request, response, targetUrl);
            return;
        }

        signupAndLogin(request, response, principal);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String targetUrl)
        throws IOException {
        String errorUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", "Login failed")
            .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, errorUrl);
    }

    private void signupAndLogin(HttpServletRequest request, HttpServletResponse response,
        OAuth2UserPrincipal principal) {
        oAuth2AccountService.signupAndLogin(principal, response);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}