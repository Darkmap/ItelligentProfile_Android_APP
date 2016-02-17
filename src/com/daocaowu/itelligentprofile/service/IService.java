package com.daocaowu.itelligentprofile.service;

import java.util.List;

public interface IService {

	/**
	 * 把实体的内容保存进数据库
	 */
	public abstract int add(Object object);
	
	/**
	 * 把相应实体从数据库中删除
	 */
	public abstract void delete(int objectId);
	
	/**
	 * 从数据库中查找相应实体
	 */
	public abstract Object check(int objectId);
	
	/**
	 * 从数据库中获取多个实体
	 * @return
	 */
	public abstract List check();
	
	/**
	 * 更新相应数据库中相应实体数据
	 */
	public abstract void update(Object object);
	
	
}
