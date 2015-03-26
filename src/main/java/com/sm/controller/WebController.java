package com.sm.controller;

import com.github.sd4324530.fastweixin.api.OauthAPI;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.sm.entity.Category;
import com.sm.entity.Consult;
import com.sm.entity.Resource;
import com.sm.entity.Video;
import com.sm.repository.CategoryRepository;
import com.sm.repository.ConsultRepository;
import com.sm.repository.ResourceRepository;
import com.sm.repository.VideoRepository;
import com.sm.util.JsonResult;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Autowired
    private ConsultRepository consultRepository;
    @Autowired
    OauthAPI oauthAPI;
    @Autowired
    private JavaMailSenderImpl mailSender;
    private String subject = "方案咨询提醒邮件";
    @Value("#{configProperties['USER_NAME']}")
    private String USER_NAME;
    @RequestMapping(value={"/","/about"})
    public String index() {
        return "about";
    }

    /**
     * 解决方案、成功案例、产品DEMO
     * @param categoryType (schemes;cases;demos)
     * @return resourcesList.html
     */
    @RequestMapping("resourceList")
    public ModelAndView resourceList(@RequestParam String categoryType) {
        ModelAndView mv = new ModelAndView();
        List<Category> categorys = categoryRepository.findByType(categoryType);

        if (!CollectionUtils.isEmpty(categorys)) {
            final Map<Long,String> cateMap = Maps.newHashMapWithExpectedSize(categorys.size());

            for (Category cate : categorys) {
                cateMap.put(cate.getId(), cate.getName());
            }

            Multimap<String, Resource> multiMap = ArrayListMultimap.create();
            List<Resource> resources = resourceRepository.findByCategoryIdIn(cateMap.keySet());
            for (Resource resource : resources) {
                multiMap.put(cateMap.get(resource.getCategoryId()), resource);
            }
            mv.addObject("resultMap", multiMap);
            String infoType = categorys.get(0).getType();
            if(infoType.equals("products")){
                mv.addObject("infoType","产品篇");
            }else if(infoType.equals("schemes")){
                mv.addObject("infoType","方案篇");
            }else if(infoType.equals("case")){
                mv.addObject("infoType","案例篇");
            }else if(infoType.equals("demos")){
                mv.addObject("infoType","Demo篇");
            }else{
                mv.addObject("infoType","未知");
            }
        }else{
            mv.addObject("infoType","未知");
        }
        mv.setViewName("resourcesList");

        return mv;
    }

    @RequestMapping("resource")
    public ModelAndView resource(@RequestParam Long resourceId) {
        Resource resource = resourceRepository.findOne(resourceId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("resource", resource);
        String refCase = resource.getRefCase();
        if (StringUtils.isNotBlank(refCase)) {
            String refCases[] = refCase.split(",");
            Set<Long> ids = Sets.newHashSetWithExpectedSize(refCases.length);
            for (String rcase : refCases) {
                ids.add(Long.valueOf(rcase));
            }
            List<Resource> resources = resourceRepository.findAll(ids);
            mv.addObject("refCases", resources);
        }
        String refDemo = resource.getRefDemo();
        if (StringUtils.isNotBlank(refDemo)) {
            String refDemos[] = refDemo.split(",");
            Set<Long> ids = Sets.newHashSetWithExpectedSize(refDemos.length);
            for (String demo : refDemos) {
                ids.add(Long.valueOf(demo));
            }
            List<Resource> resources = resourceRepository.findAll(ids);
            mv.addObject("refDemos", resources);
        }
                mv.setViewName("resource");
        return mv;
    }

    @RequestMapping("videoList")
    public ModelAndView videoList() {
        List<Video> videos = videoRepository.findAll();
        ModelAndView mv = new ModelAndView();
        mv.addObject("videos", videos);
        mv.setViewName("videoList");
        mv.addObject("infoType","视频篇");
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
        ModelAndView mv = new ModelAndView();
        List<Category> category = categoryRepository.findByType("products");
        if (!CollectionUtils.isEmpty(category)) {
            List<Resource> resources = resourceRepository.findByCategoryId(category.get(0).getId());
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
            mv.addObject("infoType","产品篇");
        }else{
            mv.addObject("infoType","案例篇");
        }
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

    @RequestMapping(value = "newConsult")
    public ModelAndView newConsult(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String openId = request.getParameter("openId");
        List<Category> categorys = categoryRepository.findByTypeIn(Lists.newArrayList("products", "schemes"));
        final Set<Long> cateSet = Sets.newHashSetWithExpectedSize(categorys.size());
        for (Category cate : categorys) {
            cateSet.add(cate.getId());
        }
        if (!CollectionUtils.isEmpty(cateSet)) {
            List<Resource> resources = resourceRepository.findByCategoryIdIn(cateSet);
            mv.addObject("resources", resources);
        }
        mv.addObject("openId", openId);
        mv.setViewName("newConsult");
        mv.addObject("infoType","咨询篇");
        return mv;
    }

    @RequestMapping(value = "newConsultPost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult newConsultPost(@ModelAttribute Consult consult) {
        JsonResult result = new JsonResult();
        consult.setCreated(DateTime.now());
        consult.setUpdated(DateTime.now());
        Consult con = consultRepository.save(consult);
        if (con.getId() != null && consult.getSchemeId() != null) {
            Resource resource = resourceRepository.findOne(consult.getSchemeId());
            try {
                _sendEmail(resource.getEmail(), consult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.setSuccess(null != con.getId());
        return result;
    }

    @RequestMapping(value = "viewConsult", method = RequestMethod.GET)
    public ModelAndView viewConsult(@RequestParam Long consultId) {
        Consult consult = consultRepository.findOne(consultId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("consult", consult);
        mv.setViewName("viewConsult");
        mv.addObject("infoType","咨询篇");
        return mv;
    }

    @RequestMapping(value = "consultList", method = RequestMethod.GET)
    public ModelAndView consultList(HttpServletRequest request) {
        String openId = request.getParameter("openId");
        openId = ObjectUtils.defaultIfNull(openId,"");
        List<Consult> consults = consultRepository.findByOpenId(openId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("consults", consults);
        mv.setViewName("consultsList");
        mv.addObject("infoType","咨询篇");
        return mv;
    }

    @Async
    private void _sendEmail(final String emailAddress,final Consult consult){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(USER_NAME);
        email.setTo(emailAddress);
        email.setSubject(subject);
        email.setText("你有一个咨询要处理：\n\t\t姓名：" + consult.getUserName() + "\n\t电话：" + consult.getMobile() + "\n\t邮箱："
                + consult.getEmail() + "\n\t方案名称：" + consult.getScheme() + "\n\t需求描述：" + consult.getIntro() + "\n\t 请到管理平台回复消息：http://productone.chinacloudapp.cn/weixin/manage/login" );
        mailSender.send(email);
    }
}
