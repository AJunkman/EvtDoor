package qr_code;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrcodeUtil {
	//加密
	public static void encodeImt(String imgpath,String format,String content,int width,int height,String logo) throws Throwable {
		
		Hashtable<EncodeHintType,Object> hints = new Hashtable<EncodeHintType,Object>();
		//排错律L<M<Q<H
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		//编码方式ʽ
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		//边距
		hints.put(EncodeHintType.MARGIN, 1);
		
		/*
		 * content:需要加密的内容
		 * BarcodeFormat.QR_CODE : 解码的是二维码类型
		 * hints : 加密涉及的一些参数，编码，排错律
		 */
		BitMatrix bitmatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
		
		//生成内存中的一张图片->需要一个boolean[][]二维数组->BitMatrix
		//RenderedImage是接口，则实例化他的子类(Ctrl+t)
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				img.setRGB(i,j,	(bitmatrix.get(i, j)?0x000000:0xffffff) );
			}
		}
		
		//画logo，可选择
		//img = QrcodeLogo.logoMatrix(img, logo);
		
		
		
		//String -> File
		File file = new File(imgpath);
		
		//生成图片
		/*
		 * img : RenderedImage内存中的一张图片
		 * format : 图片格式
		 * file : 图片文件
		 */
		ImageIO.write(img,format,file);
		
	}
	
	//解密
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
}
