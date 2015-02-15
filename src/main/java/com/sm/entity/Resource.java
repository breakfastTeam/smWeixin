package com.sm.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import javax.persistence.*;

/**
 * Created by Ez丶kkk on 15/2/5.
 */
@Entity
@Table(name = "resource")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")              private Long id;
    @Column(name="category_id")     private Long categoryId;
    @Column(name="category_name")   private String categoryName;
    @Column(name="name")            private String name;
    @Column(name="banner")          private String banner;
    @Column(name="thumbnail")       private String thumbnail;
    @Column(name="intro")           private String intro;
    @Column(name="content")         private String content;
    @Column(name="ref_case")        private String refCase;
    @Column(name="ref_demo")        private String refDemo;
    @Column(name="created")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime created;
    @Column(name="updated")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRefCase() {
        return refCase;
    }

    public void setRefCase(String refCase) {
        this.refCase = refCase;
    }

    public String getRefDemo() {
        return refDemo;
    }

    public void setRefDemo(String refDemo) {
        this.refDemo = refDemo;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getUpdated() {
        return updated;
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }
}
