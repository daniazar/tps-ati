package renderer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import util.CircleHough;
import util.hystThresh;
import util.nonmax;
import util.sobel;
import core.PixelRay;



public class CircleHoughFilter extends Filter {

	protected BufferedImage auxImg;
	public static int black;
	public CircleHough circleHoughObject;
	public int orig[] = null;
	public int width, height;
	static sobel sobelObject;
	static nonmax nonMaxSuppressionObject;
	static hystThresh hystThreshObject;
	public static int radius = 15;
	public static int circles = 5;
	
	public CircleHoughFilter() {
		width = oldImg.getWidth();
		height = oldImg.getHeight();
		
		circleHoughObject = new CircleHough();
		sobelObject = new sobel();
		nonMaxSuppressionObject = new nonmax();
		hystThreshObject = new hystThresh();
		circleHoughObject = new CircleHough();
		
		orig=new int[width*height];
		PixelGrabber grabber = new PixelGrabber(oldImg, 0, 0, width, height, orig, 0, width);
		try {
			grabber.grabPixels();
		}
		catch(InterruptedException e2) {
			System.out.println("error: " + e2);
		}



		sobelObject.init(orig,width,height);
		orig = sobelObject.process();
		double direction[] = new double[width*height];
		direction=sobelObject.getDirection();
		nonMaxSuppressionObject.init(orig,width,height);
		orig = nonMaxSuppressionObject.process();
		hystThreshObject.init(orig,width,height, 25,50);
		orig = hystThreshObject.process();	
		circleHoughObject.init(orig,width,height, radius);
		circleHoughObject.setLines(circles);
		orig = circleHoughObject.process();
	}
	
	@Override
	public Color RenderPixel(PixelRay ray) {
		//System.out.println(orig[ray.getPos().x + (width*ray.getPos().y)]);
		if(orig[ray.getPos().x + (width*ray.getPos().y)] != -16777216)
			return new Color(orig[ray.getPos().x + (width*ray.getPos().y)]);
		else
			return new Color(oldImg.getRGB(ray.getPos().x, ray.getPos().y));
	}

	
}
