package qr_code;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class QrcodeLogo {
	//从传入的logo，二维码，——>带logo的二维码
	public static BufferedImage logoMatrix(BufferedImage matrixImage,String logo) throws IOException {
		//在二维码上画logo，产生一个二维码画板
		Graphics2D g2 = matrixImage.createGraphics();
		
		
		//画上logo:String-> BufferedImage
		BufferedImage logoImg = ImageIO.read(new File(logo));
		int height = matrixImage.getHeight();
		int width = matrixImage.getWidth();
		//纯logo
		g2.drawImage(logoImg, width*2/5,height*2/5, width/5, height/5, null);
		//产生一个画白色圆角的正方形方框画笔
		BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		//将画笔画板关联起来
		g2.setStroke(stroke);
		
		RoundRectangle2D.Float round = new RoundRectangle2D.Float(width*2/5,height*2/5,width/5, height/5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setColor(Color.WHITE);
		g2.draw(round);
		
		//灰色边框
		BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke2);
		//创建一个正方形
		RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(width*2/5+2,height*2/5+2,width/5-4, height/5-4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setColor(Color.GRAY);
		g2.draw(round2);
		
		g2.dispose();
		matrixImage.flush();
		
		return matrixImage;
		
	}
	
}
