package com.allinpay.data.board.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {

	private Long roleId;
    private String roleKey;
    private String roleName;
}
