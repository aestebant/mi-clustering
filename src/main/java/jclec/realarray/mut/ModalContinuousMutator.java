package jclec.realarray.mut;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Modal Continual Mutation.
 *
 * @author Alberto Lamarca 
 * @author Sebastian Ventura
 */

public class ModalContinuousMutator extends ModalMutator 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by eclipse */
	
	private static final long serialVersionUID = 1982401789338573017L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Upper limit Limits of the triangular distribution */
	
	protected transient double upper;
	
	/** Half Point of the triangular distribution */

	protected transient double half;		
	
	/**  Distribution Density of half point */
	
	protected transient double half_density;		
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public ModalContinuousMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// java.lang.Object methods

	public boolean equals(Object other)
	{
		if (other instanceof ModalContinuousMutator) {
			ModalContinuousMutator o = (ModalContinuousMutator) other;
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(locusMutProb, o.locusMutProb);
			eb.append(Bm, o.Bm);
			eb.append(minimumRange, o.minimumRange);
			eb.append(mutationRange, o.mutationRange);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}	
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	///////////////////////////////////////////////////////////////// 
	
	// AbstractMutator methods
	
	/**
	 * Call super() method, then init triangular distribution params.
	 * 
	 * {@inheritDoc}
	 */

	@Override
	protected void prepareMutation() 
	{
		// Call super method
		super.prepareMutation();
		// Limits of the triangular distribution
		upper = 0.5*(Bm-1); 	// Upper limit 		
		half = upper/2;			// Half Point
		//  Distribution Density of half point
		half_density = 1/half;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void doLocusMutation(double[] parentChromosome, double[] mutantChromosome, int locusIndex) 
	{
		double rang = mutationRange * genotypeSchema[locusIndex].efWidth();
		int pi=(int) (Math.log(minimumRange)/Math.log(Bm));
		double gamma = 0;
		for (int k=0 ; k>=pi;k--) {
			if(randgen.raw()<0.0625) {
				double prob, point1, point2;
				double extinf=(Math.pow(Bm,k)-Math.pow(Bm,k-1))/2;
				double extsup=(Math.pow(Bm,k+1)-Math.pow(Bm,k))/2;
				if(extsup<=half) {
					point1=(half_density/half)*extinf;
					point2=(half_density/half)*extsup;
					prob=(point1+point2)*(extsup-extinf)/2;
				}
				else if(extinf>=half) {
					point1=((-half_density/(upper-half))*extinf)+
					half_density+(half*half_density/(upper-half));
					point2=((-half_density/(upper-half))*extsup)+
								half_density+(half*half_density/(upper-half));
					prob=(point1+point2)*(extsup-extinf)/2;
				}
				else {
					point1=(half_density/half)*extinf;
					point2=((-half_density/(upper-half))*extsup)+
								half_density+(half*half_density/(upper-half));
					prob=(point1+half_density)*(half-extinf)/2;
					prob+=(point2+half_density)*(extsup-half)/2;
				}
				gamma+=prob;
			}
		}
		double newValue;
		if (randgen.coin()) {
			newValue = parentChromosome[locusIndex] + rang*gamma;
		}
		else {
			newValue = parentChromosome[locusIndex] - rang*gamma;
		}
		// Check interval locus
		mutantChromosome[locusIndex] = genotypeSchema[locusIndex].nearestOf(newValue);			
	}

	// UniformMutator methods 
	
	@Override
	protected double defaultLocusMutProb() 
	{
		return 0.6;
	}

	// ModalMutator methods
	
	@Override
	protected double defaultBm() 
	{
		return 2.0;
	}

	@Override
	protected double defaultMutationRange() 
	{
		return 0.1;
	}

	@Override
	protected double defaultMinimumRange() 
	{
		return 1E-05;
	}
}