package jclec.fitness;

import jclec.IFitness;
import jclec.base.AbstractFitness;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Composite fitness.
 *
 * @author Sebastian Ventura
 */

public class CompositeFitness extends AbstractFitness implements ICompositeFitness
{
    /////////////////////////////////////////////////////////////////
    // --------------------------------------- Serialization constant
    /////////////////////////////////////////////////////////////////
 
	/** Generated by Eclipse */

	private static final long serialVersionUID = 3257291339740559156L;
	
    /////////////////////////////////////////////////////////////////
    // ------------------------------------------- Internal variables
    /////////////////////////////////////////////////////////////////
    
	/** Fitness components */
    
    protected ISimpleFitness [] components;
    
    /////////////////////////////////////////////////////////////////
    // ------------------------------------------------- Constructors
    /////////////////////////////////////////////////////////////////
    
    /**
     * Empty constructor
     */
    
    public CompositeFitness() 
    {
        this(null);
    }

    /**
     * Default constructor. Sets fitness components
     * 
     * @param components Fitness components
     */
    
    public CompositeFitness(ISimpleFitness [] components) 
    {
    	// Call super constructor
        super();
        // Set components
        setComponents(components);
    }
    
    /////////////////////////////////////////////////////////////////
    // ----------------------------------------------- Public methods
    /////////////////////////////////////////////////////////////////
    
    // Setting and getting properties
    
    /**
     * @return This fitness components
     */
    
    public ISimpleFitness[] getComponents() 
    {
        return components;
    }
    
    /**
     * Sets this fitness components.
     *
     * @param components New fitness components.
     */
    
    public void setComponents(ISimpleFitness[] components) 
    {
        this.components = components;
    }
    
    /**
     * Returns a fitness component.
     *
     * @param cidx Index of desired index.
     *
     * @return cidx-throws fitness component
     */
    
    public ISimpleFitness getComponent(int cidx) 
    {
        try {
            return components[cidx];
        }
        catch(IndexOutOfBoundsException e) {
            throw new IllegalArgumentException
            (cidx + "isn't a valid component index");
        }
    }
    
    /**
     * Sets a fitness component.
     *
     * @param cidx Index of desired component
     * @param cval Desired component value.
     */
    
    public void setComponent(int cidx, ISimpleFitness cval) 
    {
        try {
            this.components[cidx] = cval;
        }
        catch(IndexOutOfBoundsException e) {
            throw new IllegalArgumentException
                (cidx + "isn't a valid component index");
        }
    }
    
    // IFitness interface
        
	/**
	 * {@inheritDoc}
	 */
	
	public IFitness copy()
	{
		// New fitness
		CompositeFitness result = new CompositeFitness();
		// Copy components
		int cl = components.length;
		result.components = new ISimpleFitness[cl];
		for (int i=0; i<cl; i++)
			result.components[i] = (ISimpleFitness) components[i].copy();
		// Returns result
		return result;
	}
    
    // java.lang.Object methods
    
	/**
     * {@inheritDoc}
     */
    
	@Override
    public int hashCode() {
        // Hashcode builder
        HashCodeBuilder hcb = new HashCodeBuilder();
        // Append fitness components
        for (ISimpleFitness fitness : components)
            hcb.append(fitness);
        // Return hashcode
        return hcb.toHashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    
    @Override
    public boolean equals(Object oth) {
        if (oth instanceof CompositeFitness) {
            CompositeFitness coth = (CompositeFitness) oth;
            // Comparing number of components
            int cl  = components.length;
            int ocl = coth.components.length;
            if (cl == ocl) {
                EqualsBuilder eb = new EqualsBuilder();
                for (int i=0; i<cl; i++) 
                	eb.append(components[i], coth.components[i]);
                return eb.isEquals();
            }
            else {
                return false;
            }
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
        tsb.append("components", components);
        return tsb.toString();
    }
}
