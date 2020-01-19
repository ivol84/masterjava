package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        if (!isCompatible(matrixA, matrixB)) {
            throw new IllegalArgumentException();
        }

        final int matrixARowCount = matrixA.length;
        final int matrixAColCount = matrixA[0].length;
        final int matrixBRowCount = matrixB.length;
        final int matrixBColCount = matrixB[0].length;

        int[][] matrixC = new int[matrixARowCount][matrixBColCount];

        int[] bRowAsColumn = new int[matrixBRowCount];
        class ExecutionResult {
            private int row;
            private int column;
            private int sum;

            public ExecutionResult(int row, int column, int sum) {
                this.row = row;
                this.column = column;
                this.sum = sum;
            }

            public int getRow() {
                return row;
            }

            public int getColumn() {
                return column;
            }

            public int getSum() {
                return sum;
            }

            @Override
            public String toString() {
                return "ExecutionResult{" +
                        "row=" + row +
                        ", column=" + column +
                        ", sum=" + sum +
                        '}';
            }
        }
        ExecutorCompletionService<List<ExecutionResult>> listExecutorCompletionService = new ExecutorCompletionService<>(executor);
        List<Future<List<ExecutionResult>>> futures = new ArrayList();
        for (int matrixBColIndex = 0; matrixBColIndex < matrixBColCount; matrixBColIndex++) {
            for (int matrixBRowIndex = 0; matrixBRowIndex < matrixBRowCount; matrixBRowIndex++) {
                bRowAsColumn[matrixBRowIndex] = matrixB[matrixBRowIndex][matrixBColIndex];
            }
            int finalMatrixBColIndex = matrixBColIndex;
            int[] bRowAsColumnCopy = new int[matrixBRowCount];
            System.arraycopy(bRowAsColumn, 0, bRowAsColumnCopy, 0, matrixBRowCount);
            futures.add(listExecutorCompletionService.submit(() -> {
                List<ExecutionResult> results = new ArrayList();
                for (int aRowIndex = 0; aRowIndex < matrixARowCount; aRowIndex++) {
                    int[] aRow = matrixA[aRowIndex];
                    int sum = 0;
                    for (int aColIndex = 0; aColIndex < matrixAColCount; aColIndex++) {
                        sum += aRow[aColIndex] * bRowAsColumnCopy[aColIndex];
                    }
                    results.add(new ExecutionResult(aRowIndex, finalMatrixBColIndex, sum));
                }
                return results;
            }));
        }
        for (int i=0; i<futures.size(); i++) {
            List<ExecutionResult> executionResults = listExecutorCompletionService.take().get();
            executionResults.stream().forEach(er -> matrixC[er.getRow()][er.getColumn()] = er.getSum());
        }
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        if (!isCompatible(matrixA, matrixB)) {
            throw new IllegalArgumentException();
        }
        final int matrixARowCount = matrixA.length;
        final int matrixAColCount = matrixA[0].length;
        final int matrixBRowCount = matrixB.length;
        final int matrixBColCount = matrixB[0].length;

        int[][] matrixC = new int[matrixARowCount][matrixBColCount];

        int[] bRowAsColumn = new int[matrixBRowCount];
        for (int matrixBColIndex = 0; matrixBColIndex < matrixBColCount; matrixBColIndex++) {
            for (int matrixBRowIndex = 0; matrixBRowIndex < matrixBRowCount; matrixBRowIndex++) {
                bRowAsColumn[matrixBRowIndex] = matrixB[matrixBRowIndex][matrixBColIndex];
            }
            for (int aRowIndex = 0; aRowIndex < matrixARowCount; aRowIndex++) {
                int[] aRow = matrixA[aRowIndex];
                int sum = 0;
                for (int aColIndex = 0; aColIndex < matrixAColCount; aColIndex++) {
                    sum += aRow[aColIndex] * bRowAsColumn[aColIndex];
                }
                matrixC[aRowIndex][matrixBColIndex] = sum;
            }
        }
        return matrixC;
    }

    private static boolean isCompatible(int[][] a, int[][] b) {
        return a[0].length == b.length;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static void print(int[] matrix) {
        for (int col = 0; col < matrix.length; col++) {
            System.out.print(matrix[col] + " ");
        }
    }

    public static void print(int[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            print(matrix[row]);
            System.out.println();
        }
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
