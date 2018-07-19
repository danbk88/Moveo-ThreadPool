package com.company.tasks;

import java.util.concurrent.Callable;

public class SingleRowColMulTask implements Callable<Integer> {

    //MEMBERS:
    private int[] m_Row, m_Col;
    private int m_MatDim;
    private int m_RowNum, m_ColNum;
    private int m_Result = 0;

    //CTOR:
     public SingleRowColMulTask(int[] row, int[] col, int rowNum, int colNum){
         m_Row = row;
         m_Col = col;
         m_MatDim = m_Col.length;
         m_RowNum = rowNum;
         m_ColNum = colNum;
     }

     //METHODS:
    @Override
    public Integer call() throws Exception {
        for(int i = 0; i< m_MatDim; i++){
            m_Result += m_Row[i]*m_Col[i];
        }
        return m_Result;
    }

    public int getRowNum() {
        return m_RowNum;
    }

    public int getColNum() {
        return m_ColNum;
    }
}
