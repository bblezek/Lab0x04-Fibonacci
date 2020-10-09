package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Main {

    //Natural Fibonacci function
    public static long fibRecur(long x) {
        //If we have reached "the bottom", return 0 or 1
        if (x == 0 || x == 1) {
            return x;
        } else {
            //Otherwise move "down" the tree, recursively discovering and adding elements
            return fibRecur(x - 1) + fibRecur(x - 2);
        }
    }

    //Fibonacci function with cache
    public static long fibCache(long x) {
        //Creating a table to hold previously calculated numbers
        long[] lookupTable = new long[(int) x + 1];
        return fibCacheHelper(x, lookupTable);
    }

    //Fibonacci cache function helper function
    //Takes the index of the number we are trying to find and a table that will store elements in the sequence
    public static long fibCacheHelper(long x, long[] table) {
        //If we are "at the bottom"
        if (x == 0 || x == 1) {
            return x;
            //If the number at the index has already been calculated
        } else if (table[(int) x] != 0) {
            return table[(int) x];
            //If the number at the index has not been calculated, calculate it and add to the table
        } else {
            table[(int) x] = fibCacheHelper(x - 1, table) + fibCacheHelper(x - 2, table);
            return table[(int) x];
        }
    }

    //Fibonacci function with loop
    public static long fibLoop(long x) {
        long A = 0;
        long B = 1;
        long next;
        //If we are looking for one of the first two elements in the sequence
        if (x < 2) {
            return x;
        } else {
            //If it is not one of the first few elements,
            //we begin at the bottom and calculate up
            for (int i = 2; i <= x; i++) {
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
    public static long fibMatrix(long x) {
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
        long y = x - 2;
        //Return 0 if number equals 0
        if (x == 0) {
            return x;
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
        for (long x = 0; x < 10; x++) {
            recurResult = fibRecur(x);
            System.out.printf("%d element in Fibonacci sequence is %d \t", x, recurResult);
            cacheResult = fibCache(x);
            System.out.printf("%d \t", cacheResult);
            loopResult = fibLoop(x);
            System.out.printf("%d \t", loopResult);
            matrixResult = fibMatrix(x);
            System.out.printf("%d \n", matrixResult);
            if (recurResult == cacheResult && cacheResult == loopResult && loopResult == matrixResult) {
                System.out.printf("Result for %d is correct! \n", x);
            } else {
                return false;
            }
        }
        return true;
    }

    //Function to retrieve CPU time
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    public static void runTimeTests() {
        //Max time to run is 4 minutes
        long maxTime = 240000000000L;
        long beforeTime, afterTime;
        long recurPrevTime = 0;
        long loopPrevTime = 0;
        long cachePrevTime = 0;
        long matrixPrevTime = 0;

        //Printing header row for recursive and loop function tables
        System.out.printf("%7s %85s \n", "Fib Recur", "Fib Loop");
        System.out.printf("%7s %15s %31s %15s %15s %15s", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%7s %15s %31s %15s %15s %15s \n", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%71s %15s", "Ratio", "Doubling");
        System.out.printf("%71s %15s \n", "Ratio", "Doubling");
        System.out.printf("%87s", "Ratio");
        System.out.printf("%87s\n", "Ratio");
        long result = 0;
        int N;
        long x;
        for (N = 1; N < 32; N++) {
            for (x = (long) Math.pow(2, (double) N - 1); x < Math.pow(2, (double) N) - 1 && x < 93; x++) {
                if (recurPrevTime < maxTime) {
                    beforeTime = getCpuTime();
                    result = fibRecur(x);
                    afterTime = getCpuTime();
                    System.out.printf("%7d %15d %31d %15d %15.3f %15.3f ", N, x, result, afterTime - beforeTime,
                            (float) recurPrevTime / (afterTime - beforeTime), Math.pow(2, (x / 2)) / Math.pow(2, ((x - 1) / 2)));
                    recurPrevTime = afterTime - beforeTime;
                } else {
                    System.out.printf("%7s %15s %31s %15s %15s %15s ", "na", "na", "na", "na", "na", "na", "na");
                }
                beforeTime = getCpuTime();
                result = fibLoop(x);
                afterTime = getCpuTime();
                System.out.printf("%7d %15d %31d %15d %15.3f %15.3f ", N, x, result, afterTime - beforeTime,
                        (float) loopPrevTime / (afterTime - beforeTime), (float) x / (x - 1));
                loopPrevTime = afterTime - beforeTime;
                System.out.println();
            }
        }

        System.out.printf("%7s %87s \n", "Fib Cache", "Fib Matrix");
        System.out.printf("%7s %15s %15s %15s %15s %15s", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%7s %15s %15s %15s %15s %15s \n", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%71s %15s", "Ratio", "Doubling");
        System.out.printf("%71s %15s \n", "Ratio", "Doubling");
        System.out.printf("%87s", "Ratio");
        System.out.printf("%87s\n", "Ratio");
        for (N = 1; N < 32; N++) {
            for (x = (long) Math.pow(2, (double) N - 1); x < Math.pow(2, (double) N) - 1 && x < 93; x++) {
                beforeTime = getCpuTime();
                result = fibCache(x);
                afterTime = getCpuTime();
                System.out.printf("%7d %15d %31d %15d %15.3f %15.3f ", N, x, result, afterTime - beforeTime,
                        (float) cachePrevTime / (afterTime - beforeTime), Math.pow(2, (x / 2)) / Math.pow(2, ((x - 1) / 2)));
                cachePrevTime = afterTime - beforeTime;
                beforeTime = getCpuTime();
                result = fibMatrix(x);
                afterTime = getCpuTime();
                System.out.printf("%7d %15d %31d %15d %15.3f %15.3f ", N, x, result, afterTime - beforeTime,
                        (float) matrixPrevTime / (afterTime - beforeTime), (float) x / (x - 1));
                matrixPrevTime = afterTime - beforeTime;
                System.out.println();
            }
        }

    }

    public static void main(String[] args) {
        if (verificationTests()) {
            System.out.printf("Tests passed !\n");
        }
        runTimeTests();
    }
}
