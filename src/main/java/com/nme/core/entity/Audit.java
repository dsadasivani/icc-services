package com.nme.core.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Audit {
	private Timestamp createDate;
	private String createUser;
	private Timestamp updateDate;
	private String updateUser;
	private String deleteSw;
}
