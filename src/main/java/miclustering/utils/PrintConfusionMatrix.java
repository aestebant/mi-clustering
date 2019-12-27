package miclustering.utils;

import miclustering.evaluators.ClassEvalResult;
import weka.core.Attribute;
import weka.core.Utils;

import java.util.Collections;

public class PrintConfusionMatrix {
    public static String singleLine(int[][] confMat) {
        int nClusters = confMat.length;
        int nClasses = confMat[0].length;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < nClusters; ++i) {
            for (int j = 0; j < nClasses; ++j)
                result.append(confMat[i][j]).append(" ");
            if (i < nClusters - 1)
                result.append("| ");
        }
        return result.toString();
    }

    public static String severalLines(ClassEvalResult cer, int[] bagsPerCluster, Attribute classAtt) {
        int maxNumClusters = bagsPerCluster.length;
        int actualNumClusters = maxNumClusters;
        for (int value : bagsPerCluster) {
            if (value < 1)
                actualNumClusters--;
        }
        int nClasses = classAtt.numValues();

        StringBuilder matrix = new StringBuilder();
        int maxVal = 0;
        for (int i = 0; i < maxNumClusters; ++i) {
            for (int j = 0; j < classAtt.numValues(); ++j) {
                if (cer.getConfMatrix()[i][j] > maxVal) {
                    maxVal = cer.getConfMatrix()[i][j];
                }
            }
        }
        int width = 1 + Math.max((int) (Math.log(maxVal) / Math.log(10D)), (int) (Math.log(actualNumClusters) / Math.log(10D)));

        for (int i = 0; i < nClasses; ++i) {
            matrix.append(" ").append(String.format("%1$" + width + "s", classAtt.value(i)));
        }
        matrix.append(" <- real classes\n");
        matrix.append(String.join("", Collections.nCopies(matrix.length(), "-"))).append("\n");


        for (int i = 0; i < maxNumClusters; ++i) {
            if (bagsPerCluster[i] > 0) {
                for (int j = 0; j < nClasses; ++j)
                    matrix.append(" ").append(Utils.doubleToString(cer.getConfMatrix()[i][j], width, 0));
                matrix.append(" | predicted cluster: ").append(i).append("\n");
            }
        }
        return matrix.toString();
    }
}
