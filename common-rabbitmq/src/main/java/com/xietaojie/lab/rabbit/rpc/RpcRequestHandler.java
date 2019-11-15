package com.xietaojie.lab.rabbit.rpc;

import com.xietaojie.lab.rabbit.model.RpcReplyMsg;
import com.xietaojie.lab.rabbit.model.RpcRequestMsg;

/**
 * @author xietaojie1992
 */
public interface RpcRequestHandler {

    RpcReplyMsg handle(RpcRequestMsg request);

}