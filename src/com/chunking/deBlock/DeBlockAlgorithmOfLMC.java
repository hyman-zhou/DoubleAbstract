package com.chunking.deBlock;

import com.chunking.commonInf.AbstractOfString;
import com.chunking.commonInf.MaxValue;
import com.chunking.dataStore.DataStoreStringHashSet;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

/**
 * Administrator on 2018.12.17 14:50.
 */

/**
 * |*******lenOfWindow*****|maxValue&&cutPoint|*******lenOfWindow*****|
 * !!!
 */
public class DeBlockAlgorithmOfLMC {
    private int lenOfWindow;
    private String fileName;
    int[] valuesOfWindow; //暂存  |***lenOfWindow-1|fileIndex| 这个区间的数据
    private ClassOfParameters para;

    private AbstractOfString abstractOfString_m;
    private DataStoreStringHashSet dataStroe_m;
    {
        abstractOfString_m=new AbstractOfString();
        dataStroe_m=new DataStoreStringHashSet();

        para=new ClassOfParameters();
        lenOfWindow=para.lenOfWindow;
        fileName=para.fileName;
        valuesOfWindow = new int[lenOfWindow+1];
    }
    private void setLenOfWindwo(int lenOfWindow){this.lenOfWindow=lenOfWindow;}
    private int getLenOfWindow(){return this.lenOfWindow;}

    public void deBlockAlgorithmOfLmc(){
        deBlock();
//        try {
//            checkSamePart();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        int startIndex=0;//当前分块的起始下标
        int candidateMaxIndex=0;//有问题最大值的情况下，候选最大值的下标
        int candidateLength=0; //有问题最大值的情况下，候选最大值已成功匹配的次数
        int lock=0;
        int testTimes1000=0,testTimes3000=0,testTimes5000=0,testTimes10000=0,testTimesBig=0;//测试用，判断是否落入大区间内
        int[] numOfChunk=new int[20000];
        int numOfBigChunk=0;
        String blockString=""; //当前区块包含的字节的hexstring
        String intTo16String;
        while ((byteRead = in.read()) != -1) {

//            intTo16String = String.format("%02x",byteRead);//String.valueOf(byteRead);
//            blockString+=intTo16String;

            valuesOfWindow[(fileIndex%(lenOfWindow+1))]=byteRead;
            if(fileIndex-startIndex>=lenOfWindow){
                if(lock==0){
                    candidateMaxIndex=startIndex+lenOfWindow;
                    lock=1;
                }
                if(byteRead>valuesOfWindow[(candidateMaxIndex%(lenOfWindow+1))]){
                    //更改候选最大值下标
                    candidateMaxIndex=fileIndex;
                    candidateLength=0;
                }
                if(valuesOfWindow[(candidateMaxIndex%(lenOfWindow+1))]>=valuesOfWindow[((fileIndex+1)%(lenOfWindow+1))]){
                    if(++candidateLength>=lenOfWindow+1){
                        //找到切点,之所以多判断一次，是因为要等下一轮的byteRead去跟候选值比较

//                        dataStroe_m.add(blockString);
//                        blockString="";

                        //分块计数
//                        int distance=candidateMaxIndex-startIndex;
//                        if(distance<20000){
//                            numOfChunk[distance]++;
//                        }else {
//                            numOfBigChunk++;
//                        }

//                        int distance=candidateMaxIndex-startIndex;//(candidateMaxIndex-startIndex+0x10000000)&0xfffffff;
//                        if(distance<=1000){
//                            testTimes1000++;
//                        }
//                        if(distance>1000&&distance<=3000){
//                            testTimes3000++;
//                        }
//                        if(distance>3000&&distance<=5000){
//                            testTimes5000++;
//                        }
//                        if(distance>5000&&distance<=10000){
//                            testTimes10000++;
//                        }
//                        if(distance>10000){
//                            testTimesBig++;
//                        }
                        startIndex=candidateMaxIndex+1;//(candidateMaxIndex+1)&0xfffffff;//防止超出int范围
                        lock=0;

                        candidateLength=0;
                    }
                }else{
                    candidateMaxIndex=fileIndex;//fileIndex&0xfffffff;//防止超出int范围.从当前下标设为候选最大值，虽然没有之前的候选大，但有可能相等
                    candidateLength=0;
                }
            }
            fileIndex++;//=(fileIndex+1)&0xfffffff;//防止超出int范围;
        }
        endTime = currentTimeMillis();
//        try {
//            FileOutputStream fileOut = new FileOutputStream("e://a_LMC.txt");
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
        System.out.printf("Time to deBlock with LMC: %d ms. %d,%d,%d,%d,%d\n",endTime-startTime,testTimes1000,testTimes3000,testTimes5000,testTimes10000,testTimesBig);
        System.out.println("End of deblock by LMC!");
        return jsonResult;
    }
    private int getMaxIndex(int[] a){
        int maxIndex=0;
        for(int i=0;i<a.length;i++){
            if(a[i]>=a[maxIndex]) maxIndex=i;
        }
        return maxIndex;
    }
    private int getMaxIndex(int[] a,int len){
        int maxIndex=0;
        for(int i=0;i<len;i++){
            if(a[i]>=a[maxIndex]) maxIndex=i;
        }
        return maxIndex;
    }

