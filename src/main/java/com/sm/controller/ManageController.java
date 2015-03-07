package com.sm.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sm.entity.Category;
import com.sm.entity.Resource;
import com.sm.entity.User;
import com.sm.entity.Video;
import com.sm.repository.CategoryRepository;
import com.sm.repository.ResourceRepository;
import com.sm.repository.UserRepository;
import com.sm.repository.VideoRepository;
import com.sm.util.CommonUtils;
import com.sm.util.JsonResult;
import com.sm.util.SmUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Ez丶kkk on 15/2/8.
 */
@Controller
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Value("#{configProperties['host']}")
    private String host;
    private String separator = "/";

    @RequestMapping(value = {"/","/login"}, method= RequestMethod.GET)
    public String login() {
        return "manage/login";
    }

    @RequestMapping(value = "/toLogin", method= RequestMethod.POST)
    @ResponseBody
    public JsonResult loginPost(@ModelAttribute User user,HttpSession session){
        JsonResult jsonResult = new JsonResult();
        String passwordMd5= DigestUtils.md5Hex(user.getPassword());
        User result =userRepository.findByLoginNameAndPassword(user.getLoginName(),passwordMd5);
        if (result != null) {
            session.setAttribute(SmUtils.SEESION_USER_ID,result.getId());
        }
        jsonResult.setSuccess(result!=null);
        return jsonResult;
    }

    @RequestMapping("index")
    public String index() {
        return "manage/index";
    }

    @RequestMapping("products")
    public ModelAndView products(@RequestParam String category) {
        ModelAndView mv = new ModelAndView();
        List<Category> category1 = categoryRepository.findByType(category);
        if (category1 != null && category1.size()>0) {
            mv.addObject("categoryId",category1.get(0).getId());
        }else{
            DateTime now = DateTime.now();
            Category c = new Category();
            c.setName("产品");
            c.setLogo("");
            c.setThumbnail("");
            c.setType(category);
            c.setCreated(now);
            c.setUpdated(now);
            c = categoryRepository.saveAndFlush(c);
            mv.addObject("categoryId",c.getId());
        }
        mv.setViewName("manage/products");
        return mv;
    }

    @RequestMapping(value = {"/product/view","/product/add"})
    public ModelAndView productsOpt(@RequestParam(required = false) Long resourceId,
                                    @RequestParam(required = false) Long categoryId) {
        ModelAndView mv = new ModelAndView();
        if (resourceId != null) {
            Resource resource = resourceRepository.findOne(resourceId);
            mv.addObject("resource", resource);
            mv.addObject("categoryId", resource.getCategoryId());
        }
        if (categoryId != null) {
            mv.addObject("categoryId", categoryId);
        }
        mv.setViewName("manage/productsAdd");
        return mv;
    }

    @RequestMapping("product/delete")
    @ResponseBody
    public JsonResult resourceTypeDel(@RequestParam Long resourceId) {
        JsonResult jsonResult = new JsonResult();
        resourceRepository.delete(resourceId);
        jsonResult.setSuccess(true);
        return jsonResult;
    }

    @RequestMapping("/product/save")
    @ResponseBody
    public JsonResult productsSave(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Resource resource = new Resource();

        String idStr = request.getParameter("id");
        Long id = 0L;
        if (StringUtils.isNotBlank(idStr) && StringUtils.isNumeric(idStr)) {
            id = Long.valueOf(idStr);
            resource = resourceRepository.findOne(id);
        }
        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            MultipartFile banner = multiRequest.getFile("banner");
            if (banner != null) {
                String bannerPath = this._saveFile(banner, request);
                resource.setBanner(bannerPath);
            }
            MultipartFile thumbnail = multiRequest.getFile("thumbnail");
            if (thumbnail != null) {
                String thumbnailPath = this._saveFile(thumbnail, request);
                resource.setThumbnail(thumbnailPath);
            }
        }
        String categoryId = request.getParameter("categoryId");
        Long cid = 0L;
        if (StringUtils.isNotBlank(categoryId) && StringUtils.isNumeric(categoryId)) {
            cid = Long.valueOf(categoryId);
        }
        resource.setCategoryId(cid);
        resource.setCategoryName(request.getParameter("categoryName"));
        resource.setName(request.getParameter("name"));
        resource.setIntro(request.getParameter("intro"));
        resource.setContent(request.getParameter("content"));
        resource.setRefCase(request.getParameter("refCase"));
        resource.setRefDemo(request.getParameter("refDemo"));
        DateTime now = DateTime.now();
        resource.setCreated(now);
        resource.setUpdated(now);

        resource = resourceRepository.saveAndFlush(resource);
        jsonResult.setSuccess(resource.getId()!=null);
        return jsonResult;
    }
    @RequestMapping("products/list")
    public ModelAndView productsList(@RequestParam int page,@RequestParam int size,@RequestParam Long categoryId,
                                    @RequestParam(required = false) String name) {
        ModelAndView mv = new ModelAndView();
        Pageable pageable= new PageRequest(page-1,size,new Sort(new Sort.Order(Sort.Direction. DESC,"id")));
        Page<Resource> dataPage = resourceRepository.findByNameLikeAndCategoryId("%" + name + "%", categoryId, pageable);
        mv.addObject("page",dataPage);
        mv.setViewName("manage/productsList");
        return mv;
    }

    @RequestMapping("resources")
    public ModelAndView resources(@RequestParam String category) {
        ModelAndView mv = new ModelAndView();
        List<Category> categorys = categoryRepository.findByType(category);
        if (categorys != null) {
            mv.addObject("categorys",categorys);
            mv.addObject("resourceType",category);
        }
        mv.setViewName("manage/resources");
        return mv;
    }

    @RequestMapping("resourceType/add")
    public ModelAndView resourceTypeAdd(@RequestParam String resourceType,@RequestParam(required = false) Long categoryId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("resourceType", resourceType);
        if (categoryId != null) {
            Category category = categoryRepository.findOne(categoryId);
            mv.addObject("category", category);
        }
        mv.setViewName("manage/resourceTypeAdd");
        return mv;
    }

    @RequestMapping("resourceType/save")
    @ResponseBody
    public JsonResult resourceTypeSave(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();

        Category category = new Category();
        String idStr = request.getParameter("id");
        DateTime now = DateTime.now();
        Long id = 0L;
        if (StringUtils.isNotBlank(idStr) && StringUtils.isNumeric(idStr)) {
            id = Long.valueOf(idStr);
            category = categoryRepository.findOne(id);
        }else{
            category.setCreated(now);
        }
        if(ServletFileUpload.isMultipartContent(request)){
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            MultipartFile logo = multiRequest.getFile("logo");
            if (logo != null) {
                String logoPath=this._saveFile(logo, request);
                category.setLogo(logoPath);
            }
            MultipartFile thumbnail = multiRequest.getFile("thumbnail");
            if (thumbnail != null) {
                String thumbnailPath=this._saveFile(thumbnail, request);
                category.setThumbnail(thumbnailPath);
            }
        }
        category.setName(request.getParameter("name"));
        category.setType(request.getParameter("type"));
        category.setUpdated(now);
        Category result = categoryRepository.saveAndFlush(category);
        jsonResult.setSuccess(result != null);
        return jsonResult;
    }

    @RequestMapping("resourceType/delete")
    @ResponseBody
    public JsonResult resourceTypeSave(@RequestParam Long categoryId) {
        JsonResult jsonResult = new JsonResult();
        categoryRepository.delete(categoryId);
        jsonResult.setSuccess(true);
        return jsonResult;
    }

    @RequestMapping("resourcesList/list")
    public ModelAndView resourcesList(@RequestParam Long categoryId,@RequestParam(required = false) String name) {
        ModelAndView mv = new ModelAndView();
        List<Resource> resources = Lists.newArrayList();
        if (StringUtils.isNotBlank(name)) {
            resources = resourceRepository.findByNameLikeAndCategoryId("%" + name + "%", categoryId);
        }else {
            resources = resourceRepository.findByCategoryId(categoryId);
        }
        mv.setViewName("manage/resourceList");
        mv.addObject("resources", resources);
        mv.addObject("categoryId", categoryId);
        return mv;
    }

    @RequestMapping("resource/add")
    public ModelAndView resourceAdd(@RequestParam Long categoryId,@RequestParam(required = false) Long resourceId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("categoryId", categoryId);

        Category category = categoryRepository.findOne(categoryId);
        if (category.getType().equals("schemes")) {
            List<Category> casesCate = categoryRepository.findByType("cases");
            List<Category> demoCate = categoryRepository.findByType("demos");
            if(!CollectionUtils.isEmpty(casesCate)) {
                List<Long> ids = Lists.newArrayListWithExpectedSize(casesCate.size());
                for (Category c : casesCate) {
                    ids.add(c.getId());
                }
                List<Resource> caseResources = resourceRepository.findByCategoryIdIn(ids);
                mv.addObject("caseResources", caseResources);
            }
            if (!CollectionUtils.isEmpty(demoCate)) {
                List<Long> ids = Lists.newArrayListWithExpectedSize(casesCate.size());
                for (Category c : demoCate) {
                    ids.add(c.getId());
                }
                List<Resource> demoResources = resourceRepository.findByCategoryIdIn(ids);
                mv.addObject("demoResources",demoResources);
            }
        }

        if (resourceId != null) {
            Resource resource = resourceRepository.findOne(resourceId);
            mv.addObject("resource", resource);
            mv.addObject("categoryName", resource.getCategoryName());


        }else if (categoryId != null) {
            mv.addObject("categoryName", category.getName());
        }
        mv.addObject("categoryType", category.getType());
        mv.setViewName("manage/resourceAdd");
        return mv;
    }

    @RequestMapping("videos")
    public ModelAndView videos() {
        ModelAndView mv = new ModelAndView();
        List<Video> videos= videoRepository.findAll();
        mv.addObject("videos", videos);
        mv.setViewName("manage/videos");
        return mv;
    }

    @RequestMapping("video/add")
    public ModelAndView videoAdd(@RequestParam(required = false) Long videoId) {
        ModelAndView mv = new ModelAndView();
        if (videoId != null) {
            Video video = videoRepository.findOne(videoId);
            mv.addObject("video", video);
        }
        mv.setViewName("manage/videoAdd");
        return mv;
    }

    @RequestMapping("video/save")
    @ResponseBody
    public JsonResult videoSave(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Video video = new Video();
        String idStr = request.getParameter("id");
        DateTime now = DateTime.now();
        Long id = 0L;
        if (StringUtils.isNotBlank(idStr) && StringUtils.isNumeric(idStr)) {
            id = Long.valueOf(idStr);
            video = videoRepository.findOne(id);
        }else{
            video.setCreated(now);
        }
        if(ServletFileUpload.isMultipartContent(request)){
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            MultipartFile logo = multiRequest.getFile("logo");
            if (logo != null) {
                String logoPath=this._saveFile(logo, request);
                video.setLogo(logoPath);
            }
            MultipartFile url = multiRequest.getFile("url");
            if (url != null) {
                String urlPath=this._saveFile(url, request);
                video.setUrl(urlPath);
            }
        }
        video.setName(request.getParameter("name"));
        video.setSize(request.getParameter("size"));
        video.setLength(request.getParameter("length"));
        video.setUpdated(now);
        Video result = videoRepository.saveAndFlush(video);
        jsonResult.setSuccess(result != null);
        return jsonResult;
    }

    @RequestMapping("video/delete")
    public JsonResult videoDelete(@RequestParam Long videoId) {
        JsonResult jsonResult = new JsonResult();
        videoRepository.delete(videoId);
        jsonResult.setSuccess(true);
        return jsonResult;
    }

    @RequestMapping("toChangePass")
    public String toChangePass(){
        return "manage/changePass";
    }

    @RequestMapping("changePass")
    public @ResponseBody JsonResult resetPass(@RequestParam String originalPass,
                                              @RequestParam String changePass,
                                              HttpSession session) {
        JsonResult jsonResult = new JsonResult();
        Object id=session.getAttribute(SmUtils.SEESION_USER_ID);
        User user = userRepository.findOne(Long.valueOf(id.toString()));
        if (user != null) {
            if(DigestUtils.md5Hex(changePass).equals(DigestUtils.md5Hex(originalPass))){
                String changedPass = DigestUtils.md5Hex(changePass);
                user.setPassword(changedPass);
                userRepository.saveAndFlush(user);
                jsonResult.setSuccess(true);
            }else {
                jsonResult.setSuccess(false);
                jsonResult.setMessage("原密码不正确!");
            }
        }else {
            jsonResult.setSuccess(false);
            jsonResult.setMessage("用户不存在!");
        }
        return jsonResult;
    }

    @RequestMapping("exit")
    public ModelAndView exit(HttpSession session){
        ModelAndView mv = new ModelAndView();
        session.removeAttribute(SmUtils.SEESION_USER_ID);
        mv.setViewName("manage/login");
        return mv;
    }

    @RequestMapping("/editor/uploadImg")
    public @ResponseBody Map<String, Object> uploadImg(MultipartFile upfile,HttpServletRequest request) {
        String orginName = upfile.getOriginalFilename();
        String fileSuffix = orginName.substring(orginName.indexOf("."), orginName.length());
        String fileName = CommonUtils.genUUID()+fileSuffix;

        String fileurl=this._saveFileWithName(upfile, request, fileName);

        Map<String, Object> map = Maps.newHashMap();
        map.put("state","SUCCESS");
        map.put("url",fileurl);
        map.put("title",fileName);
        map.put("original",orginName);
        return map;
    }

    private String _saveFile(MultipartFile file,HttpServletRequest httpServletRequest) {
        String originalFilename=file.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.indexOf("."),originalFilename.length());

        String dirname = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String upload = httpServletRequest.getSession().getServletContext().getRealPath("upload");
        String filePath=dirname+separator+ CommonUtils.genUUID()+fileSuffix;

        File targetFile = new File(upload+separator+filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return host+ separator +"upload"+separator+filePath;
    }

    private String _saveFileWithName(MultipartFile file,HttpServletRequest httpServletRequest,String fileName) {
        String dirname = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String upload = httpServletRequest.getSession().getServletContext().getRealPath("upload");
        String filePath=dirname+separator+ fileName;

        File targetFile = new File(upload+separator+filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return host+separator+"upload"+separator+filePath;
    }
}
