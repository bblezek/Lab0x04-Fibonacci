package com.company;

public class Main {

    //Natural Fibonacci function
    public static long fibRecur(long num) {
        //If we have reached "the bottom", return 0 or 1
        if (num == 0 || num == 1) {
            return num;
        } else {
            //Otherwise move "down" the tree, recrusively discovering and adding elements
            return fibRecur(num - 1) + fibRecur(num - 2);
        }
    }

    //Fibonacci function with cache
    public static long fibCache(long num) {
        //Creating a table to hold previously calculated numbers
        long[] lookupTable = new long[(int) num + 1];
        return fibCacheHelper(num, lookupTable);
    }

    //Fibonacci cache function helper function
    //Takes the index of the number we are trying to find and a table that will store elements in the sequence
    public static long fibCacheHelper(long num, long[] table) {
        //If we are "at the bottom"
        if (num == 0 || num == 1) {
            return num;
        //If the number at the index has already been calculated
        } else if (table[(int) num] != 0) {
            return table[(int) num];
        //If the number at the index has not been calculated, calculate it and add to the table
        } else {
            table[(int) num] = fibCacheHelper(num - 1, table) + fibCacheHelper(num - 2, table);
            return table[(int) num];
        }
    }

    //Fibonacci function with loop
    public static long fibLoop(long num) {
        long A = 0;
        long B = 1;
        long next;
        //If we are looking for one of the first two elements in the sequence
        if (num < 2) {
            return num;
        } else {
            //If it is not one of the first few elements,
            //we begin at the bottom and calculate up
            for (int i = 2; i <= num; i++) {
                next = A + B;
                A = B;
                B = next;
            }
        }
        return B;
    }

    //Matrix multiplication function
    public static long[][] matrixMul(long[][] matrixOne, long[][] matrixTwo, int oneCols, int oneRows, int twoCols, int twoRows) {
        //Creating a new matrix to store our results
        long[][] resultMatrix = new long[oneRows][twoCols];
        long result;
        //Ensuring that we can multiply the matrix
        if (oneCols != twoRows) {
            System.out.printf("Incorrect matrix arrays!! \n");
            return resultMatrix;
        }

        //Select row of matrix one
        for (int oneRow = 0; oneRow < oneRows; oneRow++) {
            //Select column of matrix two
            for (int twoCol = 0; twoCol < twoCols; twoCol++) {
                result = 0;
                //Iterate over row and column, adding to result
                for (int sharedDim = 0; sharedDim < oneCols; sharedDim++) {
                    result = result + (matrixOne[oneRow][sharedDim] * matrixTwo[sharedDim][twoCol]);
                }
                //Setting spot in result matrix equal to result
                resultMatrix[oneRow][twoCol] = result;
            }
        }
        return resultMatrix;
    }

    //Simple checker function to print matrices
    public static void printMatrix(long[][] matrix, int rows, int cols) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                System.out.printf("%d ", matrix[x][y]);
            }
            System.out.println();
        }
        return;
    }

    //Fibonacci sequence function based on matrices
    public static long fibMatrix(long num) {
//This matrix will store our "powers" as we calculate them
        long[][] powerMatrix = {{1, 1},
                {1, 0}};
        //This matrix will store our final result matrix
        long[][] resultMatrix = {{1, 1},
                {1, 0}};

        //We only need to iterate to matrix "power" number - 2
        //This is because the first value of the sequence is 0 and the second value is 1, so we can't technically
        //perform a matrix multiplication for those two "powers" and thus the sequence of matrix powers starts at
        //index 3 with "M to the second"
        long y = num - 2;
        //Return 0 if number equals 0
        if (num == 0) {
            return num;
        //Otherwise, iterate over "binary digits" of num to find result matrix
        } else {
            while (y > 0) {
                //If "binary" digit at lowest position is equal to 1
                if ((y % 2) == 1) {
                    //We multiply the "current" power of our matrix to the result
                    resultMatrix = matrixMul(resultMatrix, powerMatrix, 2, 2, 2, 2);
                }
                //Then we calculate the power for the next round
                powerMatrix = matrixMul(powerMatrix, powerMatrix, 2, 2, 2, 2);
                //Then divide y to "move down" a digit
                y = y / 2;
            }
        }
        //Return value at upper left hand corner of matrix
        return resultMatrix[0][0];
    }

    //Tests to ensure that functions are working
    public static boolean verificationTests() {
        long recurResult, cacheResult, loopResult, matrixResult;
        for (long num = 0; num < 40; num++) {
            recurResult = fibRecur(num);
            System.out.printf("%d element in Fibonacci sequence is %d \t", num, recurResult);
            cacheResult = fibCache(num);
            System.out.printf("%d \t", cacheResult);
            loopResult = fibLoop(num);
            System.out.printf("%d \t", loopResult);
            matrixResult = fibMatrix(num);
            System.out.printf("%d \n", matrixResult);
            if (recurResult == cacheResult && cacheResult == loopResult && loopResult == matrixResult) {
                System.out.printf("Result for %d is correct! \n", num);
            } else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (verificationTests()) {
            System.out.printf("Tests passed !\n");
        }

    }
}
