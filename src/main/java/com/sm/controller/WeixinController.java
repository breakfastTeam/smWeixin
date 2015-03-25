package com.sm.controller;

import com.github.sd4324530.fastweixin.message.Article;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.NewsMsg;
import com.github.sd4324530.fastweixin.message.TextMsg;
import com.github.sd4324530.fastweixin.message.req.MenuEvent;
import com.google.common.collect.Lists;
import com.sm.entity.Category;
import com.sm.entity.Resource;
import com.sm.entity.Video;
import com.sm.repository.CategoryRepository;
import com.sm.repository.ResourceRepository;
import com.sm.repository.VideoRepository;
import com.sm.util.SmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/weixin")
public class WeixinController extends WeixinControllerSupport{

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Value("#{configProperties['host']}")
    private String host;

    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);
    private static final String TOKEN = "smweixin";
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
    protected BaseMsg handleMenuClickEvent(MenuEvent event) {
        String keyEvent = event.getEventKey();
        //产品资料
        if (keyEvent.equals("products")) {
            List<Article> articles = Lists.newArrayList();
            List<Category> category = categoryRepository.findByType("products");
            if (!CollectionUtils.isEmpty(category)) {
                List<Resource> resources = resourceRepository.findByCategoryId(category.get(0).getId());
                if(!CollectionUtils.isEmpty(resources)){
                    Article article = new Article();
                    article.setPicUrl(resources.get(0).getThumbnail());
                    article.setTitle("产品资料");
                    article.setUrl(host + "/web/productIntro");
                    article.setDescription("点击查看更多产品资料");
                    articles.add(article);
                    return new NewsMsg(articles);
                }
            }
        }else if(keyEvent.equals("schemes")||keyEvent.equals("cases")||keyEvent.equals("demos")) {
            //解决方案、案例、demo
            String name = "";
            if(keyEvent.equals("schemes")){
                name = "解决方案";
            }else if(keyEvent.equals("cases")){
                name = "案例";
            }else{
                name = "demo";
            }
            List<Category> categorys = categoryRepository.findByType(keyEvent);
            List<Article> articles = Lists.newArrayList();
            Article article = null;
            if(!CollectionUtils.isEmpty(categorys)) {
                Category category = categorys.get(0);
                article = new Article();
                article.setPicUrl(category.getThumbnail());
                article.setTitle(name);
                article.setUrl(host + "/web/resourceList?categoryType=" + keyEvent);
                article.setDescription("点击查看更多" + name);
                articles.add(article);
                return new NewsMsg(articles);
            }
        }else if(keyEvent.equals("consult")){
            Article article = new Article();
            article.setPicUrl(host + "/asset/imgs/zixunlist.jpg");
            article.setTitle("我要咨询");
            article.setUrl(host + "/web/newConsult?openId=" + event.getFromUserName());
            article.setDescription("点击去提交你的咨询");
            return new NewsMsg(Lists.newArrayList(article));
        }else if(keyEvent.equals("consults")){
            Article article = new Article();
            article.setPicUrl(host + "/asset/imgs/zixun.jpg");
            article.setTitle("我要咨询");
            article.setUrl(host + "/web/consultList?openId=" + event.getFromUserName());
            article.setDescription("查看自己的历史咨询记录");
            return new NewsMsg(Lists.newArrayList(article));
        }else if(keyEvent.equals("videos")){
            List<Video> videos = videoRepository.findAll();
            List<Article> articles = Lists.newArrayList();
            Article article = null;
            Video video = videos.get(0);
            if (video != null) {
                article = new Article();
                article.setPicUrl(video.getLogo());
                article.setTitle(video.getName());
                article.setUrl(host + "/web/videoList");
                article.setDescription("点击查看更多在线视频");
                articles.add(article);
            }
            return new NewsMsg(articles);
        }else if(keyEvent.equals("about")){

            return new TextMsg("关于").addLink("点击跳转", host+"/web/about");
        }
        return new TextMsg("服务器回复用户消息!");
    }

}