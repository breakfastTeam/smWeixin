package com.sm.controller;

import com.github.sd4324530.fastweixin.handle.EventHandle;
import com.github.sd4324530.fastweixin.handle.MessageHandle;
import com.github.sd4324530.fastweixin.message.Article;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.NewsMsg;
import com.github.sd4324530.fastweixin.message.TextMsg;
import com.github.sd4324530.fastweixin.message.req.TextReqMsg;
import com.github.sd4324530.fastweixin.servlet.WeixinControllerSupport;
import com.google.common.collect.Lists;
import com.sm.entity.Category;
import com.sm.entity.Resource;
import com.sm.repository.CategoryRepository;
import com.sm.repository.ResourceRepository;
import com.sm.util.SmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/weixin")
public class WeixinController extends WeixinControllerSupport {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("#{configProperties['host']}")
    private String host;

    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);
    private static final String TOKEN = "myToken";
    //设置TOKEN，用于绑定微信服务器
    @Override
    protected String getToken() {
        return TOKEN;
    }
    //使用安全模式时设置：APPID
    @Override
    protected String getAppId() {
        return SmUtils.APP_ID;
    }
    //使用安全模式时设置：密钥
    @Override
    protected String getAESKey() {
        return SmUtils.APP_AESKEY;
    }
    //重写父类方法，处理对应的微信消息
    @Override
    protected BaseMsg handleTextMsg(TextReqMsg msg) {
        String content = msg.getContent();
        log.debug("用户发送到服务器的内容:{}", content);
        //产品资料
        if (content.equals("products")) {
            List<Article> articles = Lists.newArrayList();
            Category category = categoryRepository.findOneByType("products");
            List<Resource> resources = resourceRepository.findByCategoryId(category.getId());
            Article article = null;
            for (Resource resource : resources) {
                article = new Article();
                article.setPicUrl(resource.getThumbnail());
                article.setTitle(resource.getName());
                article.setDescription(resource.getIntro());
                article.setUrl(host+"/web/product?resourceId="+resource.getId());
                articles.add(article);
            }
            return new NewsMsg(articles);
        }else if(content.equals("schemes")||content.equals("cases")||content.equals("demos")||content.equals("videos")){
            //解决方案、案例、demo
            List<Category> categorys = categoryRepository.findByType(content);
            List<Article> articles = Lists.newArrayList();
            Article article = null;
            for(Category category:categorys){
                article = new Article();
                article.setPicUrl(category.getThumbnail());
                article.setTitle(category.getName());
                article.setUrl(host+"/web/resourceList?categoryId="+category.getId());
                articles.add(article);
            }
            return new NewsMsg(articles);
        }
        return new TextMsg("服务器回复用户消息!");
    }
    /*1.1版本新增，重写父类方法，加入自定义微信消息处理器
     *不是必须的，上面的方法是统一处理所有的文本消息，如果业务觉复杂，上面的会显得比较乱
     *这个机制就是为了应对这种情况，每个MessageHandle就是一个业务，只处理指定的那部分消息
     */
    @Override
    protected List<MessageHandle> initMessageHandles() {
        List<MessageHandle> handles = new ArrayList<MessageHandle>();
        handles.add(new MyMessageHandle());
        return handles;
    }
    //1.1版本新增，重写父类方法，加入自定义微信事件处理器，同上
    @Override
    protected List<EventHandle> initEventHandles() {
        List<EventHandle> handles = new ArrayList<EventHandle>();
        handles.add(new MyEventHandle());
        return handles;
    }
}