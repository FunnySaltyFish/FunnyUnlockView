package com.funny.unlockview;

import android.content.Context;
import android.widget.Toast;
import java.util.List;

public class FunnyUtils
{
	public static void print(Context ctx,String str){
		//Toast
		Toast.makeText(ctx,str,Toast.LENGTH_SHORT).show();
	}
	
	public static boolean isInList(int number,List<Integer> list){
		//判断一个数字是否在列表里面
		for(int i:list){
			if(i==number){return true;}
		}
		return false;
	}

	public static String showArray(float[][] arr){
		//将数组转化为文字输出
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[0].length;j++){
				sb.append(arr[i][j]);
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public static float getTwoPointsDistanceSquare(float x1,float y1,float x2,float y2){
		//获取两点间距离的平方
		return (float)(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
	}
}

