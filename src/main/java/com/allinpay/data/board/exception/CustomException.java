package com.allinpay.data.board.exception;

import com.allinpay.data.board.utils.ResultJson;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 3066235133493104130L;
	private ResultJson<?> resultJson;
	
	public CustomException(ResultJson<?> resultJson) {
        this.resultJson = resultJson;
    }
}
