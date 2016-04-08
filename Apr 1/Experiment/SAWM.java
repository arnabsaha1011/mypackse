import java.text.DecimalFormat;

/**
 *
 * Created by asaha on 4/6/2016.
 */
public class SAWM {
    int columns = 6;
    int rows = 3;
    double[][] medianMatrix = new double[][]{
            {48.5, 55, 3.475, 7, 6, 8},
            {66.5, 71, 5.7, 8.5, 8, 9},
            {37.5, 213.5, 8.125, 8, 8, 7}
    };
    int[] benefitCost = {0, 0, 0, 1, 1, 1};
    int[] weights = {1, 1, 1, 1, 1, 1};

    public double[][] normalize(double[][] initMatrix, int[] benefitCost) {
        double[][] normalizedArray = new double[rows][columns];
        for (int i = 0; i < columns; i++) {
            double max = initMatrix[0][i], min = initMatrix[0][i];
            for (int j = 0; j < rows; j++) {
                max = Math.max(initMatrix[j][i], max);
                min = Math.min(initMatrix[j][i], min);
            }
            for (int j = 0; j < rows; j++) {
                if (benefitCost[i] == 1) { //benefit
                    normalizedArray[j][i] = ((initMatrix[j][i] - min) / (max - min));
                } else {
                    normalizedArray[j][i] = ((max - initMatrix[j][i]) / (max - min));
                }
            }

        }
        return normalizedArray;
    }

    public void displayArray(double[][] array) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                DecimalFormat numberFormat = new DecimalFormat("#.000");
                System.out.print(numberFormat.format(array[i][j]) + "\t");
            }
            System.out.println();
        }
    }

    public double[] sawm(double[][] normalizedArray, int[] weights) {
        double[][] calcMatrix = new double[rows][columns];
        double[] sawmArray = new double[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                calcMatrix[i][j] = normalizedArray[i][j] * weights[j];
                sawmArray[i] = sawmArray[i] + calcMatrix[i][j];
            }
        }
        return sawmArray;
    }

    public void calcSAWM() {
        System.out.println("Display Init");
        displayArray(medianMatrix);
        System.out.println("\nDisplay Normalized Matrix");
        double[][] normalize = normalize(medianMatrix, benefitCost);
        displayArray(normalize);
        double[] sawmArray = sawm(normalize, weights);
        System.out.println("\nSAWM results");
        displayArr(sawmArray);
    }

    private void displayArr(double[] sawmArray) {
        for (int i = 0; i < sawmArray.length; i++) {
            DecimalFormat numberFormat = new DecimalFormat("#.000");
            System.out.print(numberFormat.format(sawmArray[i]) + "\t");

        }
    }

    public static void main(String[] args) {
        SAWM sawm = new SAWM();
        sawm.calcSAWM();
    }
}
