package com.sm.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sm.entity.Category;
import com.sm.entity.Resource;
import com.sm.entity.Video;
import com.sm.repository.CategoryRepository;
import com.sm.repository.ResourceRepository;
import com.sm.repository.UserRepository;
import com.sm.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by Ez丶kkk on 15/2/7.
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VideoRepository videoRepository;

    @RequestMapping(value={"/","/about"})
    public String index() {
        return "about";
    }

    @RequestMapping("resourceList")
    public ModelAndView resourceList(@RequestParam Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        List resources = resourceRepository.findByCategoryId(category.getId());
        ModelAndView mv = new ModelAndView();
        mv.addObject("resources", resources);
        mv.addObject("category", category);
        mv.setViewName("resourcesList");
        return mv;
    }

    @RequestMapping("resource")
    public ModelAndView resource(@RequestParam Long resourceId) {
        Resource resource = resourceRepository.findOne(resourceId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("resource", resource);
        mv.setViewName("resource");
        return mv;
    }

    @RequestMapping("videoList")
    public ModelAndView videoList() {
        List<Video> videos = videoRepository.findAll();
        ModelAndView mv = new ModelAndView();
        mv.addObject("videos", videos);
        mv.setViewName("videoList");
        return mv;
    }

    @RequestMapping("video")
    public ModelAndView video(@RequestParam Long videoId) {
        Video video = videoRepository.findOne(videoId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("video", video);
        mv.setViewName("video");
        return mv;
    }

    @RequestMapping("productIntro")
    public ModelAndView productIntro() {
        Category category = categoryRepository.findOneByType("products");
        List<Resource> resources = resourceRepository.findByCategoryId(category.getId());
        ModelAndView mv = new ModelAndView();
        List<Resource> temp = null;
        Map<String,List<Resource>> result = Maps.newHashMapWithExpectedSize(resources.size());
        for (Resource resource : resources) {
            String categoryName = resource.getCategoryName();
            if (result.containsKey(categoryName)) {
                temp = result.get(categoryName);
                temp.add(resource);
            }else{
                temp = Lists.newArrayListWithExpectedSize(1);
                temp.add(resource);
                result.put(categoryName,temp);
            }
        }
        mv.addObject("resultMap", result);
        mv.setViewName("productIntro");
        return mv;
    }

    @RequestMapping("product")
    public ModelAndView product(@RequestParam Long resourceId) {
        Resource resource = resourceRepository.findOne(resourceId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("resource", resource);
        mv.setViewName("product");
        return mv;
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
