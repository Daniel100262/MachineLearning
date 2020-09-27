package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class MatUtils {
	
	//Converte de imagem do JavaFX para Mat do OpenCV
	public static Mat image2Mat(Image image) {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        return bufferedImage2Mat(bImage);
    }
	
	//Converte Buffered Image do AWT para Mat do OpenCV
	public static Mat bufferedImage2Mat(BufferedImage imgBufferedImage) {
          Mat imgMat;
          byte[] data;
          int r, g, b;
          int altura = imgBufferedImage.getHeight();
          int largura = imgBufferedImage.getWidth();
          
          if(imgBufferedImage.getType() == BufferedImage.TYPE_INT_RGB || imgBufferedImage.getType() == BufferedImage.TYPE_INT_ARGB) {
        	  imgMat = new Mat(altura, largura, CvType.CV_8UC3);
              data = new byte[altura * largura * (int)imgMat.elemSize()];
              int[] dataBuff = imgBufferedImage.getRGB(0, 0, largura, altura, null, 0, largura);
              for(int i = 0; i < dataBuff.length; i++) {
                  data[i*3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                  data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                  data[i*3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
              }
          }
          else {
        	  imgMat = new Mat(altura, largura, CvType.CV_8UC1);
              data = new byte[altura * largura * (int)imgMat.elemSize()];
              int[] dataBuff = imgBufferedImage.getRGB(0, 0, largura, altura, null, 0, largura);
              for(int i = 0; i < dataBuff.length; i++) {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
              }
           }
          imgMat.put(0, 0, data);
           return imgMat;
     } 

	
	//Converte de Mat do OpenCV para imagem do JavaFX
	public static Image mat2Image(Mat frame) {
		try {
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		}
		catch (Exception e) {
			System.err.println("Cannot convert the Mat obejct: " + e);
			return null;
		}
	}
	
	//Converte de Mat do OpenCV para Buffered Image do AWT  
	private static BufferedImage matToBufferedImage(Mat original) {
		// init
		BufferedImage image = null;
		int width = original.width(), height = original.height(), channels = original.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		original.get(0, 0, sourcePixels);
		
		if (original.channels() > 1) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		}
		else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
		
		return image;
	}
}
