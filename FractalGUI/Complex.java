
/*
 * Complex numbers class to perform necessary calculations
 */
public class Complex 
{
	
	double realPart;
	double imagPart;
	
	public Complex(){
		realPart = 0;
		imagPart = 0;
	}
	
	public Complex(double real, double imag)
	{
		realPart = real;
		imagPart = imag;
	}
	
	public double getReal()	{	return realPart;	}
	
	public double getImag()	{	return imagPart;	}
	
	
	public void square()
	{
		double real = realPart;
		double imag = imagPart;
		
		realPart = ((real*real)-(imag*imag));
		imagPart = ((real*imag)+(real*imag));
	}
	
	public double modSquare()
	{
		double x = ((getReal()*getReal() + getImag()*getImag()));
		return x;
		
	}
	
	public void add(Complex d)
	{
		realPart = (realPart + d.getReal());
		imagPart = (imagPart + d.getImag());
		
	}
}
