package jclec.problem.util.dataset.attribute;

import jclec.util.range.Closure;
import jclec.util.range.IRange;
import jclec.util.range.Interval;

import java.text.DecimalFormat;

/**
 * Numerical (real) attribute
 * 
 * @author Alberto Cano 
 * @author Amelia Zafra
 * @author Sebastian Ventura
 * @author Jose M. Luna 
 * @author Juan Luis Olmo
 */

public class NumericalAttribute extends AbstractAttribute
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
	
	/** Range of allowed values */
	
	protected IRange range;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Constructor
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor.
	 */
	
	public NumericalAttribute()
	{
		super();		
	}
	
	/**
	 * Constructor that sets attribute name and range of allowed values
	 * 
	 * @param name Attribute name
	 * @param range Range of allowed values
	 */
	
	public NumericalAttribute(String name, IRange range)
	{
		super(name);
		setRange(range);
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	public void setInterval(Interval interval) {
		this.interval = interval;
	}
	
	/**
	 * Return range of allowed values
	 * 
	 * @return Range of allowed values
	 */
	
	public IRange getRange() {
		return range;
	}
	
	/**
	 * Set range of allowed values
	 * 
	 * @param range Range of allowed values
	 */
	
	public void setRange(IRange range) {
		this.range = range;
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
		return AttributeType.Numerical;
	}
	
	/**
	 * Parse the attribute
	 * 
	 * @param externalValue the value
	 */

	@Override
	public double parse(String externalValue) {
		return Double.parseDouble(externalValue);
	}
	
	/**
	 * Shows the real value associated to the internal value
	 * 
	 * @param internalValue Internal value to show
	 * 
	 * @return The real value of the attribute using a format
	 */

	@Override
	public String show(double internalValue) {
		return new Double(internalValue).toString();
	}
	
	/**
	 * Shows the real value associated to the internal value using a format
	 * 
	 * @param internalValue Internal value to show
	 * @param format Format
	 * 
	 * @return The real value of the attribute
	 */
	
	public String show(double internalValue, String format) {		
		DecimalFormat df = new DecimalFormat(format);
		return df.format(new Double(internalValue));
	}
	
	/**
	 * Get the internal values interval
	 * 
	 * @return interval
	 */

	public Interval intervalValues()
	{
		if(interval == null)
			interval = new Interval(0,Double.MAX_VALUE, Closure.ClosedClosed);
		
		return interval;
	}
}