package com.daocaowu.itelligentprofile.strategy;

public interface IStrategy {

	/**
	 * 执行策略
	 */
	public abstract void execute();
	
	/**
	 * 异步执行策略
	 */
	public abstract void doAysnTask();
}
