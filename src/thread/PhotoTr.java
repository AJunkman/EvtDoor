package thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
//定义拍照线程
public class PhotoTr extends Thread {
    public static String path = "/home/pi/Pictures/image.jpg";     //定义二维码图片保存地址
    public static String[] photo = {"sh", "-c", "raspistill -op 180 -t 500 -w 500 -h 500 -o " + path};   //调用摄像头服务，两秒拍一次
    //public static String[] photo = {"sh", "-c", "raspivid -op 180 -t 5000 -w 500 -h 500 -o " + path};
    public static InputStream inputStream;     
    public boolean trRunning;

    public PhotoTr() {
        this.trRunning = false;
    }

   private  boolean TakePhoto() {
    	
    	try {
            Process process = Runtime.getRuntime().exec(photo);     //定义进程对象
            process.waitFor();
            this.inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));   //定义读取数据对象
            String line = null;
            while((line = bufferedReader.readLine()) != null) {    
                System.out.println("读取数据信息:" + line);
              }
        } catch (InterruptedException exception) {
            return false;
        } catch (IOException exception) {
            return false;
        }
        return true;
    }
   @Override
    public void run() {
        this.trRunning = true;
        while (true) {
           this.trRunning = this.TakePhoto();
        }
    }
}
