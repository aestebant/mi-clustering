package jclec.util.random;

/**
 * Ranmar generators factory.
 * 
 * @author Sebastian Ventura
 */

public class RanmarFactory extends AbstractRandGenFactory 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -2666769126275790375L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty (default) constructor.
	 */
	
	public RanmarFactory() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ------------------------- Implementing IRandGenFactory methods 
	/////////////////////////////////////////////////////////////////
	
	/**
	 * {@inheritDoc}
	 */
	
	public IRandGen createRandGen() 
	{
		return new Ranmar(seedGenerator.nextSeed());
	}
}
