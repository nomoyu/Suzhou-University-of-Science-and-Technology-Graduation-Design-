package com.tian.business.domain;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * User: tian
 * Date: 2023/3/28
 * Time: 22:26
 */
@Data
public class Write {
    private int id;
    private int teamNumber;
    private float version;

    private String note;

}
