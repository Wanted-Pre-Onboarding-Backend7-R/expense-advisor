package com.example.expenseadvisor.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 사용자의 요청으로부터 JWT를 검사하여 그에 상응하는 Authentication 객체를 SecurityContextFilter에 세팅하는 필터
 * TODO: GenericFilterBean vs OncePerRequest 스터디 후 둘 중 적합한 것으로 바꾸기
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    /**
     * JWT로부터 Authentication 정보를 만들고 SecurityContextHolder에 저장한다.
     * JWT가 존재하지 않거나 유효한 JWT가 아니라면 아무것도 하지않고 다음 필터를 실행시킨다.
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        String jwt = resolveToken(httpServletRequest);

        Authentication authentication = jwtProvider.createAuthentication(jwt);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    /**
     * HttpservletRequest로부터 JWT를 추출하여 리턴한다.
     *
     * @param request HttpServletRequest
     * @return JWT, 또는 JWT가 유효한 존재하지 않는다면 null
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String BEARER_PREFIX = "Bearer" + " ";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

}
