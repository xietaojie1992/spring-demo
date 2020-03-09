package com.xietaojie.lab.rabbit.mq;

import com.xietaojie.lab.rabbit.model.Msg;

/**
 * 消息监听器，向 MessageConsumer 注册监听器
 *
 * @author xietaojie1992
 */
public interface MessageListener {

    void onMessage(Msg msg);

}
