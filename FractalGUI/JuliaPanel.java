import java.awt.Color;
import java.awt.Graphics;

/*
 * almost identical to MandelPanel class, same paintComponent, createComplex method, checkIterations slightly different
 * as class uses the user selected point
 */

@SuppressWarnings("serial")
public class JuliaPanel extends MandelPanel //so all variables in mandelpanel are visible - reduces code duplication and makes things like selPoint available for class
{
	Complex selPoint;
	int iterations = MandelPanel.DEFAULT_ITERATIONS;

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
	
	public void update(Complex c)
	{
		selPoint = c;
		repaint();
	}
	
	public int checkIterations(Complex c) //returns an int which is the number of iterations before the complex number escapes to infinity
	{
		if(selPoint == null) return 0;
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
				c1.add(selPoint);
			}
		}

		return iterations;
	}
	
	
	public Complex createComplex(double x, double y) //creates a complex number from the relevant dot in the panel
	{	
		double width = getWidth();
		double height = getHeight();
		
		double real = (-2.0) + x / width * ((2.0) - (-2.0));
		double imag = (1.6) - y / height * ((1.6) - (-1.6));
		
		
		return new Complex(real, imag);
	}
}
