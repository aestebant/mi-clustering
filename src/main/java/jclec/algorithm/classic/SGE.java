package jclec.algorithm.classic;

import jclec.IIndividual;
import jclec.selector.BettersSelector;
import jclec.selector.WorsesSelector;

/**
 * <strong><u>S</u></strong>imple  <strong><u>G</u></strong>enerational and
 * <strong><u>E</u></strong>litist algorithm. Like a generational algorithm,
 * but ensures that best individual passes to the next generation any time.
 *
 * @author Sebastian Ventura
 */

public class SGE extends SG {

    /////////////////////////////////////////////////////////////////
    // ------------------------------------------- Internal variables
    /////////////////////////////////////////////////////////////////

    /**
     * Betters selector. Used in update phase
     */
    private BettersSelector bettersSelector = new BettersSelector(this);

    /**
     * Worses selector. Used in update phase
     */
    private WorsesSelector worsesSelector = new WorsesSelector(this);

    /////////////////////////////////////////////////////////////////
    // ------------------------------------------------- Constructors
    /////////////////////////////////////////////////////////////////

    /**
     * Empty (default) constructor
     */
    public SGE() {
        super();
    }

    /////////////////////////////////////////////////////////////////
    // -------------------------------------------- Protected methods
    /////////////////////////////////////////////////////////////////

    /**
     * Like SG, but ensuring that current best individual pass to the new population.
     */
    @Override
    protected void doUpdate() {
        IIndividual bestb = bettersSelector.select(bset, 1).get(0);
        IIndividual bestc = bettersSelector.select(cset, 1).get(0);
        // If best individual in b set (bestb) is better that best
        // individual in c set (bestc), remove worst individual in
        // c set (worstc) and add bestb to c set
        if (evaluator.getComparator().compare(bestb.getFitness(), bestc.getFitness()) > 0) {
            IIndividual worstc = worsesSelector.select(cset, 1).get(0);
            cset.remove(worstc);
            cset.add(bestb);
        }
        // Sets new bset
        bset = cset;
        // Clear pset, rset & cset
        pset = null;
        rset = null;
        cset = null;
    }
}
