package com.funny.unlockview;
import java.util.List;

public interface FunnyOnUnlockListener
{
	void onSuccessd(List<Integer> code);
	void onFailed(List<Integer> inputCode,List<Integer> rightCode);
}
