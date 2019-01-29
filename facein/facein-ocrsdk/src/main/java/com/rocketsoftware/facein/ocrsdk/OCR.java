package com.rocketsoftware.facein.ocrsdk;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * 
 * @author chens
 *
 */
public class OCR {

	private static final String datapath = "C:\\Users\\chens\\Downloads\\tess4j\\src\\main\\resources\\tessdata";
	
	public String passportNo(InputStream is) throws IOException {
	    Image image = ImageIO.read(is);
	    BufferedImage bi = toBufferedImage(image);
	    Rectangle rect = new Rectangle(0, image.getHeight(null)-image.getHeight(null)/4, image.getWidth(null)/2, image.getHeight(null)/4);
	    System.out.println("doOCR rect="+rect);
//	    ImageIcon ii = new ImageIcon(bi);
//      JOptionPane.showMessageDialog(null, ii);
	    
		Tesseract tessInst = new Tesseract();
		tessInst.setDatapath(datapath);
		tessInst.setLanguage("eng");
		try {
			String result = tessInst.doOCR(bi, rect);
//			System.out.println(result);
			Pattern p = Pattern.compile("\\d(CHN)\\d");
			String[] lines = result.split("\n");
			for (String line : lines) {
				Matcher m = p.matcher(line);
				boolean flag = m.find();
				if (flag) {
					String[] items = line.split(p.pattern());
					System.out.println("doOCR results="+items[0]);
					return items[0];
				}
			}
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
		return "";
	}
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	private BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	    	BufferedImage bi = (BufferedImage) img;
//	    	Graphics2D bGr = bi.createGraphics();
//	    	bGr.setColor(Color.red);
//	    	Rectangle rect = new Rectangle(0, img.getHeight(null)-img.getHeight(null)/5, 2000, img.getHeight(null)/5);
//		    bGr.drawRect(rect.x,rect.y,rect.width,rect.height);
	        return bi;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
}
