package com.funny.unlockview;
import android.view.View;
import android.graphics.Paint;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import java.util.ArrayList;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import static com.funny.unlockview.FunnyUtils.*;
class FunnyUnlockView extends View{
	/*
	*Author:FunnySaltyFish
	from 2018.8.12 to 2019.12.22
	*/
	public final static int PATTERN_SET=1;
	public final static int PATTERN_UNLOCK=2;
	
	private float viewWidth,viewHeight;//宽高
	private int viewTimes=0;//只设置一次view的width，height
	private int drawTimes=0;//只在第一次设置数组
	private Paint paint;
	private float curStartX=-1,curStartY=-1;//当前的X，Y
	private float nextX=-1,nextY=-1;//我也不记得这是干啥的了……
	
	private float pointsPlaces[][];
	private List<Integer> curChosenIndex=null;
	private List<Integer> unlockCode=null;
	private boolean flag;//是否在绘制图形

	//以下为用户可以设置的属性
	private float smallCircleRadius=6f;//小圆半径
	private float largeCircleRadius=12f;//当某个点被选中时，它所强调的圆的半径
	private int lineColor=Color.WHITE;//线条颜色
	private int lineWidth=12;
	private int smallCircleColor=Color.WHITE;//正常绘制的点的颜色
	private int largeCircleColor=Color.WHITE;//当某个点被选中时，画的大圆的颜色
	
	
	private int pattern;//设置/输入
	private int inputPasswordTimes=0;//输入密码次数 该变量用于记录是第一次完成输入密码还是第二次完成输入密码
	private int minCodeLength=4;//密码最短长度
	private int maxCodeLength=9;//密码最短长度
	private int activeRadius=50;//该变量用于判断手指位置在某点的多少范围位置内算是触摸到点 用于提高用户体验
	
	private FunnyOnInputCodeListener onInputCodeListener;//在输入密码时的回调
	private FunnyOnUnlockListener onUnlockListener;//在使用密码解锁时的回调
	
	private Context ctx;

	private static String TAG="FunnyUnlockView";
	//private int pointNum=0;
	public FunnyUnlockView(Context ctx){
		super(ctx);
		this.ctx=ctx;
		this.pattern=this.PATTERN_SET;
		this.pointsPlaces=new float[9][2];
		setPaint();
		setBackgroundColor(Color.BLACK);
		this.curChosenIndex=new ArrayList<Integer>();//该变量记录当前正在输入的密码序列
		this.unlockCode=new ArrayList<Integer>();//保存的密码
	}
	
	private void setPaint(){
		this.paint=new Paint();
		this.paint.setAntiAlias(true);
		this.paint.setStrokeCap(Paint.Cap.ROUND);
		this.paint.setColor(lineColor);
		this.paint.setStrokeWidth(lineWidth);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO: Implement this method
		if(event.getAction()==MotionEvent.ACTION_DOWN&&!flag){
			int index=getPointIndex(event.getX(),event.getY());
			if(index>=0){
				curChosenIndex.add(index);
				flag=true;
				curStartX=nextX=pointsPlaces[index][0];
				curStartY=nextY=pointsPlaces[index][1];
				invalidate();
				//Log.i(TAG,"按下！");
			}
		}
		if(event.getAction()==MotionEvent.ACTION_MOVE&&flag){
			int index=getPointIndex(event.getX(),event.getY());
			if(index>=0&&!isInList(index,curChosenIndex)&&flag){
				curChosenIndex.add(index);
			}
			nextX=event.getX();nextY=event.getY();
			invalidate();
			//Log.i(TAG,"  移动！ ");
			return true;
		}
		if(event.getAction()==MotionEvent.ACTION_UP&&flag){
			if(pattern==PATTERN_SET){
				if(inputPasswordTimes==0){
					unlockCode.clear();
					if(curChosenIndex.size()<minCodeLength||curChosenIndex.size()>maxCodeLength){
						if(onInputCodeListener!=null){
							onInputCodeListener.onFailed(curChosenIndex,inputPasswordTimes);
						}
					}else{
						unlockCode.addAll(curChosenIndex);
						if(onInputCodeListener!=null){
							onInputCodeListener.onFinish(curChosenIndex,inputPasswordTimes);
						}
						inputPasswordTimes=1;
					}
					
				}
				else if(inputPasswordTimes==1){
					if(curChosenIndex.equals(unlockCode)){
						if(onInputCodeListener!=null){
							onInputCodeListener.onFinish(curChosenIndex,inputPasswordTimes);
						}
						inputPasswordTimes=0;
						
					}else{
						if(onInputCodeListener!=null){
							onInputCodeListener.onFailed(curChosenIndex,inputPasswordTimes);
						}
					}
				}
			}
			if(pattern==PATTERN_UNLOCK){
				if(unlockCode.equals(curChosenIndex)){
					if(onUnlockListener!=null){
						onUnlockListener.onSuccessd(curChosenIndex);
					}
				}else{
					if(onUnlockListener!=null){
						onUnlockListener.onFailed(curChosenIndex,unlockCode);
					}
				}
			}
			curStartX=curStartY=nextX=nextY=-1;
			curChosenIndex.clear();
			flag=false;
			invalidate();
			//System.out.println("  抬起！  ");
		}
		return true;

	}

