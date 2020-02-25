package com.allinpay.data.board.utils;


import java.io.Serializable;


import lombok.Data;

@Data
public class ResultJson<T> implements Serializable {

	private static final long serialVersionUID = 279790246206874897L;
	private int code;
    private String msg;
	private T data;

	public static ResultJson<?> ok() {
        return ok("");
    }

    public static ResultJson<?> ok(Object o) {
        return new ResultJson<Object>(ResultCode.SUCCESS, o);
    }

    public static ResultJson<?> failure(ResultCode code) {
        return failure(code, "");
    }

    public static ResultJson<?> failure(ResultCode code, Object o) {
        return new ResultJson<>(code, o);
    }
    
	public ResultJson (ResultCode resultCode) {
        setResultCode(resultCode);
    }

    public ResultJson (ResultCode resultCode,T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }
    
    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":\"" + msg + '\"' +
                ", \"data\":\"" + data + '\"'+
                '}';
    }
}
