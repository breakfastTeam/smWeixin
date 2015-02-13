package com.sm.controller;

import com.github.sd4324530.fastweixin.handle.EventHandle;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.req.BaseEvent;

/**
 * Created by Ez丶kkk on 15/2/5.
 */
public class MyEventHandle implements EventHandle {

    @Override
    public BaseMsg handle(BaseEvent baseEvent) {
        //点击按钮【关于】
        if (baseEvent.getEvent().equals("KEY_ABOUT")) {

        }
        return null;
    }

    @Override
    public boolean beforeHandle(BaseEvent baseEvent) {

        return false;
    }
}
