package com.funny.unlockview;
import java.util.List;

public interface FunnyOnInputCodeListener
{
	void onFinish(List<Integer> code,int inputTimes);
	//完成一次输入/一次验证后回调
	//code：当前输入的密码
	//inputTimes：输入密码的次数（0代表第一次输入，1代表正在验证）
	
	void onFailed(List<Integer> code,int inputTimes);
	//输入的密码有问题/验证失败时回调此方法
	//code：当前输入的密码
	//inputTimes：输入密码的次数（0代表第一次输入，1代表正在验证）
	
}
