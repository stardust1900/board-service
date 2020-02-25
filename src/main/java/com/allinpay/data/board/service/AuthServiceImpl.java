package com.allinpay.data.board.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.allinpay.data.board.dao.AuthMapper;
import com.allinpay.data.board.exception.CustomException;
import com.allinpay.data.board.model.ResponseUserToken;
import com.allinpay.data.board.model.Role;
import com.allinpay.data.board.model.UserDetail;
import com.allinpay.data.board.utils.JwtUtils;
import com.allinpay.data.board.utils.ResultCode;
import com.allinpay.data.board.utils.ResultJson;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtUtils jwtTokenUtil;
	private final AuthMapper authMapper;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	public AuthServiceImpl(AuthenticationManager authenticationManager,
			@Qualifier("CustomUserDetailsService") UserDetailsService userDetailsService, JwtUtils jwtTokenUtil,
			AuthMapper authMapper) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.authMapper = authMapper;
	}

	@Override
	public UserDetail register(UserDetail userDetail) {
		final String username = userDetail.getUsername();
        if(authMapper.findByUsername(username)!=null) {
            throw new CustomException(ResultJson.failure(ResultCode.BAD_REQUEST, "用户已存在"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userDetail.getPassword();
        userDetail.setPassword(encoder.encode(rawPassword));
        userDetail.setLastPasswordResetDate(new Date());
        authMapper.insert(userDetail);
        long roleId = userDetail.getRole().getRoleId();
        Role role = authMapper.findRoleById(roleId);
        userDetail.setRole(role);
        authMapper.insertRole(userDetail.getId(), roleId);
        return userDetail;
	}

	@Override
	public ResponseUserToken login(String username, String password) {
		//用户验证
        final Authentication authentication = authenticate(username, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        //存储token
        jwtTokenUtil.putToken(username, token);
        return new ResponseUserToken(token, userDetail);
	}
	
	private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new CustomException(ResultJson.failure(ResultCode.LOGIN_ERROR, e.getMessage()));
        }
    }

	@Override
	public void logout(String token) {
		token = token.substring(tokenHead.length());
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        jwtTokenUtil.deleteToken(userName);

	}

	@Override
	public ResponseUserToken refresh(String oldToken) {
		String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetail userDetail = (UserDetail) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, userDetail.getLastPasswordResetDate())){
            token =  jwtTokenUtil.refreshToken(token);
            return new ResponseUserToken(token, userDetail);
        }
        return null;
	}

	@Override
	public UserDetail getUserByToken(String token) {
		token = token.substring(tokenHead.length());
        return jwtTokenUtil.getUserFromToken(token);
	}

}
