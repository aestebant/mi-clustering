package jclec.syntaxtree.mut;

import jclec.syntaxtree.IMutateSyntaxTree;
import jclec.syntaxtree.NonTerminalNode;
import jclec.syntaxtree.SyntaxTree;
import jclec.syntaxtree.SyntaxTreeSchema;
import jclec.util.random.IRandGen;

/**
 * ReduceNT mutator.
 * 
 * Cambia una rama del arbol por otra creada de forma aleatoria
 * pero con la minima profundidad.
 * 
 * @author Amelia Zafra
 */


public class ReduceNTMutator implements IMutateSyntaxTree 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -2706522794304044852L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	
	public ReduceNTMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// java.lang.Object methods

	public boolean equals(Object other)
	{
		if (other instanceof ReduceNTMutator) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	@Override
	public SyntaxTree mutateSyntaxTree(SyntaxTree parent, SyntaxTreeSchema schema, IRandGen randgen)	
	{	
		// Son genotype
		SyntaxTree son = new SyntaxTree(); 
		
		// Select a no terminal symbol 
		int p0_branchStart = selectSymbol(parent, schema, randgen);			
		
		// If a no terminal symbol doesn't exist in tree, copy parents and return 
		if (p0_branchStart == -1) {
			for(int i=0; i<parent.size(); i++)	son.addNode(parent.getNode(i).copy());			
			return son;
		}		
		
		// Assign the selected symbol
		NonTerminalNode selectedSymbol = (NonTerminalNode) parent.getNode(p0_branchStart);	
		
		// Set branch end 
		int p0_branchEnd = parent.subTree(p0_branchStart);
		
		// Create son (first fragment)
		for (int i=0; i<p0_branchStart; i++) 
			son.addNode(parent.getNode(i).copy());		
		
		//Complete with the minimum number of derivations
		schema.fillSyntaxBranch(son, selectedSymbol.getSymbol(), randgen);
		
		// Create son (second fragment)
		for(int j=p0_branchEnd; j<parent.size(); j++)	
			son.addNode(parent.getNode(j).copy());				
		
		// If it is not a valid individual, copy and return the parent
		if (son.derivSize() > schema.getMaxDerivSize()) {
			son.clear();
			for(int i=0; i<parent.size(); i++) 
				son.addNode(parent.getNode(i).copy());
		}
		
		// Return son
		return son;
	}	
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////

	/**
	 * Selecciona un simbolo no terminal cualquiera del arbol.
	 * 
	 * @param tree Arbol donde selecionar un simbolo cualquiera
	 * 
	 * @return Localizacion del simbolo seleccionado
	 */
	
	private final int selectSymbol(SyntaxTree tree, SyntaxTreeSchema schema, IRandGen randgen)
	{	
		// Tree length
		int treeLength = tree.size();
		// Generate a tree position at random
		int startPos = randgen.choose(0, treeLength);
		
		int actPos = startPos;
		for(int i=0; i<treeLength; i++) {
			// Update actPos
			actPos = (startPos+i)%treeLength;
			// Check symbol is nontermianl
			if( !schema.isTerminal(tree.getNode(actPos).getSymbol()) ) {
				return actPos;
			}
		}				
		return -1;
	}
}
