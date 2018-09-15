package org.lrima.utils;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class EVOMatrixUtils {

    /**
     * Randomly mix two matrices into a single one
     * @param parent1 the first matrix
     * @param parent2 the matrix to mix with the first matrix
     * @return a child matrix containing a mix of parent1 and parent2
     */
    public static RealMatrix mix(RealMatrix parent1, RealMatrix parent2){
        double[][] parent1Array = parent1.getData();
        double[][] parent2Array = parent2.getData();
        double[][] childArray = new double[parent1.getRowDimension()][parent2.getColumnDimension()];

        for(int i = 0 ; i < childArray.length ; i++){
            for(int j = 0 ; j < childArray[0].length ; j++){
                int chance = Random.getRandomIntegerValue(0, 100);
                if(chance < 50){
                    childArray[i][j] = parent1Array[i][j];
                }
                else{
                    childArray[i][j] = parent2Array[i][j];
                }
            }
        }

        return MatrixUtils.createRealMatrix(childArray);
    }
}
