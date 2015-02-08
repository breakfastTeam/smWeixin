package com.sm.entity;

import javax.persistence.*;
import java.awt.dnd.Autoscroll;

/**
 * Created by Ez丶kkk on 15/2/8.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")          private Long id;
    @Column(name="login_name")  private String loginName;
    @Column(name="password")    private String password;
    @Column(name="user_name")   private String userName;
    @Column(name="role")        private String role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
