package jclec.exprtree.fun;

import jclec.exprtree.ExprTree;
import jclec.exprtree.IContext;
import jclec.exprtree.IPrimitive;

import java.util.Iterator;
import java.util.Stack;

/**
 * Expression tree function.
 * 
 * @author Sebastian Ventura
 */

public class ExprTreeFunction implements IContext 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 266592167202045647L;

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------------- Propierties
	/////////////////////////////////////////////////////////////////
	
	/** Function code */
	
	protected ExprTree code;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Execution stack */
	
	protected Stack<Object> stack = new Stack<Object> ();
	
	/** Current arguments */
	
	protected Object [] args;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
		
	/**
	 * Empty (default) constructor
	 */
	
	public ExprTreeFunction() 
	{
		super();
	}
	
	/**
	 * Constructor that sets function code
	 */
	
	public ExprTreeFunction(ExprTree code) 
	{
		super();
		setCode(code);
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Getters and setters
	
	/**
	 * Access to function code
	 * 
	 * @return Current code
	 */
	
	public ExprTree getCode() 
	{
		return code;
	}

	/**
	 * Sets function code
	 * 
	 * @param code Function code
	 */
	
	public void setCode(ExprTree code) 
	{
		this.code = code;
	}

	// Execution method
	
	@SuppressWarnings("unchecked")
	public <E> E execute(Object... arguments)
	{
		// Set current arguments
		this.args = arguments;
		// Execute code sequence
		Iterator<IPrimitive> it = code.executeIterator();
		while (it.hasNext()) {
			it.next().evaluate(this);
		}		
		// Return the peek of the execution stack
		return (E) stack.pop();
	}
	
	// Arguments manipulation
}
