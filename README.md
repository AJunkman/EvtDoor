# EvtDoor
一、项目概述
在传统的门禁系统中，对于出入人员的管理，主是是通过刷卡、指纹、虹膜、或是人工授权等工作。在传统的门禁管理系统中，对于内部员工管理十分方便，而且功能完善，尤其是近几年的互联网快速发展，越来越多的中小企业开始安装自己的门禁系统，实现对企内工作人员的出入自动化管理。针对上述传统门禁的不足之处和对区块链技术的掌握和探索，我们拟采用区块链中通证验证的思想设计门禁系统，利用区块链中生成并发行的通证二维码来描述身份信息，通过区块链技术对二维码解析，通过调用共链上的相关信息来验证通证的有效性。这样能够对中心身份验证机制进行改进，实现去中心安全服务。 

二、项目特色
1. 采用二维码通证的方式来验证门禁系统，用户能够通过移动终端软件来生成，相对于传统门禁系统更加方便。

2. 系统的设计是基于区块链的相关技术做的二次开发，系统相对于市面上的其他同类型软件来说，比较安全。

3. 通过对通证的权限进行相关设置，能够实现对已经发行通证的销毁，管理方便。

4. 通过通证生成的二维码具有实时更新的动态效果，每个二维码验证具有时效性，相对于传统的验证方式更加安全。

三、作品安装说明
 

1. 硬件方面：树莓派3B+、UPS不间断电源、电磁锁、电磁继电器、摄像头。

 通过树莓派部署门禁系统运行需要的环境，将摄像头连接在树莓派上，能够通过树莓派调用摄像头扫描服务，然后树莓派能够通过扫描的结果来控制电磁锁，其中为防止断电，我们采用UPS不间断电源为整个系统供电。

2. 软件方面：基于Everitoken开发的钱包、基于Everitoken开发的门禁控制系统。

           用户可以直接在自己的移动终端上下载基于Everitoken二次开发的EVT钱包，通过钱包里验证通证的功能使用们系统系统的服务，在树莓派上部署基于Everitoken二次开发的门禁系统，能够实现通过验证结果来控制电磁锁的功能。

四、设计思路
 

           1. 用户使用移动设备中基于Everitoken二次开发的钱包软件，生成通证，并发布在区块链的公链上。

           2. 通过门禁系统对来访的用户手中的二维码，通过调用公链上已经存在的通证进行验证，并将验证的结果反馈给门禁系统，由门禁系统确定是否打开电磁锁。

          采用区块链中非对称加密的方法生成通证，根据通证的内容生成二维码，并且通过二维码我们不能够解析出任何与用户个人信息有关的内容，能够保证系统的安全性。树莓派上部署的门禁系统通过对用户持有的通证进行验证，验证的方法试通过调用共链上的信息执行的，基于区块链的安全性，通证具有不可伪造的安全特性。

五、代码实现（部分）
树莓派调用摄像头服务：

private static String qrcode = "/home/pi/image/image.jpg";
    private static String[] cmdOrder = {"sh", "-c", "raspistill -w 500 -h 500 -o " + qrcode};
 
    public static boolean photo() {
        try {
            Process process = Runtime.getRuntime().exec(cmdOrder);
            process.waitFor();
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            while((line = bufferedReader.readLine()) != null) {
                System.out.println("系统信息: (Take photo) " + str);
            }
        } catch (InterruptedException exception) {
            return false;
        } catch (IOException exception) {
            return false;
        }
        return true;
    }
通过树莓派控制继电器：

    public static void OpenDoor(GpioPinDigitalOutput gd) {                           
        gd.setState(PinState.LOW);        //设置低电平断电                                           
        try {
            Thread.sleep(3000);       //停留3秒                                                        
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            gd.setState(PinState.HIGH);
        }
    }
解码实现：

	public static String decodeImg(File file) throws Exception  {
		 try {
	            MultiFormatReader formatReader = new MultiFormatReader();
	            if (!file.exists()) {
	            	System.out.println("你要解析的内容不存在！");
	                return null;
	            }
	          //file->内存中的一张图片
	           BufferedImage image = ImageIO.read(file);
	            LuminanceSource source = new BufferedImageLuminanceSource(image);
	            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	            QRCodeReader reader = new QRCodeReader();
	            Result result = null;
	            try {
	            	result = reader.decode(bitmap);
	            	return result.getText();
	            } catch (ReaderException e) {
	            	e.printStackTrace();
	            	return null;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
		
	}
使用Everitoken服务对字符串内容进行解析：

    NetParams netParams = new MainNetNetParams(NetParams.NET.MAINNET1);
    String linkID = ExplainQrCodes();
    if(linkID!=null){
	   	EvtLink.EveriPassVerificationResult epvr  = new 
            EvtLink.EveriPassVerificationResult(true, "ZJ123456", "door10");
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

 
