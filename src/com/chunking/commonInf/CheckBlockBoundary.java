package com.chunking.commonInf;

/**
 * Administrator on 2018.11.28 14:38.
 */
public class CheckBlockBoundary {
    private int blockSize;  //固定分块的分块大小
    {
        blockSize=100;
    }
    public final void setBlocksize(int blockSize){this.blockSize=blockSize;}
    public final int getBlocksize(){return this.blockSize;}

    /**
     * 固定分块判断
     * @param hexString
     * @return
     */
    public boolean isFixedBlock(String hexString){
        int len=hexString.length();
        return len%2!=0||len==blockSize*2;
    }

    /**
     * CAAM分块算法，传递固定窗口数据|[固定窗口]******************[单字节]|
     * @param hexString，窗口数据的16进制字符串
     * @return
     */
    public int getMaxOfCAAMWin(String hexString){
        StringAndByte stringAndByteHandle=new StringAndByte();
        return stringAndByteHandle.maxByteInHexString(hexString);
    }

    /**
     * 判断是否是AE分块算法的边界|******************[单字节][固定窗口]|
     * @param hexString 从上一个边界的下一个字节开始的字节hexString格式
     * @param sizeOfWin 固定窗口的窗口大小
     * @return
     */
    public boolean isAEBlock(String hexString,int sizeOfWin){
        int len=hexString.length();
        if(len<=sizeOfWin*2) return false;
        int valueOfFront = Integer.parseInt(hexString.substring(len-sizeOfWin*2-2,len-sizeOfWin*2),16);
        StringAndByte stringAndByteHandle=new StringAndByte();
        int maxOfWin=stringAndByteHandle.maxByteInHexString(hexString.substring(len-sizeOfWin*2));
        return valueOfFront>=maxOfWin;
    }

    /**
     * 判断是否是LMC分块算法的边界|******************[固定窗口][区间][固定窗口]|
     * @param hexString 从上一个边界的下一个字节开始的字节hexString格式
     * @param sizeOfWin 固定窗口的窗口大小
     * @return
     */
    public boolean isLMCBlock(String hexString,int sizeOfWin){
        int len=hexString.length();
        if(len<=sizeOfWin*4) return false;
        int valueOfFront = Integer.parseInt(hexString.substring(len-sizeOfWin*2-2,len-sizeOfWin*2),16);
        StringAndByte stringAndByteHandle=new StringAndByte();
        int maxOfWinR=stringAndByteHandle.maxByteInHexString(hexString.substring(len-sizeOfWin*2));
        int maxOfWinL=stringAndByteHandle.maxByteInHexString(hexString.substring(len-sizeOfWin*4-2,len-sizeOfWin*2-2));
        return valueOfFront>=maxOfWinR&&valueOfFront>=maxOfWinL;
    }

    /**
     * 局部极小区间算法的边界  |******************[区间][固定窗口]|
     * @param hexString 从上一个边界的下一个字节开始的字节hexString格式
     * @param sizeOfWin 固定窗口大小
     * @param sizeOfInterval 区间大小
     * @return
     */
    public boolean isLMIBlock(String hexString,int sizeOfWin,int sizeOfInterval){
        int len=hexString.length();
        if(len<sizeOfWin*2+sizeOfInterval*2) return false;
        String strOfInterval=hexString.substring(len-sizeOfWin*2-sizeOfInterval*2,len-sizeOfWin*2);
        String strOfWindow=hexString.substring(len-sizeOfWin*2);
        StringAndByte stringAndByteHandle=new StringAndByte();
        int sumOfInterval=stringAndByteHandle.maxSumInHexString(strOfInterval,sizeOfInterval);
        //sumOfInterval=sumOfInterval>255?sumOfInterval:255;
        int SumOfWin=stringAndByteHandle.maxSumInHexString(strOfWindow,sizeOfInterval);
        return sumOfInterval>=SumOfWin;
    }

    /**
     * Box-Muler算法对两个字节进行计算后，其值将服从标准正态分布，然后通过值判断是否在参数设置的区间内
     * 来决定当前两个字节是否符合特定的分类特征，即可以作为边界。
     * 注：此处为一种分类算法，通过这种分类算法可以防止某个区块变化后会影响其他的区块
     * @param hexString
     * @param paraOfInterval 计算后的值在 0~paraOfInterval正无穷之间
     * @return
     */
    public boolean isBMBlock(String hexString,double paraOfInterval){
        int len=hexString.length();
        if(len<4) return false;
        String firstOfInterval=hexString.substring(len-4,len-2);
        String secondOfWindow=hexString.substring(len-2);
        double a = Integer.parseInt(firstOfInterval,16)*1.0/255;
        double b = Integer.parseInt(secondOfWindow,16)*1.0/255;
        double normalDistribution = Math.sqrt(-2*Math.log(a))*Math.cos(2*Math.PI*b);

        return normalDistribution>0&&normalDistribution<paraOfInterval;
    }
}
