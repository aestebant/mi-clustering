package jclec.syntaxtree.mut;

import jclec.IConfigure;
import jclec.syntaxtree.*;
import jclec.util.random.IRandGen;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.Comparator;

/**
 * Selective mutator.
 * 
 * @author Sebastian Ventura
 * @author Amelia Zafra
 */

public class SelectiveMutator implements IMutateSyntaxTree, IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -2706522794304044852L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	/** Maximum mutated brach depth for the sons created */
	
	protected int maximumBrachDepth;
	
	/** Symbols in this mutation operation */

	protected NonTerminalNode [] selectedSymbols;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Used to compare symbols */
	
	protected transient Comparator<SyntaxTreeNode> symbolsComparator = new Comparator<SyntaxTreeNode> () 
	{
		public int compare(SyntaxTreeNode o1, SyntaxTreeNode o2) 
		{			
			if (o1 instanceof NonTerminalNode && o2 instanceof NonTerminalNode) {
				NonTerminalNode co1 = (NonTerminalNode) o1;
				NonTerminalNode co2 = (NonTerminalNode) o2;
				EqualsBuilder eb = new EqualsBuilder();
				eb.append(co1.getSymbol(), co2.getSymbol());
				
				if(eb.isEquals())
					return 0;
				else
					return -1;
			}
			return -1;
		}	
	};
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */
	
	public SelectiveMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setting and getting properties

	public int getMaximumBrachDepth() 
	{
		return maximumBrachDepth;
	}

	public void setMaximumBrachDepth(int maximumBrachDepth) 
	{
		this.maximumBrachDepth = maximumBrachDepth;
	}

	public NonTerminalNode[] getSelectedSymbols() 
	{
		return selectedSymbols;
	}

	public void setSelectedSymbols(NonTerminalNode[] selectedSymbols) 
	{
		this.selectedSymbols = selectedSymbols;
	}
	
	// Implementing IConfigure interface
	
	public void configure (Configuration settings)
	{
		// Maximum branch depth
		int maximumBranchDepth = settings.getInt("maximum-branch-depth");
		setMaximumBrachDepth(maximumBranchDepth);
		// Number of selected symbols in grammar
		int selSymDimension = settings.getInt("selected-symbols[@selected-dimension]");
		// Allocate space for selectedSymbols
		selectedSymbols = new NonTerminalNode[selSymDimension];
		// Get all terminalSymbol elements
		for ( int i =0; i < selSymDimension; i++)
		{
			//create Selected Symbol
			NonTerminalNode element = new NonTerminalNode ();
			//configuration header
			String header = "selected-symbols.dimension("+i+")";
			//Selected Symbol name
			String selName = settings.getString(header + ".name");
			//set Element name
			element.setSymbol(selName);
			selectedSymbols[i] = element;
		}
		setSelectedSymbols(selectedSymbols);
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	@Override
	public SyntaxTree mutateSyntaxTree(SyntaxTree parent, SyntaxTreeSchema schema, IRandGen randgen)	
	{	
		if(selectedSymbols == null) {
			selectedSymbols = schema.getNonTerminals();
		}
		// Son genotype
		SyntaxTree son = new SyntaxTree(); 
		// Set selected symbol 
		int numberOfSelectedSymbols = selectedSymbols.length;
		NonTerminalNode selectedSymbol = 
			selectedSymbols[randgen.choose(0, numberOfSelectedSymbols)];
		// Search selected symbol in first parent
		int p0_branchStart = searchSymbolIn(selectedSymbol, parent, randgen);
		// If symbol don't exist in tree, copy parents and return 
		if (p0_branchStart == -1) {
			for(int i=0; i<parent.size(); i++) {
				son.addNode(parent.getNode(i).copy()); // i?oeNO?
			}
			return son;
		}
		// Set branch end 
		int p0_branchEnd = parent.subTree(p0_branchStart);
		// Create son (first fragment)
		for (int i=0; i<p0_branchStart; i++) 
			son.addNode(parent.getNode(i).copy());
		// Determine the maximum size to fill
		int p0_branchDepth = parent.derivSize();
		int p0_swapBranch = 0;
		for(int i=p0_branchStart; i< p0_branchEnd; i++){
			if(parent.getNode(i).arity()!=0) {
				p0_swapBranch++;
			}
		}
		schema.fillSyntaxBranch(son, selectedSymbol.getSymbol(), p0_branchDepth - p0_swapBranch, randgen);
		// Create son (third fragment)
		int p0_length = parent.size();
		for (int i=p0_branchEnd; i<p0_length; i++) 
			son.addNode(parent.getNode(i).copy());
		// Return son
		return son;
	}	

	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////

	private final int searchSymbolIn(NonTerminalNode symbol, SyntaxTree tree, IRandGen randgen)
	{	
		// Tree length
		int treeLength = tree.size();
		// Generate a tree position at random
		int startPos = randgen.choose(0, treeLength);
		// 
		int actPos = startPos;
		for(int i=0; i<treeLength; i++) {
			// Update actPos
			actPos = (startPos+i)%treeLength;
			// Check symbols equality
			//if(symbol.equals(tree.getNode(actPos))) return actPos;
			if(symbolsComparator.compare(symbol, tree.getNode(actPos)) == 0) {
				return actPos;
			}
		}		
		return -1;
	}
}
