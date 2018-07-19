package com.company;

import com.company.UIManager.UIManager;
import com.company.tasks.SingleRowColMulTask;
import com.company.threadPool.MyThreadPool;
import com.company.threadPool.ObservableResultMat;

import java.util.*;

public class MatricesCalculator implements Observer{
    // CONSTANTS:
    private static final int MAX_RANDOM = 10;
    private static final String NUM_OF_THREADS_STR = "Welcome to My calculator, Enter Num of threads (2-20) or 0 to exit:";
    private static final String NUM_OF_MATRICES_STR = "Enter Num of matrices (greater then 2):";
    private static final String MATRICES_DIMENSION_STR = "Enter matrices dimension:";
    private static final String MULT_STARTED_STR = "Matrices multiplication has started...";
    private static final String GOODBYE_MSG = "Goodbye :))";
    private static final String STOP_THREADS_MSG = "Stoping all threads in pool...";



    // MEMBERS:
    private UIManager m_UImanager;
    private Random m_rand;
    private int[][] m_ResutlMat;
    private int m_NumOfThreads, m_NumOfMatrices, m_MatDimension;
    private List<int[][]> m_Matrices;
    private MyThreadPool m_ThreadPool;
    private boolean m_keepGoing;

    // CTOR:
    public MatricesCalculator(){
        m_UImanager = new UIManager();
        m_rand = new Random();
        m_Matrices = null;
        m_ThreadPool = null;
        m_keepGoing = true;
    }

    // METHODS:
    public void Run() {
        startCalculator();
    }

    private void startCalculator() {
        // Get all user inputs:
        handleUserInput();
        if(m_keepGoing){
            // Create result matrix:
            createResultMatrix();
            // Create and print matrices:
            createAndPrintMatrices();
            // Create ThreadPool:
            createThreadPool();
            // Multiply matrices - result in this.resultMat/ Create and add tasks:???
            _CalcMultResult();
        }
        else{
            //user wants to exit:
            onExit_Pressed();
        }
    }

    private void onExit_Pressed() {
        m_UImanager.Print(STOP_THREADS_MSG);
        m_UImanager.Print(GOODBYE_MSG);
        if(m_ThreadPool != null){
            m_ThreadPool.stopThreadPool();
        }
    }

    private void handleUserInput() {
        // Get num of threads from user:
        do{
            getNumOfThreads();
        }while(!isNumOfThreadsValid() && m_keepGoing);
        if(m_keepGoing){
            // Get num of matrices from user:
            do{
                getNumOfMatrices();
            }while(!isNumOfMatricesValid());
            // Get matrices dimension from user:
            getMatDimension();
        }
    }

    private boolean isNumOfMatricesValid() {
        boolean retval = true;

        if(m_NumOfMatrices < 2){
            m_UImanager.PrintNumOfMatricesError();
            retval = false;
        }
        return retval;
    }

    private boolean isNumOfThreadsValid() {
        boolean retval = true;

        if((m_NumOfThreads<2 && m_NumOfThreads!=0) ||m_NumOfThreads > 20){
            m_UImanager.PrintNumOfThreadsError();
            retval = false;
        }
        return retval;
    }

    private void createAndPrintMatrices() {
        m_Matrices = createMatrices(m_NumOfMatrices, m_MatDimension);
        m_UImanager.PrintAllMatrices(m_Matrices);
    }

    private void createResultMatrix() {
        m_ResutlMat = new int[m_MatDimension][m_MatDimension];
    }

    private void getMatDimension() {
        m_MatDimension = m_UImanager.GetInputFromUser(MATRICES_DIMENSION_STR);
    }

    private void getNumOfMatrices() {
        m_NumOfMatrices = m_UImanager.GetInputFromUser(NUM_OF_MATRICES_STR);
    }

    private void getNumOfThreads() {
        m_NumOfThreads = m_UImanager.GetInputFromUser(NUM_OF_THREADS_STR);
        if(m_NumOfThreads == 0){
            m_keepGoing = false;
        }
    }

    private void createThreadPool() {
        m_ThreadPool = new MyThreadPool(this, m_NumOfThreads, m_MatDimension,this.m_ResutlMat);
    }

    private void _CalcMultResult() {
        m_UImanager.Print(MULT_STARTED_STR);
        CalcMultResult(m_Matrices.get(0), m_Matrices.get(1));
    }

    private void CalcMultResult(int[][] mat1, int[][] mat2) {
        // Create Tasks and execute them:
        m_UImanager.Print2Matrices(mat1, mat2);
        for(int i = 0; i< m_MatDimension; i++){
            for (int j = 0; j< m_MatDimension; j++){
                SingleRowColMulTask task = new SingleRowColMulTask(mat1[i], getRow(mat2, j), i, j);
                this.m_ThreadPool.execute(task);
            }
        }
    }

    private int[] getRow(int[][] mat, int colIndex) {
        int dim = mat[0].length;
        int[] col = new int[dim];
        for(int i=0;i<dim;i++){
            col[i] = mat[i][colIndex];
        }

        return col;
    }

    private List<int[][]> createMatrices(int numOfMatrices, int matDimension) {
        List<int[][]> matrices = new ArrayList<>();

        for(int i=0;i<numOfMatrices;i++){
            matrices.add(createSingleMat(matDimension));
        }

        return matrices;
    }

    private int[][] createSingleMat(int matDimension) {
        int[][] mat = new int[matDimension][matDimension];

        for(int i=0; i<matDimension; i++){
            for(int j=0; j<matDimension; j++) {
                mat[i][j] = getRandomNumber();
            }
        }

        return mat;
    }

    private int getRandomNumber() {
        return m_rand.nextInt(MAX_RANDOM);
    }

    @Override
    public void update(Observable observer, Object arg) {
        onSingleMultDone((ObservableResultMat)observer);
    }

    private void onSingleMultDone(ObservableResultMat resultMat) {
        int numOfMult = resultMat.GetNumOfMult();

        if(numOfMult == m_NumOfMatrices - 1){
            // Calc is over:
            m_UImanager.PrintResults(m_ResutlMat);
            startCalculator();
        }
        else{
            // Calc is not over:
            resultMat.IncNumOfMult();
            numOfMult++;
            m_ResutlMat = resultMat.GetMatResult();
            m_UImanager.Print("subResult Matrix:");
            m_UImanager.printMatrix(m_ResutlMat);
            sleep(200);
            CalcMultResult(getClone(m_ResutlMat), m_Matrices.get(numOfMult));
        }
    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int[][] getClone(int[][] Mat) {
        int matDim = Mat[0].length;
        int[][] clone = new int[matDim][matDim];

        for(int i=0;i<matDim;i++){
            for(int j=0;j<matDim;j++) {
                clone[i][j] = Mat[i][j];
            }
        }

        return clone;
    }
}
