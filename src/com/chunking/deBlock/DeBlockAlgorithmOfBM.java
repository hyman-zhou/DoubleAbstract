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
public class DeBlockAlgorithmOfBM {
    private String fileName;
    private CheckBlockBoundary handleOfCheckBoundary;
    private double paraOfInterval;
    {
        //fileName="e://11.docx";
        fileName="e://bigsize.rmvb";
        handleOfCheckBoundary = new CheckBlockBoundary();
        paraOfInterval = 0.0035;
    }

    public void deBlockAlgorithmOfBm(){
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
        String blockString=""; //当前区块包含的字节的hexstring
        int maxByte=-1;//固定窗口中最大字节的数值
        while ((byteRead = in.read()) != -1) {
            String intTo16String = String.format("%02x",byteRead);
            blockString+=intTo16String;
            if(handleOfCheckBoundary.isBMBlock(blockString,paraOfInterval)){
                System.out.printf("%d---分块的长度为：%d\n",blockIndex++,blockString.length()/2);
                blockString="";
            }
        }
        endTime = currentTimeMillis();
        System.out.printf("Time to deBlock with BM: %d ms.\n",endTime-startTime);
        return jsonResult;
    }
}
