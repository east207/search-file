package com.gzh.pojo.entity;

import java.io.Serializable;


/**
 * 
 */
public class UserAccount implements Serializable {


	/**
	 * 
	 */
	private String email;

	/**
	 * 
	 */
	private String userId;

	/**
	 * 
	 */
	private Integer state;

	/**
	 * 
	 */
	private Integer id;


	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setState(Integer state){
		this.state = state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
	}

	@Override
	public String toString (){
		return "email:"+(email == null ? "空" : email)+"，userId:"+(userId == null ? "空" : userId)+"，state:"+(state == null ? "空" : state)+"，id:"+(id == null ? "空" : id);
	}
}
