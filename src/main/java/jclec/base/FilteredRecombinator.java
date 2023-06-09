package jclec.base;

import jclec.IIndividual;
import jclec.ISystem;
import jclec.util.random.IRandGen;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Filtered recombinator. Recombines individuals with a given recombination probability.
 *
 * @author Sebastian Ventura
 */

public class FilteredRecombinator extends DecoratedRecombinator {
    /////////////////////////////////////////////////////////////////
    // --------------------------------------- Serialization constant
    /////////////////////////////////////////////////////////////////

    /**
     * Generated by Eclipse
     */

    private static final long serialVersionUID = 9168537902966940372L;

    /////////////////////////////////////////////////////////////////
    // --------------------------------------------------- Properties
    /////////////////////////////////////////////////////////////////

    /**
     * Mutation probability
     */

    protected double recProb;

    /////////////////////////////////////////////////////////////////
    // ------------------------------------------- Internal variables
    /////////////////////////////////////////////////////////////////

    // Operator state

    /**
     * Random generator
     */

    protected IRandGen randgen;

    // Auxiliary variables

    /**
     * Fertile parents set
     */

    protected transient List<IIndividual> fertile = new ArrayList<>();

    /**
     * Sterile parents set
     */

    protected transient List<IIndividual> sterile = new ArrayList<>();

    /////////////////////////////////////////////////////////////////
    // ------------------------------------------------- Constructors
    /////////////////////////////////////////////////////////////////

    /**
     * Empty constructor
     */

    public FilteredRecombinator() {
        super();
    }

    /**
     * Constructor that contextualize this operator.
     *
     * @param context Execution context
     */

    public FilteredRecombinator(ISystem context) {
        super();
        contextualize(context);
    }

    /////////////////////////////////////////////////////////////////
    // ----------------------------------------------- Public methods
    /////////////////////////////////////////////////////////////////

    // Setting and getting properties

    /**
     * Access to "recProb" property.
     *
     * @return Actual recombination probability
     */

    public final double getRecProb() {
        return recProb;
    }

    /**
     * Sets the "recProb" property.
     *
     * @param recProb New recombination probability
     */

    public final void setRecProb(double recProb) {
        this.recProb = recProb;
    }

    // IRecombinator interface

    /**
     * Take a random generator and contextualize decorated recombinator
     * (if exists)
     * <p>
     * {@inheritDoc}
     */

    @Override
    public void contextualize(ISystem context) {
        // Call super method
        super.contextualize(context);
        // Take a random generator
        randgen = context.createRandGen();
    }

    /**
     * This operator perform ...
     * <p>
     * {@inheritDoc}
     */

    public List<IIndividual> recombine(List<IIndividual> parents) {
        // Clear fertile and sterile sets
        fertile.clear();
        sterile.clear();
        // Build fertile and sterile sets
        for (IIndividual ind : parents) {
            if (randgen.coin(recProb)) {
                fertile.add(ind);
            } else {
                sterile.add(ind);
            }
        }
        // Ensures that fertile contains an exact number
        // of parents. Else, remove unnecessary parents,
        // putting them in sterile set
        int rest = fertile.size() % decorated.getPpl();
        if (rest != 0) {
            for (int i = 0, j = fertile.size() - 1; i < rest; i++, j--) {
                sterile.add(fertile.remove(j));
            }
        }
        // Returns generated sons
        return decorated.recombine(fertile);
    }

    // Other methods

    /**
     * Access to the sterile parents set.
     *
     * @return Sterile parents set
     */

    public List<IIndividual> getSterile() {
        return sterile;
    }

    /**
     * Access to the fertile parents set.
     *
     * @return Fertile parents set
     */

    public List<IIndividual> getFertile() {
        return fertile;
    }

    // IConfigure interface

    public void configure(Configuration settings) {
        // Call super method
        super.configure(settings);
        // Get mutation probability
        double recProb = settings.getDouble("rec-prob", 1);
        setRecProb(recProb);
    }

    // java.lang.Object methods

    /**
     * Compare decorated mutator and mutation probability.
     * <p>
     * {@inheritDoc}
     */

    public boolean equals(Object other) {
        if (other instanceof FilteredRecombinator) {
            FilteredRecombinator o = (FilteredRecombinator) other;
            EqualsBuilder eb = new EqualsBuilder();
            eb.append(decorated, o.decorated);
            eb.append(recProb, o.recProb);
            return eb.isEquals();
        } else {
            return false;
        }
    }
}
/*
 * Los objetos FilteredRecombinator no cruzan a todos los individuos que
 * reciben a travs del mtodo recombine(), sino que actan sobre un
 * porcentaje de ellos (definido por la probabilidad de cruce) elegidos
 * al azar. Para ello, realizan en primer lugar un filtrado del conjunto
 * recibido, pasando el resultado de este filtrado al operador de cruce
 * que decoran. Como funcionalidad adicional, el operador proporciona dos
 * mtodos, getSterile() y getFertile(), que dan acceso a los dos conjuntos
 * formados a partir del conjunto original (los que han originado nuevos
 * individuos y los que no han pasado al operador de mutacin)
 *
 * NOTA: El funcionamiento de este operador asegura que el nmero de
 * individuos que se pasa al operador de cruce es mltiplo del nmero de
 * padres por camada...
 */