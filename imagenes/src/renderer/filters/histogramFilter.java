package renderer.filters;

import gui.histogram.HistPanel;
import gui.histogram.Histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.color.ColorSpace;

import javax.swing.JFrame;

import core.PixelRay;
import core.Settings;



public class histogramFilter extends Filter {
	Histogram histogram;
	HistPanel panel;
	
	public histogramFilter() {
		JFrame frame = new JFrame("Histogram");
		histogram = new Histogram("Frequency" ,"Grey level",Settings.bins, 0, 1);
		panel = new HistPanel(histogram);
		frame.setSize(600, 400);
		panel.setSize(600, 400);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	
	}
	
	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [3];
		
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
		
		Color.RGBtoHSB((int)red, (int)green, (int)blue, colors);//an array of three elements containing the hue, saturation and brightness (in that order)
		
		
		histogram.add(colors[2]);
		//colors va de 0 a 255 pero en realidad de 0 a 1.
		colors[0] = red /255; 		 
		colors[1] = green /255 ;
		colors[2] = blue /255; 		 
		//System.out.println("red " + colors[0] + "green " + colors[1] + "blue " + colors[2]  );
		Color c = new Color(space, colors, 1 );
		return c;

	}

	
}
