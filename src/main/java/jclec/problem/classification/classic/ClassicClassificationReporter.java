package jclec.problem.classification.classic;

import jclec.problem.classification.ClassificationReporter;
import jclec.problem.classification.IClassifier;
import jclec.problem.util.dataset.IDataset;
import jclec.problem.util.dataset.IExample;
import jclec.problem.util.dataset.IMetadata;
import jclec.problem.util.dataset.attribute.IAttribute;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Listener for classic classification algorithms
 * 
 * @author Alberto Cano 
 * @author Amelia Zafra
 * @author Sebastian Ventura
 * @author Jose M. Luna 
 * @author Juan Luis Olmo
 */

public abstract class ClassicClassificationReporter extends ClassificationReporter
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -8548482239030974796L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor
	 */
	
	public ClassicClassificationReporter() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	/**
     * Obtain the percentage of correct predictions, the percentage of bad predictions,
     * the percentage of unclassified, the percentage of correct predictions per class
     * 
     * @param dataset Dataset
     * @param predicted values predicted for the dataset instances
     * @return vector that contains the percentage of correct predictions, the percentage 
     * of bad predictions the percentage of unclassified and the percentage of correct 
     * predictions per class
     */
    protected double [] checkingResult(IDataset dataset, double [] predicted)
	{
		IMetadata metadata = dataset.getMetadata();
    	int numClasses = metadata.numberOfClasses();
    	double [] result = new double[3+numClasses*2];
    	double success=0, fail =0, unclassified=0;

    	for(int i=0; i<numClasses*2; i++)
    		result[3+i] = 0.0;
    	
    	int i = 0;
    		
    	for (IExample example : dataset.getExamples()) 
    	{
    		double Class = ((ClassicInstance) example).getClassValue();
    			
    		result[3+numClasses+(int)Math.round(Class)] ++;
				
			if(predicted[i] == -1)
				unclassified++;
			else if(Class != predicted[i])
				fail++;
			else
			{
				success++;
				result[3+(int)Math.round(Class)]++;
			}
			i++;
    	}
    	
    	// Success rate
    	result[0] = success/dataset.getExamples().size();
    	// Fail rate
    	result[1] = fail/dataset.getExamples().size();
    	// Unclassified rate
    	result[2] = unclassified/dataset.getExamples().size();
    	
    	return result;
    }

	/**
	 * This method classifies a dataset and write the results in the FileWriter
	 * 
	 * @param dataset The dataset
	 * @param classifier The classifier
	 * @param file The file to write
	 */
    @Override
    protected void classify(IDataset dataset, IClassifier classifier, FileWriter file)
    {
    	IMetadata metadata = dataset.getMetadata();
		int numAttributes = metadata.numberOfAttributes();
		int numInstances = 0;
		
		double [] predicted = ((IClassicClassifier) classifier).classify(dataset);
		
    	try {
    		file.write("DATASET: " + dataset.getName());
    		
    		for (IExample example : dataset.getExamples()) 
        	{
				double Class = ((ClassicInstance) example).getClassValue();
				
				IAttribute attribute = null;
				file.write("\n");
				
				for(int j=0; j<numAttributes; j++)
				{
					attribute = metadata.getAttribute(j);
					file.write(attribute.show(example.getValue(j)) + ", ");
				}
				attribute = metadata.getAttribute(numAttributes);
				file.write(attribute.show(((ClassicInstance) example).getClassValue()));
							
				if(predicted[numInstances] == -1)
					file.write("\t Predicted: Unclassified -> FAIL" );
				else if(Class != predicted[numInstances])
					file.write("\t Predicted: "+attribute.show(predicted[numInstances]) + " -> FAIL");
				else
					file.write("\t Predicted: "+attribute.show(predicted[numInstances]) + " -> HIT");
				
				numInstances++;
        	}

			file.close();
			
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
    
    /**
	 * This method computes the area under the curve
	 * 
	 * @param confusionMatrix the confusion matrix
	 * @return the AUC value
	 */
    protected double AUC(int[][] confusionMatrix)
	{
		if(confusionMatrix.length == 2)
		{
			return AUC(confusionMatrix,0,1);
		}
		else
		{
			/** Multi-class AUC **/
			double auc = 0.0;
			
			for(int i = 0; i < confusionMatrix.length; i++)
				for(int j = 0; j < confusionMatrix.length; j++)
					if(i != j)
						auc += AUC(confusionMatrix,i,j);
			
			auc = auc / (double) (confusionMatrix.length * (confusionMatrix.length-1));
			
			return auc;
		}
	}
    
    /**
	 * This method computes the area under the curve for two classes
	 * 
	 * @param confusionMatrix the confusion matrix
	 * @param Class1 the class index of the first class
	 * @param Class2 the class index of the second class
	 * @return the AUC value
	 */
    protected double AUC(int[][] confusionMatrix, int Class1, int Class2)
	{
		double auc = 0.0;
		
		int tp = confusionMatrix[Class1][Class1];
		int fp = confusionMatrix[Class2][Class1];
		int tn = confusionMatrix[Class2][Class2];
		int fn = confusionMatrix[Class1][Class2];
		
		double tpRate = 1.0, fpRate = 0.0;
		
		if(tp + fn != 0)
			tpRate = tp / (double) (tp + fn);
		
		if(fp + tn != 0)
			fpRate = fp / (double) (fp + tn);
		
		auc = (1.0 + tpRate - fpRate) / 2.0;
		
		return auc;
	}
    
    /**
	 * This method computes the cohen's kappa rate
	 * 
	 * @param confusionMatrix the confusion matrix
	 * @return the kappa value
	 */
    protected double Kappa(int[][] confusionMatrix)
	{
		int correctedClassified = 0;
		int numberInstancesTotal = 0;
		int[] numberInstances = new int[confusionMatrix.length];
		int[] predictedInstances = new int[confusionMatrix.length];
		
		for(int i = 0; i < confusionMatrix.length; i++)
		{
			correctedClassified += confusionMatrix[i][i];
			
			for(int j = 0; j < confusionMatrix.length; j++)
			{
				numberInstances[i] += confusionMatrix[i][j];
				predictedInstances[j] += confusionMatrix[i][j];
			}
			
			numberInstancesTotal += numberInstances[i];
		}
		
		double mul = 0;
		
		for(int i = 0; i < confusionMatrix.length; i++)
			mul += numberInstances[i] * predictedInstances[i];
		
		if(numberInstancesTotal*numberInstancesTotal - mul  != 0)
			return ((numberInstancesTotal * correctedClassified) - mul) / (double) ((numberInstancesTotal*numberInstancesTotal) - mul);
		else
			return 1.0;
	}
    
    /**
	 * This method computes the geometric mean
	 * 
	 * @param confusionMatrix the confusion matrix
	 * @return the geometric mean
	 */
    protected double GeoMean(int[][] confusionMatrix)
	{
    	int[] numberInstances = new int[confusionMatrix.length];
		
		for(int i = 0; i < confusionMatrix.length; i++)
		{
			for(int j = 0; j < confusionMatrix.length; j++)
			{
				numberInstances[i] += confusionMatrix[i][j];
			}
		}
		
		double gm = 1.0;
		
		for(int i = 0; i < confusionMatrix.length; i++)
			if(numberInstances[i] != 0)
				gm *= confusionMatrix[i][i] / (double) numberInstances[i];
		
		return Math.pow(gm, 1.0 / (double) confusionMatrix.length);
	}
}