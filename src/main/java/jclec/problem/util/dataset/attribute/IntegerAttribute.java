package jclec.problem.util.dataset.attribute;

import jclec.util.intset.Closure;
import jclec.util.intset.Interval;

/**
 * Integer attribute
 * 
 * @author Alberto Cano 
 * @author Amelia Zafra
 * @author Sebastian Ventura
 * @author Jose M. Luna 
 * @author Juan Luis Olmo
 */

public class IntegerAttribute extends AbstractAttribute
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -6328412187422731602L;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables 
	/////////////////////////////////////////////////////////////////

	/** Real values range */
	
	private Interval interval;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Constructor
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor.
	 */
	
	public IntegerAttribute()
	{
		super();		
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Set the interval
	 *
	 * @param interval the interval
	 */
	
	public void setInterval(Interval interval) {
		this.interval = interval;
	}
	
	/////////////////////////////////////////////////////////////////
	// ------------------------ Overwriting AbstractAttribute methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Get the attribute type
	 * 
	 * @return IntegerNumerical
	 */
	
	@Override
	public AttributeType getType() {
		return AttributeType.Integer;
	}
	
	/**
	 * Parse the attribute
	 * 
	 * @param value the value
	 */

	@Override
	public double parse(String value) {
		return Double.parseDouble(value);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public String show(double internalValue) {
		return new Integer(((Double) internalValue).intValue()).toString();
	}
	
	/**
	 * Get the internal values interval
	 * 
	 * @return interval
	 */

	public Interval intervalValues()
	{
		if(interval == null)
			interval = new Interval(0, Integer.MAX_VALUE, Closure.ClosedClosed);
		
		return interval;
	}
}