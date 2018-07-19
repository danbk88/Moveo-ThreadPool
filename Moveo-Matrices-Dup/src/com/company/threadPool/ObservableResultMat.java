package com.company.threadPool;

import java.util.Observable;

public class ObservableResultMat extends Observable {
    int[][] m_resultMat;
    int m_Dim;
    int m_NumOfMult = 1;

    public int[][] GetMatResult(){return m_resultMat;}

    public int GetNumOfMult(){return m_NumOfMult; }

    public void IncNumOfMult(){m_NumOfMult++; }

    public void SetResult(int row, int col, int val){
        m_resultMat[row][col] = val;
        if(row == col && row == m_Dim -1){
            // Result matrix is fully calculated:
            setChanged();
            notifyObservers();
        }
    }

    public void FirstSet(int[][] resultMat) {
        m_resultMat = resultMat;
        m_Dim = m_resultMat[0].length;
    }
}
