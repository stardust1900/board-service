package com.allinpay.data.board.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.allinpay.data.board.utils.ResultCode;
import com.allinpay.data.board.utils.ResultJson;

/**
 * 认证失败处理类，返回401
 * 
 * @author shawn
 *
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -8141578635231868421L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		logger.debug("认证失败 {} ", authException.getMessage());
		// 因为我们使用的REST API,所以我们认为到达后台的请求都是正常的，
		// 所以返回的HTTP状态码都是200，用接口返回的code来确定请求是否正常。
		response.setStatus(ResultCode.SUCCESS.getCode());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		//响应中增加允许跨域，解决认证失败是的跨域问题
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,Authorization,SessionToken,JSESSIONID,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");

		PrintWriter printWriter = response.getWriter();
		String body = ResultJson.failure(ResultCode.UNAUTHORIZED, authException.getMessage()).toString();
		printWriter.write(body);
		printWriter.flush();
	}

}
