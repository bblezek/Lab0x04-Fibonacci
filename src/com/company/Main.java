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

    //Calculates log base 2 of N
    public static float logBaseTwo(int N) {
        float result = ((float) Math.log(N) / (float) Math.log(2));
        return result;
    }

    public static void runTimeTests() {
        //Max time to run is 2 minutes
        long maxTime = 120000000000L;
        long beforeTime = 0;
        long afterTime = 0;
        long[] recurTime = new long[92];
        long[] loopTime = new long[92];
        long[] cacheTime = new long[92];
        long[] matrixTime = new long[92];

        //Printing header row for recursive and loop function tables
        System.out.printf("%7s %85s \n", "Fib Recur", "Fib Loop");
        System.out.printf("%7s %15s %31s %15s %15s %15s", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%7s %15s %31s %15s %15s %15s \n", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%87s %15s", "Ratio", "Doubling");
        System.out.printf("%87s %15s \n", "Ratio", "Doubling");
        System.out.printf("%103s", "Ratio");
        System.out.printf("%103s\n", "Ratio");
        long result = 0;
        int N, x, y;
        int maxRecurLoops = 101;

        //N is how many "bits" we have
        for (N = 0; N < 10; N++) {
            //X is what "index" of the fibonacci sequence we are finding
            for (x = (int) Math.pow(2, (double) N - 1); x < Math.pow(2, (double) N) && x < 93; x++) {
                //We only run the recur function until it surpasses the 2-minute mark
                if (x == 0 || recurTime[x - 1] < maxTime) {
                    //Setting the number of loops by what number of the sequence we are at
                    if (x % 10 == 0 && x > 0) {
                        maxRecurLoops = maxRecurLoops - 20;
                    }

                    //Repeating the recursive Fibonacci function and taking average
                    for (y = 0; y < maxRecurLoops; y++) {
                        beforeTime = getCpuTime();
                        result = fibRecur(x);
                        afterTime = getCpuTime();
                        //Updating appropriate place in "time" array
                        recurTime[x] = recurTime[x] + afterTime - beforeTime;
                    }
                    //Calculating average time
                    recurTime[x] = recurTime[x] / maxRecurLoops;
                    System.out.printf("%7d %15d %31d %15d ", N, x, result, recurTime[x]);

                    //Only print doubling ratios for even numbers
                    if (x % 2 == 0 && x != 0) {
                        //Time complexity is exponential - work is at least 2^(x/2)
                        System.out.printf("%15.3f %15.3f ", (float) recurTime[x] / recurTime[x / 2],
                                (float) result / fibMatrix(x / 2));
                    } else {
                        System.out.printf("%15s %15s ", "na", "na");
                    }
                } else {
                    System.out.printf("%7s %15s %31s %15s %15s %15s ", "na", "na", "na", "na", "na", "na", "na");
                }

                //Performing loop function 20 times and taking average
                for (y = 0; y < 100; y++) {
                    beforeTime = getCpuTime();
                    result = fibLoop(x);
                    afterTime = getCpuTime();
                    //Updating appropriate place in time array
                    loopTime[x] = loopTime[x] + afterTime - beforeTime;
                }

                //Calculating average time
                loopTime[x] = loopTime[x] / 20;
                System.out.printf("%7d %15d %31d %15d ", N, x, result, loopTime[x]);

                //Only printing ratios if x is even
                if (x % 2 == 0 && x != 0) {
                    //Time complexity is linear, so we expect the doubling ratio to be x/(x/2) or 2
                    System.out.printf("%15.3f %15d ", (float) loopTime[x] / loopTime[(x / 2)], 2);
                } else {
                    System.out.printf("%15s %15s ", "na", "na");
                }
                System.out.println();
            }
        }

        //Printing header row for fibonacci cache and fibonacci matrix functions
        System.out.printf("%7s %87s \n", "Fib Cache", "Fib Matrix");
        System.out.printf("%7s %15s %15s %15s %15s %15s", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%7s %15s %15s %15s %15s %15s \n", "N", "X", "Fib(x)", "Time", "Doubling", "Expected");
        System.out.printf("%71s %15s", "Ratio", "Doubling");
        System.out.printf("%71s %15s \n", "Ratio", "Doubling");
        System.out.printf("%87s", "Ratio");
        System.out.printf("%87s\n", "Ratio");

        //N is the number of "bits"
        for (N = 1; N < 32; N++) {
            //x is the element of the Fibonacci sequence that we are finding
            for (x = (int) Math.pow(2, (double) N - 1); x < Math.pow(2, (double) N) - 1 && x < 93; x++) {
                //Running cache function 20 times
                for (y = 0; y < 20; y++) {
                    beforeTime = getCpuTime();
                    result = fibCache(x);
                    afterTime = getCpuTime();
                    cacheTime[x] = cacheTime[x] + afterTime - beforeTime;
                }

                //Calculating average time
                cacheTime[x] = cacheTime[x] / 20;
                System.out.printf("%7d %15d %31d %15d ", N, x, result, cacheTime[x],
                        (float) cacheTime[x] / (afterTime - beforeTime), Math.pow(2, (x / 2)) / Math.pow(2, ((x - 1) / 2)));
                if (x % 2 == 0 && x != 0) {
                    //Time complexity is linear, so our expected doubling ratio will be x/(x/2) or 2
                    System.out.printf("%15.3f %15.3f ", (float) cacheTime[x] / cacheTime[x / 2], x / (x / 2));
                } else {
                    System.out.printf("%15s %15s ", "na", "na");
                }

                //Running matrix function 20 times
                for (y = 0; y < 20; y++) {
                    beforeTime = getCpuTime();
                    result = fibMatrix(x);
                    afterTime = getCpuTime();
                    matrixTime[x] = matrixTime[x] + afterTime - beforeTime;
                }

                //Calculating average time
                matrixTime[x] = matrixTime[x] / 20;
                System.out.printf("%7d %15d %31d %15d ", N, x, result, matrixTime[x]);
                //Don't print doubling ratios unless x is divisible by 2
                if (x % 2 == 0 && x != 0) {
                    //Time complexity is log base two of x
                    System.out.printf("%15.3f %15.3f ", (float) matrixTime[x] / matrixTime[x / 2],
                            logBaseTwo(x) / (logBaseTwo(x / 2)));
                } else {
                    System.out.printf("%15s %15s ", "na", "na");
                }
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
