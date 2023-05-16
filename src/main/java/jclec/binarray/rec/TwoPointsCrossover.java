package jclec.binarray.rec;

import jclec.binarray.BinArrayIndividual;
import jclec.binarray.BinArrayRecombinator;

/**
 * Two points crossover operator for BinArrayIndividual and its subclasses.
 * 
 * @author Sebastian Ventura
 */

public class TwoPointsCrossover extends BinArrayRecombinator
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 3835150645048325173L;	
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	
	public TwoPointsCrossover() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// java.lang.Object methods

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof TwoPointsCrossover) {
			return true;
		}
		else {
			return false;
		}
	}	

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	// AbstractRecombinator methods

	/**
	 * {@inheritDoc}
	 */

	@Override
	protected void recombineNext() 
	{
		int gl = species.getGenotypeLength();
		// Parents conversion
		BinArrayIndividual p0 = 
			(BinArrayIndividual) parentsBuffer.get(parentsCounter);
		BinArrayIndividual p1 = 
			(BinArrayIndividual) parentsBuffer.get(parentsCounter+1);
		// Parents genotypes
		byte [] p0_genome = p0.getGenotype();
		byte [] p1_genome = p1.getGenotype();
		// Creating sons genotypes
		byte [] s0_genome = new byte[gl];
		byte [] s1_genome = new byte[gl];
		// Taking a crossover point
		int cp1, cp2;
		cp1 = randgen.choose(1, gl-2);
		cp2 = randgen.choose(cp1, gl-1);
		// First son
		System.arraycopy(p0_genome, 0,   s0_genome, 0,   cp1);
		System.arraycopy(p1_genome, cp1, s0_genome, cp1, cp2-cp1);
		System.arraycopy(p0_genome, cp2, s0_genome, cp2, gl-cp2);
		// Second son
		System.arraycopy(p1_genome, 0,   s1_genome, 0,   cp1);
		System.arraycopy(p0_genome, cp1, s1_genome, cp1, cp2-cp1);
		System.arraycopy(p1_genome, cp2, s1_genome, cp2, gl-cp2);
		// Put sons in s
		sonsBuffer.add(species.createIndividual(s0_genome));
		sonsBuffer.add(species.createIndividual(s1_genome));
	}

	/*
	 * El mtodo realiza las siguientes acciones:
	 * 
	 * 1) Crea los genotipos de los hijos
	 * 2) Elige los puntos de cruce al azar, generando valores aleatorios
	 *    en el rango [1,genome.length-2] u [cp1, genome.length-1]
	 * 3) Construye los nuevos individuos. Los fragmentos que se utilizan
	 *    en la construccin de los nuevos individuos son:
	 *    a) Desde 0 hasta cp1-1 (ambos inclusive)
	 *    b) Desde cp1 hasta cp2-1 (ambos inclusive)
	 *    c) Desde cp2 hasta genome.length-1 (ambos inclusive)
	 */
}
