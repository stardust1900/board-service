package com.allinpay.data.board.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.allinpay.data.board.dao.AuthMapper;
import com.allinpay.data.board.model.Role;
import com.allinpay.data.board.model.UserDetail;

/**
 * 登陆身份认证
 * 
 * @author shawn
 *
 */
@Component(value = "CustomUserDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	private final AuthMapper authMapper;

	public CustomUserDetailsServiceImpl(AuthMapper authMapper) {
		this.authMapper = authMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetail userDetail = authMapper.findByUsername(username);
		if (userDetail == null) {
			throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", username));
		}
		Role role = authMapper.findRoleByUserId(userDetail.getId());
        userDetail.setRole(role);
        return userDetail;
	}

}
