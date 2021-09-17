package com.chunking.deBlock;

import com.chunking.commonInf.AbstractOfString;
import com.chunking.dataStore.DataStoreStringHashSet;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

/**
 * Administrator on 2018.12.17 14:50.
 */
//Minimal Incremental Interval 极小递增区间
public class DeBlockAlgorithmOfMII {
    private int lenOfWindow;
    private String fileName;
    private ClassOfParameters para;

    private AbstractOfString abstractOfString_m;
    private DataStoreStringHashSet dataStroe_m;
    {
        abstractOfString_m=new AbstractOfString();
        dataStroe_m=new DataStoreStringHashSet();

        para=new ClassOfParameters();
        lenOfWindow=5;
        fileName=para.fileName;
    }
    private void setLenOfWindwo(int lenOfWindow){this.lenOfWindow=lenOfWindow;}
    private int getLenOfWindow(){return this.lenOfWindow;}

    public void deBlockAlgorithmOfMii(){
        deBlock();
        try {
            checkSamePart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private JSONObject deBlock(){
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
        long startTime,endTime;
        startTime=currentTimeMillis();
        JSONObject jsonResult = new JSONObject();
        int byteRead; //读取的字节
        int blockIndex=0; //区块下标
        int fileIndex=0; //文件下标
        int frontByte=-1;
        int incrementTimes=0;
        int startIndex=0;
        int testTimes1000=0,testTimes3000=0,testTimes5000=0,testTimes10000=0,testTimesBig=0;//测试用，判断是否落入大区间内
        int[] numOfChunk=new int[20000];
        int numOfBigChunk=0;
        String blockString=""; //当前区块包含的字节的hexstring
        String intTo16String;
        while ((byteRead = in.read()) != -1) {

            intTo16String = String.format("%02x",byteRead);//String.valueOf(byteRead);
            blockString+=intTo16String;

            if(byteRead>frontByte){
                incrementTimes++;
                if(incrementTimes>=lenOfWindow){
                    //找到新切点
                    //System.out.printf("Time to deBlock with MII block %d: %d 个字节.\n",blockIndex++,fileIndex-startIndex+1);

                    dataStroe_m.add(blockString);
                    blockString="";

                    //分块计数
//                    int distance=fileIndex-startIndex;
//                    if(distance<20000){
//                        numOfChunk[distance]++;
//                    }else {
//                        numOfBigChunk++;
//                    }

//                    distance=fileIndex-startIndex;//(fileIndex-startIndex+0x10000000)&0xfffffff;
//                    if(distance<=1000){
//                        testTimes1000++;
//                    }
//                    if(distance>1000&&distance<=3000){
//                        testTimes3000++;
//                    }
//                    if(distance>3000&&distance<=5000){
//                        testTimes5000++;
//                    }
//                    if(distance>5000&&distance<=12000){
//                        testTimes10000++;
//                    }
//                    if(distance>12000){
//                        testTimesBig++;
//                    }

                    startIndex=fileIndex+1;//(fileIndex+1)&0xfffffff;//防止超出int范围
                    incrementTimes=0;
                }
            }else{
                incrementTimes=0;
            }
            frontByte=byteRead;
            fileIndex++;//=(fileIndex+1)&0xfffffff;//防止超出int范围;
        }
        endTime = currentTimeMillis();
//        try {
//            FileOutputStream fileOut = new FileOutputStream("e://a_MII.txt");
//            BufferedOutputStream dataOut=new BufferedOutputStream(fileOut);
//            for(int i=0;i<20000;i++){
//                dataOut.write((i+":"+numOfChunk[i]+"\n").getBytes());
//            }
//            dataOut.write("OUT:".getBytes());
//            dataOut.write((numOfBigChunk+"").getBytes());
//            dataOut.flush();
//            dataOut.close();
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.printf("Time to deBlock with MII: %d ms. %d,%d,%d,%d,%d\n",endTime-startTime,testTimes1000,testTimes3000,testTimes5000,testTimes10000,testTimesBig);
        System.out.println("End of deblock by MII!");
        return jsonResult;
    }

    private void checkSamePart() throws IOException {
        String[] exNmae=fileName.split("\\.");
        ArrayList<String> fileList = new ArrayList<>();
        //fileList.add(exNmae[0]+"_Add.txt");
        //fileList.add(exNmae[0]+"_Delete.txt");
        fileList.add(exNmae[0]+"_Insert.txt");

        for(String testFile:fileList){
            try {
                InputStream fileIn = new FileInputStream(testFile);
                BufferedInputStream bufferIn = new BufferedInputStream(fileIn);

                int byteRead; //读取的字节
                int fileIndex=0; //文件下标
                int frontByte=-1;
                int incrementTimes=0;
                int startIndex=0;
                String blockString=""; //当前区块包含的字节的hexstring
                String intTo16String;
                int sameBytes=0;
                while ((byteRead = bufferIn.read()) != -1) {

                    intTo16String = String.format("%02x",byteRead);//String.valueOf(byteRead);
                    blockString+=intTo16String;

                    if(byteRead>frontByte){
                        incrementTimes++;
                        if(incrementTimes>=lenOfWindow){
                            //找到新切点

                            if(dataStroe_m.contains(blockString)){
                                int distance=fileIndex-startIndex+1;
                                sameBytes+=distance;
                                //System.out.println("发现重复块！");
                            }

                            blockString="";

                            startIndex=fileIndex+1;//(fileIndex+1)&0xfffffff;//防止超出int范围
                            incrementTimes=0;
                        }
                    }else{
                        incrementTimes=0;
                    }
                    frontByte=byteRead;
                    fileIndex++;//=(fileIndex+1)&0xfffffff;//防止超出int范围;
                }

                System.out.printf("MII---%s:发现重复数据 %d 个字节\n",testFile,sameBytes);

                bufferIn.close();
                fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataStroe_m.clear();
        return ;
    }
}
