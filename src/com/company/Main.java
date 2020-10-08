package com.company;

public class Main {

    //Natural Fibonacci function
    public static long fibRecur(long num){
        if(num == 0 || num == 1){
            return num;
        } else {
            return fibRecur(num-1) + fibRecur(num-2);
        }
    }

    //Fibonacci function with cache
    public static long fibCache(long num){
        long[] lookupTable = new long[(int) num+1];
        return fibCacheHelper(num, lookupTable);
    }

    //Fibonacci cache function helper function
    public static long fibCacheHelper(long num, long[] table){
        if(num == 0 || num == 1){
            return num;
        } else if (table[(int)num] != 0){
            return table[(int)num];
        } else {
            table[(int)num] = fibCacheHelper(num - 1, table) + fibCacheHelper(num- 2, table);
            return table[(int)num];
        }
    }

    //Fibonacci function with loop
    public static long fibLoop(long num){
        long A = 0;
        long B = 1;
        long next;
        if(num < 2){
            return num;
        }
        for(int i = 2; i <= num; i++){
            next = A + B;
            A = B;
            B = next;
        }
        return B;
    }

    //Matrix multiplication function
    public static long[][] matrixMul(long[][] matrixOne, long[][] matrixTwo, int oneCols, int oneRows, int twoCols, int twoRows){
        long[][] resultMatrix = new long[oneRows][twoCols];
        long result;
        if(oneCols != twoRows){
            System.out.printf("Incorrect matrix arrays!! \n");
            return resultMatrix;
        }

        //Select row of matrix one
        for(int oneRow = 0; oneRow < oneRows; oneRow++){
            for(int twoCol = 0; twoCol < twoCols; twoCol++){
                result = 0;
                for(int sharedDim = 0; sharedDim < oneCols; sharedDim++){
                    result = result + (matrixOne[oneRow][sharedDim] * matrixTwo[sharedDim][twoCol]);
                }
                resultMatrix[oneRow][twoCol] = result;
            }
        }
        return resultMatrix;
    }

    public static void printMatrix(long[][] matrix, int rows, int cols){
        for(int x = 0; x < rows; x++){
            for(int y = 0; y < cols; y++){
                System.out.printf("%d ", matrix[x][y]);
            }
            System.out.println();
        }
        return;
    }

    public static long fibMatrix(long num){
        long[][] originalMatrix = {{1, 1},
                {1, 0}};
        long[][] resultMatrix = {{1, 1},
                {1, 0}};
        for(int x = 0; x < num-2; x++){
            resultMatrix = matrixMul(resultMatrix, originalMatrix, 2, 2, 2, 2);
        }
        return resultMatrix[0][0];
    }

    public static void main(String[] args) {
       long[][] matrixOne = {{1, 1},
               {1, 0}};
       matrixOne = matrixMul(matrixOne, matrixOne, 2, 2, 2, 2);
       printMatrix(matrixOne, 2, 2);

       long[][] A = {{6, 5, 3},
               {4, 7, -4}};
       long[][] B = {{6, 5, -2, 0},
               {-1, 1, 7, 2},
               {-4, 3, 4, 8}};
       long[][] result = matrixMul(A, B, 3, 2, 4, 3);
       printMatrix(result, 2, 4);
	// write your code here
        for(long x = 0; x < 10; x++) {
            System.out.printf("%d element in Fibonacci sequence is %d \t", x, fibRecur(x));
            System.out.printf("%d \t", fibCache(x));
            System.out.printf("%d \t", fibLoop(x));
            System.out.printf("%d \n", fibMatrix(x));
        }
    }
}
