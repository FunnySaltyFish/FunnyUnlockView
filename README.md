
> 但是一个人并不是为失败而生的——一个人可以被摧毁，但不能被打败——海明威  
  
年轻人的第一个开源库：  
FunnyUnlockView  
几行代码助您轻松实现Android九格滑动解锁界面！  
### 概况/Brief Description  
___ 
Author:FunnySaltyFish  
from 2018.8.12 to 2019.12.22  
### 效果/Result  
___ 
 ![result](https://img-blog.csdnimg.cn/2019122200390342.gif){:height="50%" width="50%"}

### 基本使用/Basic Usage  
___ 
```java  
FunnyUnlockView view=new FunnyUnlockView(this);  
setContentView(view);  
```
●若您不设置任何属性，您可以直接向这样使用。  
◆在这种状况下，您可以实现手指滑动完成连线，内部处理逻辑会自动完成输入 *(第一次合法连线)* 和验证 *（第二次合法连线）* 操作。但不会有任何提示。  
  
### 设置模式/Set Pattern  
●本View实现了两个模式及输入密码模式和使用密码模式  
  

|   模式      |  常量       |   描述      |  
| :------: | :------: | :------ |  
|    设置密码  |    PATTERN_SET     | 在该模式下用户将完成对密码的输入操作，其中包括第一次的输入和第二次的验证操作。当输入密码合法时，将会自动跳到验证。      |  
|    使用密码  |    PATTERN_UNLOCK     |   在该模式下，用户将使用以获得的密码进行解锁。您可以调用getUnlockCode()获取已保存密码     |  
* 您可以使用unlockview.setPattern(int pattern);  
* 默认模式为设置密码模式  
  
### 添加回调/Add Callback  
___ 
●本View可以添加以下两个Listener  
◆FunnyOnInputCodeListener  
* 当用户输入密码时，会回调此接口  
```java  
void onFinish(List<Integer> code,int inputTimes);  
	//完成一次输入或一次验证后回调  
	//code：当前输入的密码  
	//inputTimes：输入密码的次数（0代表第一次输入，1代表正在验证）  
	  
void onFailed(List<Integer> code,int inputTimes);  
	//输入的密码有问题或验证失败时回调此方法  
	//code：当前输入的密码  
	//inputTimes：输入密码的次数（0代表第一次输入，1代表正在验证）  
```
* 具体使用逻辑可参见内置案例  
◆FunnyOnUnlockListener  
*当用户使用已设置好的密码解锁时，会回掉此接口  
```java  
	void onSuccessd(List<Integer> code);  
	//使用密码解锁成功后回调  
	//code：解锁密码  
	  
	void onFailed(List<Integer> inputCode,List<Integer> rightCode);  
	//解锁失败（密码不正确）的回调  
	//inputCode:用户当前输入的密码  
	//rightCode:实际上正确的密码  
```
  
  
### 属性设置/Set Attributes  
___ 
●目前仅支持使用java代码设置属性  
◆绘制方面/Draw（以下所有属性均有对应的set/get方法）  
```java  
   private float smallCircleRadius=6f;//小圆半径  
   private float largeCircleRadius=12f;//当某个点被选中时，它所强调的圆的半径  
   private int lineColor=Color.WHITE;//线条颜色  
   private int lineWidth=12;  
   private int smallCircleColor=Color.WHITE;//正常绘制的点的颜色  
   private int largeCircleColor=Color.WHITE;//当某个点被选中时，画的大圆的颜色  
  
```
  
◆密码方面/Code  
```java  
   private int pattern;//设置/输入  
	private int inputPasswordTimes=0;//输入密码次数 该变量用于记录是第一次完成输入密码还是第二次完成输入密码  
	private int minCodeLength=4;//密码最短长度  
	private int maxCodeLength=9;//密码最短长度  
	private int activeRadius=50;//该变量用于判断手指位置在某点的多少范围位置内算是触摸到点 用于提高用户触摸到某点的概率  
```
  
### 其他/Others  
___ 
* 您可以使用isCurCodeRight()判断当前输入的密码是否正确  
* 您可以直接使用setUnlockCode(List<Integer> unlockCode)来设置一个密码，而不是让用户输入  
* 不同模式间的切换，内部不会自动进行，请根据需要手动进行  
* 本项目内的密码类型为ArrayList<Integer>，从0-8代指九个点（从左到右，从上到下）  
* 这是我第一次做开源，很多地方不是很到位，敬请谅解。本项目初次创建于2018年，当时是为了练习使用2019年12月将其改成现在。这将是FunnyViews家族的第一个孩子，感谢您的支持。  
  
### 使用/Use  
___  

* 暂时不支持maven方式添加依赖，如需使用，请clong项目，并使用compile project或直接复制相应代码（保留作者身份），谢谢  
  


