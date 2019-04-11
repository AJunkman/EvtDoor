package dao;

import java.io.File;
import java.io.InputStream;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import qr_code.QrcodeUtil;

import thread.PhotoTr;


import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.abi.EveriPassAction;
import io.everitoken.sdk.java.param.MainNetNetParams;
import io.everitoken.sdk.java.param.NetParams;

public class ControlMain {
	
	 //用于读取图片，解释二维码的信息
	 public static String ExplainQrCodes() throws Exception {                         
	        File file = new File(PhotoTr.path);                                  //获取图片路径
	        String link = new QrcodeUtil().decodeImg(file);                         //提取二维码的信息
	        if (link == null) {                                               //二维码信息为空
	           System.out.println("提示：二维码无法识别！");
	            return null;
	        } 
	        return link;
	    }
	 
	 //对继电器进行控制
	 public static void OpenDoor(GpioPinDigitalOutput doorController) {                           //开门函数
	        doorController.setState(PinState.LOW);                                                   //设置低电平断电
	        try {
	            Thread.sleep(5000);                                                                  //停留5秒回归高电平
	        } catch (InterruptedException exception) {
	            exception.printStackTrace();
	        } finally {
	            doorController.setState(PinState.HIGH);
	        }
	    }
	 
	 
	
	public static void main(String[] args) throws Exception {
		 
	        boolean isSuccess;
	        PhotoTr photoTr= new PhotoTr();                                 //创建拍照线程

	        final GpioController gpioController = GpioFactory.getInstance();
	        final GpioPinDigitalOutput doorController = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyControl", PinState.HIGH);
	        final GpioPinDigitalInput buttonController = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

	        doorController.setShutdownOptions(true, PinState.LOW);

	        photoTr.start();
	        //wechatListenThread.start();
	        while (true) {
	            isSuccess = photoTr.trRunning;                                           //如果拍照线程正在运行
	            if (!isSuccess) {
	                System.out.println("拍照失败");
	                continue;
	              }
	            //String reservationID = ExplainQrCodes();
	           NetParams netParams = new MainNetNetParams(NetParams.NET.MAINNET1);
	   		 	String linkID = ExplainQrCodes();
	   		 	if(linkID!=null){
	   		 		EvtLink.EveriPassVerificationResult epvr  = new EvtLink.EveriPassVerificationResult(true, "ZJ123456", "door10");
	   		 		epvr = EvtLink.validateEveriPassUnsafe(netParams,linkID);
	   		 		if(epvr.isValid()) {
	   		 			System.out.println(epvr.isValid()+epvr.getDomain()+epvr.getTokenName());
	   		 			if(epvr.getDomain().equalsIgnoreCase("ZJ123456")){
		   		 			System.out.println("通证"+epvr.getTokenName()+"打开门禁");
		   		 			OpenDoor(doorController);
		   		 			continue;
	   		 			}
	   		 		}
	   		 	}	   		 	
	        }
	}
}