	private int getPointIndex(float x,float y){//获取点的索引 0-8 九个点
		for(int i=0;i<9;i++){
			float distance=getTwoPointsDistanceSquare(x,y,pointsPlaces[i][0],pointsPlaces[i][1]);
			if(distance<=activeRadius*activeRadius){
				return i;
			}
		}
		return -1;
	}


	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO: Implement this method
		super.onDraw(canvas);
		if(viewWidth>0&&viewHeight>0){
			for(int i=0;i<3;i++){
				float distance=(viewHeight-60-3*smallCircleRadius)/2;
				if(drawTimes<=2){
					pointsPlaces[0+i*3][1]=(30f+smallCircleRadius*(i+1)+i*distance);
					pointsPlaces[1+i*3][1]=(30f+smallCircleRadius*(i+1)+i*distance);
					pointsPlaces[2+i*3][1]=(30f+smallCircleRadius*(i+1)+i*distance);
					pointsPlaces[0+i*3][0]=30f+smallCircleRadius;
					pointsPlaces[1+i*3][0]=viewWidth/2f;
					pointsPlaces[2+i*3][0]=viewWidth-30-smallCircleRadius;
					//System.out.println(showArray(pointsPlaces));
					drawTimes++;
				}
				//从左往右,从上往下依次1234……
				this.paint.setStrokeWidth(smallCircleRadius);
				this.paint.setColor(smallCircleColor);
				//this.paint.setStyle(Paint.Style.STROKE);
				canvas.drawCircle(pointsPlaces[0+i*3][0],pointsPlaces[0+i*3][1],smallCircleRadius,paint);
				canvas.drawCircle(pointsPlaces[1+i*3][0],pointsPlaces[1+i*3][1],smallCircleRadius,paint);
				canvas.drawCircle(pointsPlaces[2+i*3][0],pointsPlaces[2+i*3][1],smallCircleRadius,paint);
				if(flag){//已经开始绘制
					this.paint.setColor(lineColor);
					this.paint.setStyle(Paint.Style.FILL);
					int j=0;
					if(curStartX>=0&&curStartY>=0){//如果当前点在屏幕范围内
						for(j=0;j<curChosenIndex.size();j++){
							//绘制已经被连过的点;
							this.paint.setColor(largeCircleColor);
							canvas.drawCircle(pointsPlaces[curChosenIndex.get(j)][0],pointsPlaces[curChosenIndex.get(j)][1],largeCircleRadius,paint);
							if(j<curChosenIndex.size()-1){
								//绘制目前手指正在连的线
								this.paint.setStrokeWidth(lineWidth);
								this.paint.setColor(lineColor);
								canvas.drawLine(pointsPlaces[curChosenIndex.get(j)][0],pointsPlaces[curChosenIndex.get(j)][1],pointsPlaces[curChosenIndex.get(j+1)][0],pointsPlaces[curChosenIndex.get(j+1)][1],paint);
							}
						}
						curStartX=pointsPlaces[curChosenIndex.get(j-1)][0];
						curStartY=pointsPlaces[curChosenIndex.get(j-1)][1];
					}
					if(curStartX>=0&&curStartY>=0){
						this.paint.setStrokeWidth(lineWidth);
						this.paint.setColor(lineColor);
						canvas.drawLine(curStartX,curStartY,nextX,nextY,paint);
					}
				}
			}
		}else{//如果宽高没获取到，再来一次
			new Handler().postDelayed(new Runnable(){
					@Override
					public void run()
					{
						// TODO: Implement this method
						postInvalidate();
					}
				},100);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO: Implement this method
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.viewWidth=this.getMeasuredWidth();
		this.viewHeight=this.getMeasuredHeight();
		Log.i(TAG,"w:"+viewWidth);
		Log.i(TAG,"h:"+viewHeight);
		this.viewTimes++;
	}
	
