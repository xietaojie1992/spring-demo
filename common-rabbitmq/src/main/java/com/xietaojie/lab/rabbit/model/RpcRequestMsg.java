package com.xietaojie.lab.rabbit.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xietaojie1992
 */
@Data
public class RpcRequestMsg<T> implements Serializable {

    private static final long serialVersionUID = 4759826582562893L;

    private String id;

    private String msgType;

    private String operation;

    private Long timestamp;

    private T entity;
}
