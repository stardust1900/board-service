package com.allinpay.data.board.config;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.allinpay.data.board.model.UserDetail;
import com.allinpay.data.board.utils.JwtUtils;


/**
 * token校验
 * @author shawn
 *
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// 令牌自定义标识
	@Value("${jwt.header}")
	private String tokenHeader;

	@Resource
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String auth_token = request.getHeader(this.tokenHeader);
		
		logger.debug("auth_token {}", auth_token);
		
		final String auth_token_start = "Bearer ";
		if (StringUtils.isNotEmpty(auth_token) && auth_token.startsWith(auth_token_start)) {
			auth_token = auth_token.substring(auth_token_start.length());
		} else {
			// 不按规范,不允许通过验证
			auth_token = null;
		}
		String username = jwtUtils.getUsernameFromToken(auth_token);

		//logger.info("Checking authentication for userDetail {}", username);

		if (jwtUtils.containToken(username, auth_token) && username != null
				&& SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetail userDetail = jwtUtils.getUserFromToken(auth_token);
			if (jwtUtils.validateToken(auth_token, userDetail)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//logger.info("Authenticated userDetail {}, setting security context",username);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}

}
