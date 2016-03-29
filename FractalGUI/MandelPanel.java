import javax.swing.*;
import java.awt.*;



/*
 * Class to draw Mandelbrot on panel - doesn't contain any listeners as they are all handled in the main panel
 * 
 */

@SuppressWarnings("serial")
public class MandelPanel extends JPanel
{
	//variables are static so other classes can access - hasn't caused any problems
	
	final static double DEFAULT_xMax = 2.0;
	public static double xMax = DEFAULT_xMax;
	
	final static double DEFAULT_xMin = -2.0;
	public static double xMin = DEFAULT_xMin;

	final static double DEFAULT_yMax = 1.6;
	public static double yMax = DEFAULT_yMax;
	
	final static double DEFAULT_yMin = -1.6;
	public static double yMin = DEFAULT_yMin;
	
	final static int DEFAULT_ITERATIONS = 100;
	public static int iterations = DEFAULT_ITERATIONS;
	
	
	public void paintComponent(Graphics g)
	{
		for(int x = 0; x < getWidth(); x++)
		{
			for(int y = 0; y < getHeight(); y++)
			{
				Complex current = createComplex(x,y);
				int itr = checkIterations(current);
				
				//sets colour to black if the pixel never escapes to infinity
				if(itr == iterations) 
				{
					g.setColor(Color.BLACK);
				}
				//selection of other colours depending on how many iterations it takes to escape to infinity
				else
				{
					 g.setColor(Color.getHSBColor(itr/100.0f, 1.0f, 1.0f));
				}
				g.drawLine(x, y, x, y);
				
			}
		}
	}
	//returns an int which is the number of iterations before the complex number escapes to infinity
	public int checkIterations(Complex c) 
	{
		Complex c1 = new Complex(c.getReal(), c.getImag());
		
		for(int i = 0; i < iterations; i++)
		{
			if(c1.modSquare() > 4)
			{
				return i;
			}
			else
			{
				c1.square();
				c1.add(c);
			}
		}

		return iterations;
	}
	
	//creates a complex number from the relevant dot in the panel
	public Complex createComplex(double x, double y) 
	{	
		double width = getWidth();
		double height = getHeight();
		
		double real = xMin + x / width * (xMax - xMin);
		double imag = yMax - y / height * (yMax - yMin);
		
		
		return new Complex(real, imag);
	}
	
	public void update()
	{
		repaint();
	}
	
}
	
	