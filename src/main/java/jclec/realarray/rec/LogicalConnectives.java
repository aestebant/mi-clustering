package jclec.realarray.rec;

/**
 * Logical Connectives
 * 
 * @author Alberto Lamarca-Rosales 
 * @author Sebastian Ventura
 * 
 * @version 1.0
 */

public class LogicalConnectives extends AbstractFuzzyConnectives 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -5918263166262566254L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public LogicalConnectives() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ---------------------------------- AbstractConnectives methods
	/////////////////////////////////////////////////////////////////

	/**
	 * Logical Function m
	 */
	
	public double m(double x, double y) 
	{
		return ( (1-alpha)*x + alpha*y );
	}
	
	/**
	 * Logical Function s
	 */
	
	public double s(double x, double y)
	{
		if (x>y)
			return x;
		else
			return y;
	}
	
	/**
	 * Logical Function f
	 */
	
	public double f(double x, double y) 
	{
		if (x<y)
		    return x;
		  else
		    return y;
	}
	
	/**
	 * Logical Function l
	 */
	
	public double l(double x, double y) 
	{
		return Math.pow(f(x,y),(1-alpha))*Math.pow(s(x,y),alpha);
	}
}
