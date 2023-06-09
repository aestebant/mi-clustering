package jclec.binarray.mut;

import jclec.IConfigure;
import jclec.binarray.BinArrayIndividual;
import jclec.binarray.BinArrayMutator;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Several loci mutator for BinArrayIndividual and subclasses.
 * 
 * @author Sebastian Ventura
 */

public class SeveralLociMutator extends BinArrayMutator implements IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 3258415014955071027L;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Number of mutation points */
	
	private int numberOfMutationPoints;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Mutation points */
	
	private transient int [] mp;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor.
	 */
	
	public SeveralLociMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting and getting properties
	
	/**
	 * @return Number of loci to mutate
	 */
	
	public int getNumberOfMutationPoints() 
	{
		return numberOfMutationPoints;
	}

	/**
	 * Sets the number of loci to mutate
	 * 
	 * @param nofmp Number of mutation points
	 */
	
	public void setNumberOfMutationPoints(int nofmp) 
	{
		this.numberOfMutationPoints = nofmp;
	}

	// IConfigure interface

	/**
	 * Configuration method.
	 * 
	 * Configuration parameters for SeveralLociMutator are:
	 * 
	 * <ul>
	 * <li>
	 * <code>[@evaluate]: boolean (default = true)</code></p> 
	 * If this parameter is set to <code>true</true> individuals will
	 * be evaluated after its creation. 
	 * </li>
	 * <li>
	 * <code>[@number-of-mutation-points]: integer</code></p>
	 * Number of mutation points. 
	 * </li>
	 * </ul>
	 */
	
	public void configure(Configuration configuration) 
	{
		// Get the '[@number-of-mutation-points]' property
		int numberOfMutationPoints = configuration.getInt("[@number-of-mutation-points]");
		setNumberOfMutationPoints(numberOfMutationPoints);
	}
	
	// java.lang.Object methods

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof SeveralLociMutator) {
			SeveralLociMutator o = (SeveralLociMutator) other;
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(numberOfMutationPoints, o.numberOfMutationPoints);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this);
		tsb.append(numberOfMutationPoints);
		return tsb.toString();
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	// AbstractMutator methods

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void mutateNext() 
	{
		// Genome length
		int gl = species.getGenotypeLength();
		// Individual to mutate
		BinArrayIndividual mutant = 
			(BinArrayIndividual) parentsBuffer.get(parentsCounter);
		// Creates mutant genotype
		byte [] mgenome = new byte[gl];
		System.arraycopy(mutant.getGenotype(), 0, mgenome, 0, gl);
		// Choose mutation point
		setMpoints(gl);
		// Flip selected point
		for (int i=0; i<numberOfMutationPoints; i++)
			flip(mgenome, mp[i]);
		// Returns mutant
		sonsBuffer.add(species.createIndividual(mgenome));
	}

	/*
	 * El mtodo realiza las siguientes acciones:
	 * 
	 * 1) Crea el genotipo del individuo, copiando el del padre
	 * 2) Elige varios punto de mutacin al azar (tantos como indica 
	 *    el parmetro number-of-mutation-points), invocando al mtodo 
	 *    getMutableLocus()
	 * 3) Muta los loci elegidos, invocando a flip(int)
	 */

	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////

	/**
	 * Set mutation points
	 * 
	 * @param gl Genotype length
	 */
	
	private final void setMpoints(int gl) 
	{
		// Allocate space for mp
		if (mp == null) {
			mp = new int[numberOfMutationPoints];		
		}
		// Assign mutation points
		for (int i=0; i<numberOfMutationPoints; i++) {
			while(true){
				mp[i] = randgen.choose(0, gl);
				boolean exit = true;
				for (int j=0; j<i; j++) {
					if (mp[i] == mp[j]) {
						exit = false;
						break;
					}
				}
				if (exit) break;
			}
		}
	}
}
