package com.icc.core.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Audit {
    private Timestamp createDate;
    private String createUser;
    private Timestamp updateDate;
    private String updateUser;
    private String deleteSw;
}
