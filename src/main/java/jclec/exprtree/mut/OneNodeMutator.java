package jclec.exprtree.mut;

import jclec.exprtree.ExprTree;
import jclec.exprtree.ExprTreeSchema;
import jclec.exprtree.IMutateExprTree;
import jclec.exprtree.IPrimitive;
import jclec.util.random.IRandGen;

/**
 * Randomly select a node in the expression and replace it with another randomly
 * chosen block of the same return type and arity.
 * 
 * @author Sebastian Ventura
 */

public class OneNodeMutator implements IMutateExprTree
{
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	public OneNodeMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// java.lang.Object methods

	@Override
	public boolean equals(Object other)
	{
		return (other instanceof OneNodeMutator);
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	// PrefExprMutator methods
	
	@Override
	public ExprTree mutateExprTree(ExprTree ptree, ExprTreeSchema schema, IRandGen randgen) 
	{
		// Copy of expr
		ExprTree stree = ptree.copy();
		// Node selection
		int selectIndex = randgen.choose(0, ptree.size());
		// Mutated node
		IPrimitive oldBlock = stree.getBlock(selectIndex);
		Class<?> rtype = oldBlock.returnType();
		int arity = oldBlock.argumentTypes().length;		
		IPrimitive newBlock = 
			schema.getAnyBlock(rtype, randgen, arity);
		stree.setBlock(newBlock, selectIndex);
		// Return result
		return stree;
	}
}
