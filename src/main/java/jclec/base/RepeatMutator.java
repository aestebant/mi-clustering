package jclec.base;

import jclec.IIndividual;
import jclec.ISystem;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Repeat Mutator.
 * 
 * Applies a mutator several times over the same individuals set. The number of
 * repetitions is a configuration parameter.
 * 
 * @author Sebastian Ventura
 */

public class RepeatMutator extends DecoratedMutator 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 7926409688402274090L;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties 
	/////////////////////////////////////////////////////////////////
	
	/** Number of repetitions */
	
	protected int numberOfRepetitions;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors 
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor.
	 */
	
	public RepeatMutator() 
	{
		super();
	}

	/**
	 * Empty constructor.
	 */
	
	public RepeatMutator(ISystem system) 
	{
		super(system);
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods 
	/////////////////////////////////////////////////////////////////
	
	// Setting and getting properties
	
	public final int getNumberOfRepetitions() 
	{
		return numberOfRepetitions;
	}

	public final void setNumberOfRepetitions(int numberOfRepetitions) 
	{
		this.numberOfRepetitions = numberOfRepetitions;
	}

	// IConfigure interface
	
	/**
	 * 
	 */
	
	public void configure(Configuration settings)
	{
		// Call super.configure() method
		super.configure(settings);
		// Set number of repetitions
		int numberOfRepetitions = settings.getInt("number-of-repetitions");
		setNumberOfRepetitions(numberOfRepetitions);
	}
	
	// IRecombine interface
	
	public List<IIndividual> mutate(List<IIndividual> parents) 
	{
		// Allocaet space for result
		ArrayList<IIndividual> result = new ArrayList<IIndividual>();
		// Performs decorated recombination k times
		for (int i=0; i<numberOfRepetitions; i++) {
			result.addAll(decorated.mutate(parents));
		}
		// Return result
		return result;
	}
	
	// java.lang.Object methods
	
	/**
	 * Compare decorated mutator and mutation probability.
	 * 
	 * {@inheritDoc} 
	 */
	
	public boolean equals(Object other)
	{
		if (other instanceof RepeatMutator) {
			RepeatMutator o = (RepeatMutator) other;
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(decorated, o.decorated);
			eb.append(numberOfRepetitions, o.numberOfRepetitions);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}
}
