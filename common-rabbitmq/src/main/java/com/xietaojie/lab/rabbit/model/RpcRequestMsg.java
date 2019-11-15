package com.xietaojie.lab.rabbit.model;

import java.io.Serializable;

/**
 * @author xietaojie1992
 */
public class RpcRequestMsg<T> implements Serializable {

    private static final long serialVersionUID = 4759826582562893L;

    private String msgType;

    private String operation;

    private Long timestamp;

    private T entity;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "RpcRequestMsg{" + "msgType='" + msgType + '\'' + ", operation='" + operation + '\'' + ", timestamp=" + timestamp
                + ", entity=" + entity + '}';
    }
}
