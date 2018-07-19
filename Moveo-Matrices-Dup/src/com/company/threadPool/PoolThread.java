package com.company.threadPool;
import com.company.tasks.SingleRowColMulTask;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

public class PoolThread extends Thread {
    //MEMBERS:
    private BlockingQueue m_Taskqueue = null;
    private boolean m_IsStopped;
    private int m_TaskCalcResult;
    private ObservableResultMat m_ResultMat;

    //CTOR:
    public PoolThread(BlockingQueue queue, int[][] resultMat){
        m_Taskqueue = queue;
        m_IsStopped = false;
        initResultMat(resultMat);
    }

    //METHODS:
    private void initResultMat(int[][] resultMat) {
        m_ResultMat = new ObservableResultMat();
        m_ResultMat.FirstSet(resultMat);
    }

    public void run(){
         while(!m_IsStopped){
             try {
                 SingleRowColMulTask currTask = (SingleRowColMulTask) m_Taskqueue.take();
                 m_TaskCalcResult = currTask.call();
                 updateReusltMat(currTask.getRowNum(), currTask.getColNum());
             } catch (InterruptedException e) {
                 e.printStackTrace();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }

    private void updateReusltMat(int rowNum, int colNum) {
        m_ResultMat.SetResult(rowNum, colNum, m_TaskCalcResult);
    }

    public void stopThread() {
        m_IsStopped = true;
        System.exit(0);
    }

    public void AddResultMatListener(Observer m_resultMatListener) {
        m_ResultMat.addObserver(m_resultMatListener);
    }
}
