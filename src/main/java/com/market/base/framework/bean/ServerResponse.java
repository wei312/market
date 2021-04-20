package com.market.base.framework.bean;

public class ServerResponse<T> {

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public void setDetailErrMsg(String detailErrMsg) {
        this.detailErrMsg = detailErrMsg;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerResponse)) {
            return false;
        }
        ServerResponse<?> other = (ServerResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (getErrcode() != other.getErrcode()) {
            return false;
        }
        Object this$errmsg = getErrmsg(), other$errmsg = other.getErrmsg();
        if ((this$errmsg == null) ? (other$errmsg != null) : !this$errmsg.equals(other$errmsg)) {
            return false;
        }
        Object this$detailErrMsg = getDetailErrMsg(), other$detailErrMsg = other.getDetailErrMsg();
        if ((this$detailErrMsg == null) ? (other$detailErrMsg != null)
                : !this$detailErrMsg.equals(other$detailErrMsg)) {
            return false;
        }
        Object this$data = getData(), other$data = other.getData();
        if ((this$data == null) ? (other$data != null) : !this$data.equals(other$data)) {
            return false;
        }
        Object this$hint = getHint(), other$hint = other.getHint();
        return !((this$hint == null) ? (other$hint != null) : !this$hint.equals(other$hint));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerResponse;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + getErrcode();
        Object $errmsg = getErrmsg();
        result = result * 59 + (($errmsg == null) ? 43 : $errmsg.hashCode());
        Object $detailErrMsg = getDetailErrMsg();
        result = result * 59 + (($detailErrMsg == null) ? 43 : $detailErrMsg.hashCode());
        Object $data = getData();
        result = result * 59 + (($data == null) ? 43 : $data.hashCode());
        Object $hint = getHint();
        return result * 59 + (($hint == null) ? 43 : $hint.hashCode());
    }

    public String toString() {
        return "ServerResponse(errcode=" + getErrcode() + ", errmsg=" + getErrmsg() + ", detailErrMsg="
                + getDetailErrMsg() + ", data=" + getData() + ", hint=" + getHint() + ")";
    }


    private int errcode;

    private String errmsg;

    private String detailErrMsg;

    private T data;

    private String hint;

    public int getErrcode() {
        return this.errcode;
    }

    public String getErrmsg() {
        return this.errmsg;
    }

    public String getDetailErrMsg() {
        return this.detailErrMsg;
    }

    public String getHint() {
        return this.hint;
    }

    public ServerResponse() {
    }


    public ServerResponse(int errCode, String msg, String detailErrMsg) {
        this.errcode = errCode;
        this.errmsg = msg;
        this.detailErrMsg = detailErrMsg;
    }

    public ServerResponse(int errCode, String msg) {
        this(errCode, msg, "");
    }

    public ServerResponse(int errCode, String msg, String detailErrMsg, String hint) {
        this.errcode = errCode;
        this.errmsg = msg;
        this.detailErrMsg = detailErrMsg;
        this.hint = hint;
    }

    public ServerResponse(T data) {
        this.errcode = 0;
        this.errmsg = "";
        setData(data);
    }

    public ServerResponse<T> withCode(int code) {
        this.errcode = code;
        return this;
    }

    public ServerResponse<T> withMessage(String message) {
        this.errmsg = message;
        return this;
    }

    public ServerResponse<T> withData(T data) {
        this.data = data;
        return this;
    }


    public boolean success() {
        return (this.errcode == 0);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
