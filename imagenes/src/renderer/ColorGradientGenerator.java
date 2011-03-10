package renderer;

import java.awt.Color;
import java.awt.color.ColorSpace;

import core.PixelRay;
import core.Settings;



public class ColorGradientGenerator extends Renderer {

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [3];
		//colors va de 0 a 255
		colors[0] = (float) ray.getPos().x /Settings.getResolution().x ; 		 
		colors[1] = ((float) ray.getPos().y )/ Settings.getResolution().y;
		colors[2] = (( 1 - (float) ray.getPos().x /Settings.getResolution().x + ( (float) ray.getPos().y )/ Settings.getResolution().y) / 2); 		 
		
		Color c = new Color(space, colors, 1 );
		return c;

	}

	
}
