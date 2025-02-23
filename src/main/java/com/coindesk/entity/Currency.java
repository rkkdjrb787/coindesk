package com.coindesk.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "currency")
public class Currency {

	@Id
	private String id; // 英文
	
	@Column(name = "chinese_name", nullable = false)
	private String chineseName; // 中文
	
	@CreationTimestamp
	@Column(name = "create_time", updatable = false)
	private LocalDateTime createTime;

	@UpdateTimestamp
	@Column(name = "update_time")
	private LocalDateTime updateTime;
}
