package com.sm.controller;

import com.github.sd4324530.fastweixin.handle.MessageHandle;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.req.BaseReqMsg;

/**
 * Created by Ez丶kkk on 15/2/5.
 */
public class MyMessageHandle implements MessageHandle {
    @Override
    public BaseMsg handle(BaseReqMsg baseReqMsg) {
        return null;
    }

    @Override
    public boolean beforeHandle(BaseReqMsg baseReqMsg) {
        return false;
    }
}
