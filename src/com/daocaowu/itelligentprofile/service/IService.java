package com.daocaowu.itelligentprofile.service;

import java.util.List;

public interface IService {

	/**
	 * ��ʵ������ݱ�������ݿ�
	 */
	public abstract int add(Object object);
	
	/**
	 * ����Ӧʵ������ݿ���ɾ��
	 */
	public abstract void delete(int objectId);
	
	/**
	 * �����ݿ��в�����Ӧʵ��
	 */
	public abstract Object check(int objectId);
	
	/**
	 * �����ݿ��л�ȡ���ʵ��
	 * @return
	 */
	public abstract List check();
	
	/**
	 * ������Ӧ���ݿ�����Ӧʵ������
	 */
	public abstract void update(Object object);
	
	
}
