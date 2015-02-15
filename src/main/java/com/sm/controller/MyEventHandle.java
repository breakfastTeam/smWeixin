package com.sm.controller;

import com.github.sd4324530.fastweixin.handle.EventHandle;
import com.github.sd4324530.fastweixin.message.Article;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.NewsMsg;
import com.github.sd4324530.fastweixin.message.TextMsg;
import com.github.sd4324530.fastweixin.message.req.BaseEvent;
import com.google.common.collect.Lists;
import com.sm.entity.Category;
import com.sm.entity.Video;
import com.sm.repository.CategoryRepository;
import com.sm.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Ez丶kkk on 15/2/5.
 */
public class MyEventHandle implements EventHandle {


    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Value("#{configProperties['host']}")
    private String host;

    @Override
    public BaseMsg handle(BaseEvent baseEvent) {
        String keyEvent = baseEvent.getEvent();
        //产品资料
        if (keyEvent.equals("products")) {
            List<Article> articles = Lists.newArrayList();
            List<Category> category = categoryRepository.findByType("products");
            if (!CollectionUtils.isEmpty(category)) {
                Article article = new Article();
                article.setPicUrl(category.get(0).getThumbnail());
                article.setTitle("产品资料");
                article.setUrl(host + "/web/productIntro");
                article.setDescription("点击查看更多产品资料");
                articles.add(article);
                return new NewsMsg(articles);
            }
        }else if(keyEvent.equals("schemes")||keyEvent.equals("cases")||keyEvent.equals("demos")){
            //解决方案、案例、demo
            List<Category> categorys = categoryRepository.findByType(keyEvent);
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

    @Override
    public boolean beforeHandle(BaseEvent baseEvent) {

        return false;
    }
}