    private void checkSamePart() throws IOException {
        String[] exNmae=fileName.split("\\.");
        ArrayList<String> fileList = new ArrayList<>();
        fileList.add(exNmae[0]+"_Add.txt");
        fileList.add(exNmae[0]+"_Delete.txt");
        fileList.add(exNmae[0]+"_Insert.txt");

        for(String testFile:fileList){
            try {
                InputStream fileIn = new FileInputStream(testFile);
                BufferedInputStream bufferIn = new BufferedInputStream(fileIn);

                int byteRead; //读取的字节
                int blockIndex=0; //区块下标
                int fileIndex=0; //文件下标
                int startIndex=0;//当前分块的起始下标
                int candidateMaxIndex=0;//有问题最大值的情况下，候选最大值的下标
                int candidateLength=0; //有问题最大值的情况下，候选最大值已成功匹配的次数
                int lock=0;
                String blockString=""; //当前区块包含的字节的hexstring
                String intTo16String;
                int sameBytes=0;
                while ((byteRead = bufferIn.read()) != -1) {

                    intTo16String = String.format("%02x",byteRead);//String.valueOf(byteRead);
                    blockString+=intTo16String;

                    valuesOfWindow[(fileIndex%(lenOfWindow+1))]=byteRead;
                    if(fileIndex-startIndex>=lenOfWindow){
                        if(lock==0){
                            candidateMaxIndex=startIndex+lenOfWindow;
                            lock=1;
                        }
                        if(byteRead>valuesOfWindow[(candidateMaxIndex%(lenOfWindow+1))]){
                            //更改候选最大值下标
                            candidateMaxIndex=fileIndex;
                            candidateLength=0;
                        }
                        if(valuesOfWindow[(candidateMaxIndex%(lenOfWindow+1))]>=valuesOfWindow[((fileIndex+1)%(lenOfWindow+1))]){
                            if(++candidateLength>=lenOfWindow+1){
                                //找到切点,之所以多判断一次，是因为要等下一轮的byteRead去跟候选值比较

                                if(dataStroe_m.contains(blockString)){
                                    int distance=candidateMaxIndex-startIndex+1;
                                    sameBytes+=distance;
                                    //System.out.println("发现重复块！");
                                }
                                blockString="";
                                startIndex=candidateMaxIndex+1;//(candidateMaxIndex+1)&0xfffffff;//防止超出int范围
                                lock=0;

                                candidateLength=0;
                            }
                        }else{
                            candidateMaxIndex=fileIndex;//fileIndex&0xfffffff;//防止超出int范围.从当前下标设为候选最大值，虽然没有之前的候选大，但有可能相等
                            candidateLength=0;
                        }
                    }
                    fileIndex++;//=(fileIndex+1)&0xfffffff;//防止超出int范围;
                }
                System.out.printf("LMC---%s:发现重复数据 %d 个字节\n",testFile,sameBytes);

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
