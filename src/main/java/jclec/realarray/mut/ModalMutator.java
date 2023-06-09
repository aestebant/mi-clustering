package jclec.realarray.mut;

import jclec.realarray.UniformMutator;
import org.apache.commons.configuration.Configuration;

/**
 * Root class for ModalDiscrete and ModalContinuous mutators
 *
 * @author Sebastian Ventura
 */

public abstract class ModalMutator extends UniformMutator 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by eclipse */
	
	private static final long serialVersionUID = 1982401789338573017L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/**Mutation base */
	
	protected double Bm;

	 /** Mutation range*/
	
	protected double mutationRange;
	
	/** Lower limit of mutation range */
	
	protected double minimumRange;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public ModalMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setting and getting properties

	/**
	 * @return Returns Mutation range
	 */
	
	public double getMutationRange() 
	{
		return mutationRange;
	}
	
	/**
	 * @param mutationRange Mutation range to set.
	 */
	
	public void setMutationRange(double mutationRange) 
	{
		this.mutationRange = mutationRange;
	}
	
	/**
	 * @return Returns mutation base
	 */
	
	public double getBm() 
	{
		return Bm;
	}
	
	/**
	 * @param bm Mutation base to set.
	 */
	
	public void setBm(double bm) 
	{
		this.Bm = bm;
	}
	
	/**
	 * @return Returns Lower limit of mutation range 
	 */
	
	public double getMinimumRange() 
	{
		return minimumRange;
	}
	
	/**
	 * @param rangmin Lower limit of mutation range to set.
	 */
	
	public void setMinimumRange(double rangmin) 
	{
		this.minimumRange = rangmin;
	}
	
	// IConfigure interface

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public void configure(Configuration settings)
	{
		// Call super.configure method
		super.configure(settings);
		// set mutation range
		double mutationRange = settings.getDouble("mutation-range", defaultMutationRange());
		setMutationRange(mutationRange);
		// set mutation base
		double bm = settings.getDouble("bm", defaultBm());
		setBm(bm);
		// Set lower limit
		double rangmin = settings.getDouble("minimumRange", defaultMinimumRange());
		setMinimumRange(rangmin);
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	protected abstract double defaultMutationRange();

	protected abstract double defaultBm();

	protected abstract double defaultMinimumRange();
}
