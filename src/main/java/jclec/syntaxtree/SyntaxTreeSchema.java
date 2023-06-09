package jclec.syntaxtree;

import jclec.JCLEC;
import jclec.util.random.IRandGen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Species for SyntaxTreeIndividual and its subclasses.
 *  
 * @author Sebastian Ventura
 */

public class SyntaxTreeSchema implements JCLEC  
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -8873594607721603028L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** All terminals */
	
	protected TerminalNode [] terminals;

	/** All non terminals */
	
	protected NonTerminalNode [] nonTerminals;

	/** Root symbol name */
	
	protected String rootSymbol;
	
	/** Maximum of derivations per tree */
	
	protected int maxDerivSize;
	
	/** Interval variable */
	
	private BigInteger MINUSONE = new BigInteger("-1");
	
	/////////////////////////////////////////////////////////////
	// --------------------------------------- Internal variables
	///////////////////////////////////////////////////////////// 
		
	/** Minimum of derivations per tree */
	
	protected int minDerivSize;

	/** Terminal symbols map */
	
	protected transient HashMap<String, TerminalNode> terminalsMap;

	/** Non terminal symbols map */
	
	protected transient HashMap<String, NonTerminalNode[]> nonTerminalsMap;

	/** Cardinality map */
	
	protected transient HashMap<String [], BigInteger[]> cardinalityMap;	

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor.
	 */
	
	public SyntaxTreeSchema() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting properties
	
	/**
	 * Sets the  maximum number of  derivations for  all syntax trees 
	 * represented  by this schema
	 * 
	 * @param maxDerivSize
	 */

	public void setMaxDerivSize(int maxDerivSize) 
	{
		// Set maxDerivSize
		this.maxDerivSize = maxDerivSize;
		// Set cardinality map (if necessary) 
		if(terminalsMap!= null && nonTerminalsMap != null) {
			setCardinalityMap();
		}
	}

	/**
	 * Set all the terminal symbols for this grammar
	 * 
	 * @param terminals Terminals set
	 */
	
	public void setTerminals(TerminalNode[] terminals) 
	{
		// Set terminal symbols
		this.terminals = terminals;
		// Set terminal symbols map
		setTerminalsMap();
		// Set cardinality map (if necessary) 
		if(maxDerivSize != 0 && nonTerminalsMap != null) {
			setCardinalityMap();
		}
	}

	/**
	 * Set all the non terminal symbols for this grammar
	 * 
	 * @param nonTerminals Non terminal symbols
	 */
	
	public void setNonTerminals(NonTerminalNode[] nonTerminals) 
	{
		// Set non-terminal symbols
		this.nonTerminals = nonTerminals;
		// Set non-terminal symbols map
		setNonTerminalsMap();
		// Set cardinality map (if necessary) // ???
		if(maxDerivSize != 0 && terminalsMap != null) {
			setCardinalityMap();
		}
	}

	/**
	 * Set the start symbol for this grammar
	 * 
	 * @param rootSymbol Start symbol
	 */
	
	public void setRootSymbol(String rootSymbol) 
	{
		this.rootSymbol = rootSymbol;
	}

	// Syntax trees information
		
	/**
	 * Get the maximum derivation size
	 * 
	 * @return maximum derivation size
	 */
		
	public int getMaxDerivSize() 
	{
		return maxDerivSize;
	}
	
	/**
	 * Get the root symbol
	 * 
	 * @return root symbol
	 */

	public String getRootSymbol() {
		return rootSymbol;
	}

	/**
	 * Get the minimum derivation size
	 * 
	 * @return minimum derivation size
	 */
		
	public int getMinDerivSize() 
	{
		if (minDerivSize == 0) {
			setMinDerivSize();
		}
		return minDerivSize;
	}	

	/**
	 * Get the array of terminal nodes
	 * 
	 * @return terminal nodes
	 */
		
	public TerminalNode[] getTerminals() 
	{
		return terminals;
	}

	/**
	 * Get the array of nonterminal nodes
	 * 
	 * @return nonterminal nodes
	 */
		
	public NonTerminalNode[] getNonTerminals() 
	{
		return nonTerminals;
	}

	/**
	 * Checks if a symbol is terminal
	 * 
	 * @param symbol the symbol
	 * 
	 * @return true or false
	 */
		
	public boolean isTerminal(String symbol)
	{
		return terminalsMap.containsKey(symbol);
	}

	/**
	 * Creates a SyntaxTree
	 * 
	 * @param nOfDer number of derivations
	 * @param randgen random generator
	 * 
	 * @return new SyntaxTree
	 */
		
	public SyntaxTree createSyntaxTree(int nOfDer, IRandGen randgen)
	{
		// Create resulting tree
		SyntaxTree result = new SyntaxTree();
		// Fill result branch
		fillSyntaxBranch(result, rootSymbol, nOfDer, randgen);
		// Return resulting tree 
		return result;
	}

	/**
	 * Fills a syntaxtree using the symbol
	 * 
	 * @param owner given syntaxtree
	 * @param symbol to add
	 * @param randgen Random generator
	 */

	public void fillSyntaxBranch(SyntaxTree owner, String symbol, IRandGen randgen)
	{
		for (int i=0; i<maxDerivSize; i++)
			if (!cardinality(symbol, i).equals(BigInteger.ZERO))
				fillSyntaxBranch(owner, symbol, i, randgen);
	}
		
	/**
	 * Fills a syntaxtree using the symbol and the allowed number of derivations
	 * 
	 * @param owner given syntaxtree
	 * @param symbol to add
	 * @param nOfDer number of derivations
	 * @param randgen Random generator
	 */
		
	public void fillSyntaxBranch(SyntaxTree owner, String symbol, int nOfDer, IRandGen randgen)
	{
		if (isTerminal(symbol)) {
			owner.addNode(getTerminal(symbol));
		}
		else {
			// Select a production rule
			NonTerminalNode selectedProduction = selectProduction(symbol, nOfDer, randgen); 
			if (selectedProduction != null) {
				// Add this node
				owner.addNode(selectedProduction);
				
				// Select a partition for this production rule
				int [] selectedPartition = selectPartition(selectedProduction.production, nOfDer-1, randgen);
				// Apply partition, expanding production symbols
				int selProdSize = selectedPartition.length;
				
				for (int i=0; i<selProdSize; i++)
					fillSyntaxBranch(owner, selectedProduction.production[i], selectedPartition[i], randgen);
			}
			else {
				fillSyntaxBranch(owner, symbol, nOfDer-1, randgen);
			}
		}
	}
		
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
		
	/**
	 * Calculate and set the minimum number of derivations.
	 */
		
	protected final void setMinDerivSize() 
	{
		for (int i=0; i<maxDerivSize; i++) {
			if (!cardinality(rootSymbol, i).equals(BigInteger.ZERO)) {
				minDerivSize = i;
				return;
			}
		}
	}

	/**
	 * Build and set the terminals map.
	 */
		
	protected final void setTerminalsMap() 
	{
		terminalsMap = new HashMap<String, TerminalNode> ();
		for (TerminalNode termSymbol : terminals) {
			terminalsMap.put(termSymbol.getSymbol(), termSymbol);
		}
	}

	/**
	 * Build and set the non terminals map.
	 */

	protected final void setNonTerminalsMap() 
	{
		// Used to classify symbols
		HashMap<String, List<NonTerminalNode>> auxMap = 
			new HashMap<String, List<NonTerminalNode>> ();
		// Classify non-term symbols
		for (NonTerminalNode nonTermSymbol : nonTerminals) {
			String nonTermSymbolName = nonTermSymbol.getSymbol();
			if (auxMap.containsKey(nonTermSymbolName)) {
				auxMap.get(nonTermSymbolName).add(nonTermSymbol);
			}
			else {
				ArrayList<NonTerminalNode> list = 
					new ArrayList<NonTerminalNode>();
				list.add(nonTermSymbol);
				auxMap.put(nonTermSymbolName, list);
			}
		}			
		// Create non-term symbols map
		nonTerminalsMap = new HashMap<String, NonTerminalNode[]> ();
		for (String nonTermName : auxMap.keySet()) {
			// Get symbols list
			List<NonTerminalNode> list = auxMap.get(nonTermName);
			// Convert list to array
			NonTerminalNode [] array = 
				list.toArray(new NonTerminalNode[list.size()]);
			// Put array in non terminals map
			nonTerminalsMap.put(nonTermName, array);
		}
	}

	/**
	 * Build and set the cardinality map. This map contains cardinality 
	 * of all production rules (from cero to max number of derivations)
	 */
	
	protected final void setCardinalityMap() 
	{
		// Cardinality map
		cardinalityMap = new HashMap<String [] , BigInteger []> ();
		for (NonTerminalNode nonTermSymbol : nonTerminals) {
			// Allocate space for cardinalities array 
			BigInteger [] list1 = new BigInteger [1+maxDerivSize];
			for(int j=0; j<list1.length; j++) list1[j] = new BigInteger("-1");
			// Put array in cardinality map
			cardinalityMap.put(nonTermSymbol.production, list1);
		}			
	}
		
	/**
	 * Get a terminal giving his name.
	 *  
	 * @param symbol Symbol name
	 * 
	 * @return Desired symbol
	 */
	
	public final TerminalNode getTerminal(String symbol)
	{
		return terminalsMap.get(symbol);
	}

	/**
	 * Select a production rule for a symbol of the grammar, given the 
	 * number of derivations available.
	 * 
	 * @param symbol  Symbol to expand
	 * @param nOfDer  Number of derivations available
	 * @param randgen Random generator used
	 * 
	 * @return A production rule for  the given symbol or null if this
	 * 		   symbol cannot be expanded  using exactly such number of
	 * 		   derivations.
	 */
		
	protected NonTerminalNode selectProduction(String symbol, int nOfDer, IRandGen randgen)
	{
		// Get all productions of this symbol
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		// Number of productions
		int nOfProdRules = prodRules.length;
		// Create productions roulette
		BigInteger [] roulette = new BigInteger[nOfProdRules];
		
		for(int i = 0; i < nOfProdRules; i++)
			roulette[i] = new BigInteger("0");
		
		// Fill roulette
		for (int i=0; i<nOfProdRules; i++) {
			BigInteger [] cardinalities = cardinalityMap.get(prodRules[i].production);
			// If this cardinality is not calculated, it will be calculated 
			if(cardinalities[nOfDer-1].equals(MINUSONE)) {
				cardinalities[nOfDer-1] = cardinality(prodRules[i].production, nOfDer-1);
				cardinalityMap.put(prodRules[i].production, cardinalities);
			}
			roulette[i] = cardinalities[nOfDer-1];
			if (i != 0) {
				roulette[i] = roulette[i].add(roulette[i-1]);
			}
		}
		
		// Choose a production at random
		BigDecimal randVal = new BigDecimal(roulette[nOfProdRules - 1].toString());
		randVal = randVal.multiply(new BigDecimal(randgen.raw()));
		
		for (int i=0; i<nOfProdRules; i++) {
			if (randVal.compareTo(new BigDecimal(roulette[i].toString())) < 0) return prodRules[i];
		}
		
		return null;			
	}
		
	/**
	 * Select a partition to expand a symbol using a production rule.
	 *  
	 * @param prodRule Production rule to expand 
	 * @param nOfDer   Number of derivations available
	 * @param randgen  Random generator used
	 * 
	 * @return A partition...
	 */
		
	protected final int [] selectPartition(String [] prodRule, int nOfDer, IRandGen randgen)
	{
		// Obtain all partitions for this production rule 
		List<int []> partitions = partitions(nOfDer, prodRule.length);
		// Number of partitions
		int nOfPart = partitions.size();
		// Create partitions roulette
		BigInteger [] roulette = new BigInteger[nOfPart];
		
		for(int i = 0; i < nOfPart; i++)
			roulette[i] = new BigInteger("0");

		// Set roulette values
		for (int i=0; i<nOfPart; i++) {
			roulette[i] = cardinality(prodRule, partitions.get(i));
			if (i != 0) {
				roulette[i] = roulette[i].add(roulette[i-1]);
			}
		}
		
		// Choose a production at random
		BigDecimal randVal = new BigDecimal(roulette[nOfPart - 1].toString());
		randVal = randVal.multiply(new BigDecimal(randgen.raw()));
		
		for (int i=0; i<nOfPart; i++) {
			if (randVal.compareTo(new BigDecimal(roulette[i].toString())) < 0) return partitions.get(i);
		}
		
		// This point shouldn't be reached 
		return null;
	}

	// Cardinality methods
		
	/**
	 * Cardinality of a grammar symbol for the given number of derivs
	 * 
	 * @param symbol Grammar symbol 
	 * @param nOfDer Number of derivations
	 */
	
	protected final BigInteger cardinality(String symbol, int nOfDer)
	{
		if (isTerminal(symbol)) {
			return (nOfDer == 0) ? new BigInteger("1") : new BigInteger("0");
		}
		else {
			BigInteger result = new BigInteger("0");
			NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
			for (NonTerminalNode prodRule : prodRules) {					
				BigInteger [] cardinalities = cardinalityMap.get(prodRule.production);
				if(nOfDer <= 0){
					result = result.add(cardinality(prodRule.production, nOfDer-1));
				}
				else{
					// If this cardinality is not calculated, it will be calculated
					if(cardinalities[nOfDer-1].equals(MINUSONE)) {
						cardinalities[nOfDer-1] = cardinality(prodRule.production, nOfDer-1);
						cardinalityMap.put(prodRule.production, cardinalities);
					}
					result = result.add(cardinalities[nOfDer-1]);
				}
			}
			return result;
		}
	}
		
	/**
	 * Cardinality of a production rule for the given number of derivs
	 *
	 * @param nOfDer Number of derivations
	 */
		
	protected BigInteger cardinality(String [] pRule, int nOfDer)
	{
		// Resulting cardinality
		BigInteger result = new BigInteger("0");
		// Obtain all partitions  
		List<int []> partitions = partitions(nOfDer, pRule.length);
		// For all partitions of nOfDer...
		for (int [] partition : partitions) {
			result = result.add(cardinality(pRule, partition));
		}
		// Return result
		return result;
	}

	/**
	 * Cardinality of a production rule for the given partition.
	 * 
	 * @param prodRule  Production rule
	 * @param partition Partition
	 * 
	 * @return Cardinality of the production rule for the partition
	 */
		
	protected BigInteger cardinality(String [] prodRule, int [] partition)
	{
		int prodRuleSize = prodRule.length;
		BigInteger result = new BigInteger("1");
		for (int i=0; i<prodRuleSize; i++) {
			BigInteger factor = cardinality(prodRule[i], partition[i]);
			if (factor.equals(BigInteger.ZERO)) {
				return new BigInteger("0");
			}
			else {
				result = result.multiply(factor);
			}
		}
		return result;
	}
		
	// Partition methods
		
	/**
	 * Partitions generation.
	 * 
	 * @param total     Total count
	 * @param dimension Partition dimension
	 */
		
	protected final List<int []> partitions(int total, int dimension) 
	{
		ArrayList<int []> result = new ArrayList<int[]> ();
		if (dimension == 1) {
			result.add(new int [] {total});
		}
		else {
			for (int i=0; i<=total; i++) 
			{
				List<int []> pi = partitions(total-i, dimension-1); 
				result.addAll(insertBefore(i, pi));
			}
		}
		return result;
	}
	
	protected final List<int []> insertBefore(int previous, List<int []> strings)
	{
		ArrayList<int []> result = new ArrayList<int []> ();
		for (int [] string : strings) {
			int [] tmp = new int [1+string.length];
			System.arraycopy(string, 0, tmp, 1, string.length);
			tmp[0] = previous;
			result.add(tmp);
		}
		return result;
	}
}