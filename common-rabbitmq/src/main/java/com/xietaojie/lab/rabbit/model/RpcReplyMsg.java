package com.xietaojie.lab.rabbit.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author xietaojie1992
 */
@Data
public class RpcReplyMsg<T> implements Serializable {

    private static final long serialVersionUID = 4742534534082754844L;

    private String id;

    private boolean success = false;

    private String resultCode = "uninitializedCode";

    private String resultMsg = "uninitializedMsg";

    private T obj = null;

    public void copy(RpcReplyMsg<T> other) {
        this.withSuccess(other.isSuccess()).withResultCode(other.getResultCode()).withResultMsg(other.getResultMsg()).withResultObj(
                other.getObj());
    }

    public RpcReplyMsg<T> succeed() {
        this.success = true;
        if (StringUtils.isBlank(this.resultCode)) {
            this.resultCode = "SUCCESS";
        }
        if (StringUtils.isBlank(this.resultMsg)) {
            this.resultMsg = "Operation Success.";
        }
        return this;
    }

    public RpcReplyMsg<T> withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public RpcReplyMsg<T> withResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public RpcReplyMsg<T> withResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
        return this;
    }

    public RpcReplyMsg<T> withResultObj(T t) {
        this.obj = t;
        return this;
    }

    public RpcReplyMsg<T> fail(RpcReplyMsg other) {
        this.success = false;
        this.resultCode = other.getResultCode();
        this.resultMsg = other.getResultMsg();
        return this;
    }

    public RpcReplyMsg<T> fail(String resultCode, String resultMsg) {
        this.success = false;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        return this;
    }
}
