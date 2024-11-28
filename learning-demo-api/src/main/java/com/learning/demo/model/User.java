package com.learning.demo.model;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("sex")
    private String sex;
    @TableField("pwd")
    private String pwd;
    @TableField("email")
    private String email;
    @TableField("status")
    private Integer status;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}