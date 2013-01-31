package cpp.validatecode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import cpp.imagefilter.ThresholdFilter;

public class ValidateCode 
{
//	返回 读取到的验证码数字 和 会话session;
	public String[] getValidateCode (java.net.URL url)
	{
		String number = null, session=null;
		try 
		{
//			打开链接;
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
//			接收验证码图片;
			InputStream in = connection.getInputStream();
			BufferedImage image = javax.imageio.ImageIO.read (in);
//			图片二值化;
			image = threshould (image);
//			图片切割;(60x20);
			List<BufferedImage> subimages = splitImage(image);
//			分割后图片保存0-9数字单体图片做样本;
//			for (int x=0;x<subimages.size();++x)
//				ImageIO.write(subimages.get(x), "BMP", new File ("c:\\codes\\newPP"+x+".bmp"));
//			System.out.println("取样本");

//			循环比较分割后的图片 与 0-9单体数字binary集合;
			number = convert2Number (subimages);

//			返回当前会话session;
			session = connection.getHeaderField("Set-Cookie").split(";")[0];
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new String[]{ number,session };
	}
	
//	图片二值化;
	private BufferedImage threshould (BufferedImage image) throws IOException 
	{
        // 定义过滤器
        ThresholdFilter filter = new ThresholdFilter();
        
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage toImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        //
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                // 过滤
                int grayValue = filter.filterRGB(i, j, rgb);
                toImage.setRGB(i, j, grayValue);
            }
        }
       
        //ImageIO.write(toImage, "jpg", new File(toPath));
        return toImage;
    }
	
//	图片切割; (60x20);
	private List<BufferedImage> splitImage(BufferedImage img)
	{
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		int h = 12;
		int w = 8;
		
		subImgs.add(img.getSubimage(7, 4, w, h));
		subImgs.add(img.getSubimage(20, 4, w, h));
		subImgs.add(img.getSubimage(33, 4, w, h));
		subImgs.add(img.getSubimage(46, 4, w, h));
		return subImgs;
	}
	
//	循环比较分割后的图片 与 0-9单体数字binary集合;
	private String convert2Number (List<BufferedImage> images) throws IOException
	{
		StringBuffer buffer = new StringBuffer (images.size());

		for (BufferedImage x:images)
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(x, "BMP", out);
			byte b[] = out.toByteArray();
			out.flush();
			out.close();
			for (int i=0; i<NumberCode.numbers.length; ++i)
			{
				//定义index为字节相异个数;
				int index=0;
				for (int j=0; j<b.length; ++j)
				{
					if (b[j]!=NumberCode.numbers[i][j])
						index+=1;
				}
				//容错率;
				if(index<30)
				{	
					buffer.append (i);
					break;
				}

			}
		
		}	
		return buffer.toString();
	}
	
}
