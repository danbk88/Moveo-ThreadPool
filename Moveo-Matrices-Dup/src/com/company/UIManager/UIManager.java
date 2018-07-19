package com.company.UIManager;

import java.util.List;
import java.util.Scanner;

public class UIManager {


    private Scanner m_Scanner = new Scanner(System.in);


    public int GetInputFromUser(String strToUser) {
        System.out.println(strToUser);
        return m_Scanner.nextInt();
    }

    public void PrintAllMatrices(List<int[][]> matrices) {
        int i=1;
        System.out.println("Matrices: ");
        for(int[][] mat: matrices){
            System.out.println("Matrix" + i++ + ":");
            printMatrix(mat);
        }
    }

    public void printMatrix(int[][] mat) {
        int dim = mat.length;
        String row;
        for(int i=0;i<dim;i++){
            row="";
            for(int j=0;j<dim;j++) {
                row+= mat[i][j] + " ";
            }
            System.out.println(row);
        }
    }

    public void Print(String s) {
        System.out.println(s);
    }

    public void Print2Matrices(int[][] mat1, int[][] mat2) {
        System.out.println("Calculating mat1*mat2...");
        System.out.println("mat1:");
        printMatrix(mat1);
        System.out.println("mat2:");
        printMatrix(mat2);
    }

    public void PrintResults(int[][] resutlMat) {
        System.out.println("Calculation is over !");
        System.out.println("Result Matrix:");
        printMatrix(resutlMat);
    }

    public void PrintNumOfMatricesError() {
        System.out.println("Error - Enter Number greater then 2");
    }

    public void PrintNumOfThreadsError() {
        System.out.println("Error - Enter Number Between 2 to 20");
    }
}
