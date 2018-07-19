package com.company.threadPool;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class MyThreadPool{

    //MEMBERS:
    private BlockingQueue m_TaskQueue;
    private List<PoolThread> m_Threads;
    private boolean m_IsStopped;

    // CTOR:
    public MyThreadPool(Observer Listener, int numOfThreadsInPool, int matDim, int[][] resultMat){

        m_IsStopped = false;
        m_TaskQueue = new ArrayBlockingQueue(matDim*matDim);
        m_Threads = new ArrayList<>();
        initThreadPool(numOfThreadsInPool, resultMat, Listener);
    }

    private void initThreadPool(int numOfThreadsInPool, int[][] resultMat, Observer listener) {
        // Create threads and add it to threads array:
        for(int i=0;i<numOfThreadsInPool; i++){
            PoolThread newPoolThread = new PoolThread(m_TaskQueue, resultMat);
            newPoolThread.AddResultMatListener(listener);
            m_Threads.add(newPoolThread);
        }
        // Start all Threads:
        for(PoolThread currThread: m_Threads){
            currThread.start();
        }
    }

    public synchronized void execute(Callable<Integer> task){
        if(this.m_IsStopped) throw new IllegalStateException("Thread pool has stopped");
        try {
            this.m_TaskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopThreadPool(){
        this.m_IsStopped = true;
        for(PoolThread currThread: m_Threads){
            currThread.stopThread();
        }
    }
}
