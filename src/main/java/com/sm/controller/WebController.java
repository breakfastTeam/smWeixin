package com.sm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ez丶kkk on 15/2/7.
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @RequestMapping(value={"/","/about"})
    public String index() {
        return "about";
    }

    @RequestMapping("resourceList")
    public String resourceList() {
        return "resourcesList";
    }

    @RequestMapping("resource")
    public String resource() {
        return "resource";
    }

    @RequestMapping("videoList")
    public String videoList() {
        return "videoList";
    }

    @RequestMapping("video")
    public String video() {
        return "video";
    }

    @RequestMapping("productIntro")
    public String productIntro() {
        return "productIntro";
    }

    @RequestMapping("product")
    public String product() {
        return "product";
    }

    @RequestMapping("newsList")
    public String newsList() {
        return "newsList";
    }

    @RequestMapping("news")
    public String news() {
        return "news";
    }

}
