package com.funny.unlockview;

import android.app.*;
import android.os.*;
import android.content.res.Resources;
import android.content.Context;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import static com.funny.unlockview.FunnyUtils.*;
import java.util.List;
import android.graphics.Color;
import android.widget.Toast;
public class MainActivity extends Activity 
{
	FunnyUnlockView unlockView;
	Resources re;
	int width,height;
	OnClickListener listener;
	Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		con=this;
		setTheme(android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
		re=getResources();
		width=re.getDisplayMetrics().widthPixels;
		height=re.getDisplayMetrics().heightPixels;
		LinearLayout l=new LinearLayout(this);
		l.setOrientation(1);
		l.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-1);
		params.width=params.height=width*7/8;
		unlockView=new FunnyUnlockView(this);
		unlockView.setSmallCircleColor(Color.WHITE);
		unlockView.setSmallCircleRadius(6);
		unlockView.setLargeCircleColor(Color.WHITE);
		unlockView.setLargeCircleRadius(9);
		unlockView.setLineColor(Color.WHITE);
		unlockView.setLineWidth(6);
		
		unlockView.setMinCodeLength(4);
		unlockView.setMaxCodeLength(9);
		unlockView.setActiveRadius(50);
		unlockView.setPattern(FunnyUnlockView.PATTERN_SET);
		unlockView.setOnUnlockListener(new FunnyOnUnlockListener(){
				@Override
				public void onSuccessd(List<Integer> code)
				{
					// TODO: Implement this method
					print(re,R.string.unlock_successd);
				}

				@Override
				public void onFailed(List<Integer> inputCode, List<Integer> rightCode)
				{
					// TODO: Implement this method
					print(re,R.string.unlock_failed);
				}
		});
		unlockView.setOnInputCodeListener(new FunnyOnInputCodeListener(){
				@Override
				public void onFailed(List<Integer> code, int inputTimes)
				{
					// TODO: Implement this method
					if(inputTimes==0){
						if(code.size()<unlockView.getMinCodeLength()){
							print(re,R.string.code_too_short);
						}else if(code.size()>unlockView.getMaxCodeLength()){
							print(re,R.string.code_too_long);
						}
					}else if(inputTimes==1){
						print(re,R.string.code_verify_fail);
					}
				}

				@Override
				public void onFinish(List<Integer> code,int inputTimes)
				{
					// TODO: Implement this method
					if(inputTimes==0){
						print(re,R.string.code_set_finish);
					}else{
						print(re,R.string.code_verify_succeed);
					}
				}
			
		});
		
		l.addView(unlockView,params);
		FunnyButton b1=new FunnyButton(this,"设置密码模式",0x001);
		setButtonListener();
		b1.setOnClickListener(listener);
		FunnyButton b2=new FunnyButton(this,"输入密码模式",0x002);
		b2.setOnClickListener(listener);
		l.addView(b1);
		l.addView(b2);
		setContentView(l);
        //setContentView(R.layout.main);
    }
	
	private void setButtonListener(){
		listener = new OnClickListener(){
			@Override
			public void onClick(View view)
			{
				// TODO: Implement this method
				switch(view.getId()){
					case 0x001:
						unlockView.setPattern(unlockView.PATTERN_SET);
						print(re,R.string.pattern_to_set);
						break;
					case 0x002:
						if(!(unlockView.getPattern()==unlockView.PATTERN_SET&&unlockView.getInputPasswordTimes()==1)){
							if(unlockView.isChoosen()){
								unlockView.setPattern(unlockView.PATTERN_UNLOCK);
								print(re,R.string.patter_to_unlock);
								unlockView.setInputPasswordTimes(0);
							}else{
								print(re,R.string.toast_no_code);
							}
						}else{
							print(re,R.string.toast_no_verify);
						}
						break;
				}
			}
		};
	};
	
	public void print(Resources re,int id){
		Toast.makeText(this,re.getString(id),Toast.LENGTH_SHORT).show();
	}
	
	class FunnyButton extends Button{
		public FunnyButton(Context ctx,String text,int id){
			super(ctx);
			setText(text);
			setId(id);
		}
	}
	
}