	//以下为get/set方法
	public void setUnlockCode(List<Integer> unlockCode)
	{
		this.unlockCode = unlockCode;
	}

	public List<Integer> getUnlockCode()
	{
		return unlockCode;
	}

	public void setMinCodeLength(int minCodeLength)
	{
		this.minCodeLength = minCodeLength;
	}

	public int getMinCodeLength()
	{
		return minCodeLength;
	}

	public void setMaxCodeLength(int maxCodeLength)
	{
		this.maxCodeLength = maxCodeLength;
	}

	public int getMaxCodeLength()
	{
		return maxCodeLength;
	}

	public void setSmallCircleRadius(float smallCircleRadius)
	{
		this.smallCircleRadius = smallCircleRadius;
	}

	public float getSmallCircleRadius()
	{
		return smallCircleRadius;
	}

	public void setLargeCircleRadius(float largeCircleRadius)
	{
		this.largeCircleRadius = largeCircleRadius;
	}

	public float getLargeCircleRadius()
	{
		return largeCircleRadius;
	}

	public void setLineColor(int lineColor)
	{
		this.lineColor = lineColor;
	}

	public int getLineColor()
	{
		return lineColor;
	}

	public void setLineWidth(int lineWidth)
	{
		this.lineWidth = lineWidth;
	}

	public int getLineWidth()
	{
		return lineWidth;
	}

	public void setSmallCircleColor(int smallCircleColor)
	{
		this.smallCircleColor = smallCircleColor;
	}

	public int getSmallCircleColor()
	{
		return smallCircleColor;
	}

	public void setLargeCircleColor(int largeCircleColor)
	{
		this.largeCircleColor = largeCircleColor;
	}

	public int getLargeCircleColor()
	{
		return largeCircleColor;
	}

	public void setPattern(int pattern)
	{
		this.pattern = pattern;
	}

	public int getPattern()
	{
		return pattern;
	}

	public void setInputPasswordTimes(int inputPasswordTimes)
	{
		this.inputPasswordTimes = inputPasswordTimes;
	}

	public int getInputPasswordTimes()
	{
		return inputPasswordTimes;
	}

	public void setActiveRadius(int activeRadius)
	{
		this.activeRadius = activeRadius;
	}

	public int getActiveRadius()
	{
		return activeRadius;
	}

	public void setOnInputCodeListener(FunnyOnInputCodeListener onInputCodeListener)
	{
		this.onInputCodeListener = onInputCodeListener;
	}

	public FunnyOnInputCodeListener getOnInputCodeListener()
	{
		return onInputCodeListener;
	}

	public void setOnUnlockListener(FunnyOnUnlockListener onUnlockListener)
	{
		this.onUnlockListener = onUnlockListener;
	}

	public FunnyOnUnlockListener getOnUnlockListener()
	{
		return onUnlockListener;
	}

	public void setRadius(float radius)
	{
		this.smallCircleRadius = radius;
	}

	public float getRadius()
	{
		return smallCircleRadius;
	}

	public boolean isChoosen(){//判断现在是否选择了至少一个点
		return curChosenIndex!=null;
	}

	public boolean isCurCodeRight(){//当前选择的点是否正确
		return curChosenIndex.equals(unlockCode);
	}
	


}
