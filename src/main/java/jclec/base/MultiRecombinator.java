package jclec.base;

import jclec.IConfigure;
import jclec.IIndividual;
import jclec.IRecombinator;
import jclec.ISystem;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Multirecombinator. Apply several recombinators over the same parents set.
 * 
 * @author Sebastian Ventura
 */

public class MultiRecombinator implements IRecombinator, IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -6300502786816813015L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	/** Component mutators */
	
	protected IRecombinator [] components;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Execution context */
	
	protected transient ISystem context;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/** Empty constructor */

	public MultiRecombinator() 
	{
		super();
	}

	/** Contextualized constructor */

	public MultiRecombinator(ISystem context) 
	{
		super();
		contextualize(context);
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting and getting properties

	public final IRecombinator[] getComponents() 
	{
		return components;
	}

	public final void setComponents(IRecombinator[] components) 
	{
		// Assign components
		this.components = components;
		// Contextualize (if necessary)
		if (context != null) {
			for (IRecombinator component : components) component.contextualize(context);
		}
	}

	// IMutator interface
	
	/**
	 * {@inheritDoc}
	 */
	
	public void contextualize(ISystem context) 
	{
		// Set execution context
		this.context = context;
		// Contextualize components (if necessary)
		if (components != null) {
			for (IRecombinator component : components) component.contextualize(context);
		}
	}

	public int getPpl() 
	{
		throw new UnsupportedOperationException
			("Number of parent per litter depends on component recombinators");
	}

	public int getSpl() 
	{
		throw new UnsupportedOperationException
			("Number of sons per litter depends on component recombinators");
	}

	public List<IIndividual> recombine(List<IIndividual> parents) 
	{
		// Allocate space for result
		ArrayList<IIndividual> result = new ArrayList<IIndividual> ();
		// Apply component mutators over parents and
		// add all mutated individuals to result
		for (IRecombinator component : components) {
			result.addAll(component.recombine(parents));
		}
		// Return result
		return result;
	}

	// IConfigure interface
	
	/**
	 * Configuration parameters are...
	 */
	
	@SuppressWarnings("unchecked")
	public void configure(Configuration settings)
	{
		// Number of mutator components
		int numberOfComponents = settings.getList("component[@type]").size();
		// Allocate space for components
		IRecombinator [] components = new IRecombinator[numberOfComponents];
		for (int i=0; i<numberOfComponents; i++) {
			// Header
			String header = "component("+i+")";
			try {
				// Component classname
				String componentClassname = settings.getString(header+"[@type");
				// Component class
				Class<IRecombinator> componentClass = 
					(Class<IRecombinator>) Class.forName(componentClassname);				
				// Component instance
				IRecombinator component = componentClass.newInstance();
				// Configure component (if necessary)
				if (component instanceof IConfigure) {
					((IConfigure) component).configure(settings.subset(header));
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
			catch (InstantiationException e) {
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// Set components
		setComponents(components);
	}

	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		if (other instanceof MultiRecombinator) {
			MultiRecombinator o = (MultiRecombinator) other;
			int cl = components.length;
			if (cl == o.components.length) {
				EqualsBuilder eb = new EqualsBuilder();
				for (int i=0; i<cl; i++) 
					eb.append(components[i], o.components[i]);
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
}
