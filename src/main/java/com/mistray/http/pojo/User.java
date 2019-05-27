package com.mistray.http.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author MistRay
 * @Project netty-study
 * @Package com.mistray.http.pojo
 * @create 2019年05月24日 16:40
 * @Desc
 */
@Data
public class User {
    private String userName;
    private String method;
    private Date date;
}
