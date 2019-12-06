package com.qianfeng.smsplatform.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author simon
 * 2019/12/3 15:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Man implements Serializable {
    private String name;
    private int age;
    private String addr;
}
