package com.daocaowu.itelligentprofile.strategy;

public abstract class Strategy implements IStrategy{

	private IStrategy iStrategy;
	
	public Strategy(IStrategy iStrategy) {
		this.iStrategy = iStrategy;
	}
	
}
