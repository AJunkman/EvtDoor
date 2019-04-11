package qr_code;

import java.awt.image.BufferedImage;
import java.io.File;

public class QrcodeTest {
	public static void main(String[] args) throws Throwable {
		String imgpath = "src/二维码样例.gif";
		String content = "www.baidu.com";
		String logopath = "src/logo.png";

		//加密
		QrcodeUtil.encodeImt(imgpath, "gif", content, 430, 430,logopath);

		//解密
		QrcodeUtil.decodeImg(new File(imgpath));
	}
}
