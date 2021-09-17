package com.chunking.deBlock;

import com.chunking.commonInf.CheckBlockBoundary;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.currentTimeMillis;

/**
 * Administrator on 2018.12.17 14:50.
 */
//sum of Interval 区间之和
public class DeBlockAlgorithmOfSOI {
    private int lenOfWindow;
    private String fileName;
    int thresholdValue;
    private CheckBlockBoundary handleOfCheckBoundary;
    private ClassOfParameters para;
    {
        para=new ClassOfParameters();
        fileName=para.fileName;

        thresholdValue=800;
        lenOfWindow=1000;
        //fileName="e://bigsize.rmvb";
        handleOfCheckBoundary = new CheckBlockBoundary();
    }
    private void setLenOfWindwo(int lenOfWindow){this.lenOfWindow=lenOfWindow;}
    private int getLenOfWindow(){return this.lenOfWindow;}

    public void deBlockAlgorithmOfSoi(){
        deBlock();
    }
    private JSONObject deBlock(){
        try {
            InputStream fileIn = new FileInputStream(fileName);
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            return deBlockFromInputBuffer(bufferIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private JSONObject deBlockFromInputBuffer(BufferedInputStream in) throws IOException {
        long startTime,endTime;
        startTime=currentTimeMillis();
        JSONObject jsonResult = new JSONObject();
        int byteRead; //读取的字节
        int blockIndex=0; //区块下标
        int fileIndex=0; //文件下标
        String blockString=""; //当前区块包含的字节的hexstring
        int value1,value2,value3,value4,value5;
        value1=value2=value3=value4=value5=-1;
        int startIndex=0;
        int testTimes1000=0,testTimes3000=0,testTimes5000=0,testTimes10000=0,testTimesBig=0;//测试用，判断是否落入大区间内
        while ((byteRead = in.read()) != -1) {
            if(fileIndex-startIndex<4){
                if(fileIndex-startIndex==0) value1=byteRead;
                if(fileIndex-startIndex==1) value2=byteRead;
                if(fileIndex-startIndex==2) value3=byteRead;
                if(fileIndex-startIndex==3) value4=byteRead;
            }else{
                if(fileIndex-startIndex==4){
                    value5=byteRead;
                }else{
                    value1=value2;
                    value2=value3;
                    value3=value4;
                    value4=value5;
                    value5=byteRead;
                }
                int sum = value1+value2+value3+value4+value5;
                if(sum>=1230//thresholdValue*1.5
                        ||fileIndex-startIndex>=lenOfWindow&&sum>=1200
                        ||fileIndex-startIndex>=lenOfWindow*10&&sum>=1170){
                    //找到新切点
                    //System.out.printf("Time to deBlock with SOI block %d: %d 个字节.\n",blockIndex++,fileIndex-startIndex+1);
                    int distance=(fileIndex-startIndex+0x10000000)&0xfffffff;
                    if(distance<=1000){
                        testTimes1000++;
                    }
                    if(distance>1000&&distance<=3000){
                        testTimes3000++;
                    }
                    if(distance>3000&&distance<=5000){
                        testTimes5000++;
                    }
                    if(distance>5000&&distance<=10000){
                        testTimes10000++;
                    }
                    if(distance>10000){
                        testTimesBig++;
                    }
                    startIndex=(fileIndex+1)&0xfffffff;//防止超出int范围
                    value1=value2=value3=value4=value5=-1;
                }
            }
            fileIndex=(fileIndex+1)&0xfffffff;//防止超出int范围;
        }
        endTime = currentTimeMillis();
        System.out.printf("Time to deBlock with SOI: %d ms. %d,%d,%d,%d,%d\n",endTime-startTime,testTimes1000,testTimes3000,testTimes5000,testTimes10000,testTimesBig);
        return jsonResult;
    }
}
