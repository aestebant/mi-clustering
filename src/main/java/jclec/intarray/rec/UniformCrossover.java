package jclec.intarray.rec;

import jclec.IConfigure;
import jclec.IIndividual;
import jclec.intarray.IntArrayIndividual;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Uniform crossover for IntArrayIndividual and its subclasses
 *
 * @author Sebastian Ventura
 */

public class UniformCrossover extends IntArrayRecombinator implements IConfigure {


    /////////////////////////////////////////////////////////////////
    // --------------------------------------------------- Properties
    /////////////////////////////////////////////////////////////////

    /**
     * Crossover probability
     */
    private double locusCrossoverProb;

    /////////////////////////////////////////////////////////////////
    // ------------------------------------------------- Constructors
    /////////////////////////////////////////////////////////////////

    /**
     * Empty constructor
     */
    public UniformCrossover() {
        super();
    }

    /////////////////////////////////////////////////////////////////
    // ----------------------------------------------- Public methods
    /////////////////////////////////////////////////////////////////

    // Setting and getting properties

    /**
     * @return Returns the crossprob.
     */
    public final double getLocusCrossoverProb() {
        return locusCrossoverProb;
    }

    /**
     * @param crossProb New crossover probability
     */
    public final void setLocusCrossoverProb(double crossProb) {
        this.locusCrossoverProb = crossProb;
    }

    // IConfigure interface

    /**
     * Configuration method.
     * <p>
     * Configuration parameters for UniformCrossover are:
     *
     * <ul>
     * <li>
     * <code>[@evaluate]: boolean (default = true)</code></p>
     * If this parameter is set to <code>true</true> individuals will
     * be evaluated after its creation.
     * </li>
     * <li>
     * <code>[@locus-crossover-prob]: double (default = 0.5)</code></p>
     * Locus crossover probability.
     * </li>
     * <li>
     * <code>random-generator: complex</code></p>
     * Random generator used in individuals mutation.
     * <ul>
     * <li>
     * <code>random-generator[@type] String (default 'jclec.random.Ranecu')</code>
     * </li>
     * </ul>
     * </li>
     * </ul>
     */
    public void configure(Configuration configuration) {
        // Get the 'locus-crossover-prob' property
        double locusCrossoverProb = configuration.getDouble("[@locus-rec-prob]", 0.5);
        setLocusCrossoverProb(locusCrossoverProb);
    }

    // java.lang.Object methods
    @Override
    public boolean equals(Object other) {
        if (other instanceof UniformCrossover) {
            // Type conversion
            UniformCrossover cother = (UniformCrossover) other;
            // Equals Builder
            EqualsBuilder eb = new EqualsBuilder();
            eb.append(locusCrossoverProb, cother.locusCrossoverProb);
            // Returns
            return eb.isEquals();
        } else {
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
    protected void recombineNext() {
        List<IIndividual> parents = new ArrayList<>(2);
        parents.add(parentsBuffer.get(parentsCounter));
        parents.add(parentsBuffer.get(parentsCounter + 1));
        sonsBuffer.addAll(recombineInd(parents));
    }

    @Override
    protected List<IIndividual> recombineInd(List<IIndividual> individuals) {
        // Genotype length
        int gl = species.getGenotypeLength();
        // Parents genotypes
        int[] p0_genome = ((IntArrayIndividual) individuals.get(0)).getGenotype();
        int[] p1_genome = ((IntArrayIndividual) individuals.get(1)).getGenotype();
        // Creating sons genotypes
        int[] s0_genome = new int[gl];
        int[] s1_genome = new int[gl];
        // Building sons
        for (int i = 0; i < gl; i++) {
            if (randgen.coin(locusCrossoverProb)) {
                s0_genome[i] = p1_genome[i];
                s1_genome[i] = p0_genome[i];
            } else {
                s0_genome[i] = p0_genome[i];
                s1_genome[i] = p1_genome[i];
            }
        }
        // Put sons in sincell
        List<IIndividual> sons = new ArrayList<>(2);
        sons.add(species.createIndividual(s0_genome));
        sons.add(species.createIndividual(s1_genome));
        return sons;
    }
}
