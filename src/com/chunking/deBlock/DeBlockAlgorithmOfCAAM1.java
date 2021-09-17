package com.chunking.deBlock;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Administrator on 2018.12.17 14:50.
 */
public class DeBlockAlgorithmOfCAAM1 {
    private int lenOfWindow = 700;
    private String fileName = "E:/1.txt";

    public void deBlockAlgorithmOfCaam() {
        deBlockOfCAAM();
    }

    private JSONObject deBlockOfCAAM() {
        try {
            InputStream fileIn = new FileInputStream(fileName);
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            JSONObject jsonResult = deBlockFromInputBuffer(bufferIn);
            bufferIn.close();
            fileIn.close();
            return jsonResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject deBlockFromInputBuffer(BufferedInputStream in) throws IOException {
        JSONObject jsonResult = new JSONObject();
        int byteRead; //读取的字节
        int maxValue = -1;
        int fileIndex = 0; //文件下标
        int startIndex = 0;
        while ((byteRead = in.read()) != -1) {
            if (byteRead >= maxValue) {
                maxValue = byteRead;
                if (fileIndex >= startIndex + lenOfWindow) {
                    //找到一个切点
                    maxValue = -1;
                    startIndex = fileIndex + 1;//(fileIndex+1)&0xfffffff;//防止超出int范围
                }
            }
            fileIndex++;//=(fileIndex+1)&0xfffffff;//防止超出int范围;
        }
        return jsonResult;
    }
}
