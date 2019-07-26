package com.yumore.common.reflect;


/**
 * @author Nathaniel
 */
public class RxException extends Exception {

    private RxError rxError = new RxError();

    public RxException(int code) {
        rxError.setCode(code);
        rxError.setMessage(getErrorMsg(code));
    }

    public RxError getRxError() {
        return rxError;
    }


    private String getErrorMsg(int code) {
        String messgage = "";
        if (code == 999) {
            messgage = "无数据";
        } else if (code == 1007) {
            messgage = "账号或密码错误，请重新填写";
        } else if (code == 1006) {
            messgage = "账号不存在";
        } else if (code == 1000) {
            messgage = "鉴权失败";
        } else if (code == 1001) {
            messgage = "数据库异常";
        } else if (code == 1002) {
            messgage = "客户端数据异常";
        } else if (code == 1007) {
            messgage = "密码错误";
        } else if (code == 8000) {
            messgage = "后台维护";
        } else if (code == 1020) {
            messgage = "强制下线";
        } else if (code == 9000) {
            messgage = "其他错误";
        }
        return messgage;
    }

}
