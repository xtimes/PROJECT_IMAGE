package cpp.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cpp.imagefilter.ThresholdFilter;

public class ImageFilterTest 
{
	public void threshould (String fromPath, String toPath) throws IOException 
	{
        // 定义过滤器
        ThresholdFilter filter = new ThresholdFilter();
        BufferedImage fromImage = ImageIO.read(new File(fromPath));
        int width = fromImage.getWidth();
        int height = fromImage.getHeight();
        BufferedImage toImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        //
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = fromImage.getRGB(i, j);
                // 过滤
                int grayValue = filter.filterRGB(i, j, rgb);
                toImage.setRGB(i, j, grayValue);
            }
        }
        //
        ImageIO.write(toImage, "jpg", new File(toPath));
    }
	
	public static void main(String[] args) throws IOException 
	{
		new ImageFilterTest().threshould("c:\\test.bmp", "c:\\xxx.bmp");
	}
}
