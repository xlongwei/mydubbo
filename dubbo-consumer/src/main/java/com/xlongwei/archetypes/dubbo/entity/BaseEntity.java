package com.xlongwei.archetypes.dubbo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {
	@Id	@GeneratedValue(generator = "entity")    
	@GenericGenerator(name = "entity", strategy = "assigned")    
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP) @Column(updatable=false)
	private Date createAt;
	@Column(insertable=false)
	private boolean deleted;
	@Version @Column(insertable=false)
	private Integer version;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public String toString() {
		return getClass().getSimpleName()+"@"+getId();
	}
}
