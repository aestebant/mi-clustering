package jclec.exprtree.fun;

import jclec.exprtree.IContext;
import jclec.exprtree.IPrimitive;

/**
 * IPrimitive abstract implementation
 * 
 * @author Sebastian Ventura
 */

public abstract class AbstractPrimitive implements IPrimitive 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -2462382378306675405L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Argument types */
	
	protected final Class<?> [] argTypes;
	
	/** Return type */
	
	protected final Class<?> returnType;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Default constructor.
	 * 
	 * Subclasses of AbstractPrimitive set argument and return types
	 */
	
	protected AbstractPrimitive(Class<?> [] atypes, Class<?> rtype) 
	{
		this.argTypes   = atypes;
		this.returnType = rtype; 
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// IPrimitive interface
	
	@Override
	public final Class<?>[] argumentTypes() 
	{
		return argTypes;
	}

	@Override
	public final Class<?> returnType() 
	{
		return returnType;
	}

	/**
	 * Default implementation return this
	 * 
	 * {@inheritDoc}
	 */
	
	@Override
	public IPrimitive copy() 
	{
		return this;
	}

	/**
	 * Default implementation return this
	 * 
	 * {@inheritDoc}
	 */
	
	@Override
	public IPrimitive instance() 
	{
		return this;
	}
	
	/**
	 * This context has to be an ExprTreeFunction object.
	 * 
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException If 
	 */
	
	public final void evaluate(IContext context)
	{
		if (context instanceof ExprTreeFunction) {
			evaluate((ExprTreeFunction) context);
		}
		else {
			throw new IllegalArgumentException("ExprTreeFunction expected as context");
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	// Execution method
	
	/**
	 * Evaluate this primitive.
	 * 
	 * @param context Execution context
	 */
	
	protected abstract void evaluate(ExprTreeFunction context);
	
	// Access to execution stack
	
	/**
	 * Clear the execution stack
	 */
	
	protected void clear(ExprTreeFunction context)
	{
		context.stack.clear();
	}

	/**
	 * Push an object in the execution stack
	 * 
	 * @param context Execution context
	 * @param object  Object to push in the stack
	 */
	
	protected void push(ExprTreeFunction context, Object object)
	{
		context.stack.push(object);
	}
	
	/**
	 * Pops an object in the execution stack
	 * 
	 * @return Peek in the execution stack
	 */
	
	@SuppressWarnings("unchecked")
	protected <E> E pop(ExprTreeFunction context)
	{
		return (E) context.stack.pop();
	}
	
	/**
	 * Return the peek object in the execution stack
	 * 
	 * @return Peek in the execution stack
	 */
		
	@SuppressWarnings("unchecked")
	protected <E> E peek(ExprTreeFunction context)
	{
		return (E) context.stack.peek();
	}
	
	// Access to arguments pool
	
	/**
	 * Access to the argindex-th argument in the argument pool
	 * 
	 * @param context  Execution context
	 * @param argindex Argument index
	 */
	
	@SuppressWarnings("unchecked")
	protected <E> E getArg(ExprTreeFunction context, int argindex)
	{
		return (E) context.args[argindex];
	}
	
	/**
	 * Access to all context arguments
	 * 
	 * @param context Execution context
	 * 
	 * @return Arguments pool
	 */
	
	protected Object [] getArgs(ExprTreeFunction context)
	{
		return context.args;
	}
	
	// Mixed methods
	
	/**
	 * Push the argindex-th argument in execution stack
	 * 
	 * @param context  Execution context
	 * @param argindex Argument index
	 */
	
	protected void pushArg(ExprTreeFunction context, int argindex)
	{
		context.stack.push(context.args[argindex]);
	}
}
