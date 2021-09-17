package com.chunking.deBlock;

import com.chunking.commonInf.AbstractOfString;
import com.chunking.commonInf.MaxValue;
import com.chunking.dataStore.DataStoreStringHashSet;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Administrator on 2018.12.17 14:50.
 */
public class DeBlockAlgorithmOfAE {
    private int lenOfWindow;
    private String fileName;
    private ClassOfParameters para;

    private AbstractOfString abstractOfString_m;
    private DataStoreStringHashSet dataStroe_m;
    {
        abstractOfString_m=new AbstractOfString();
        dataStroe_m=new DataStoreStringHashSet();

        para=new ClassOfParameters();
        lenOfWindow=para.lenOfWindow;
        fileName=para.fileName;
    }

    private final String[] HexStringTable = new String[]
            {
                    "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F",
                    "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F",
                    "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F",
                    "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
                    "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F",
                    "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F",
                    "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F",
                    "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F",
                    "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F",
                    "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
                    "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF",
                    "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF",
                    "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF",
                    "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF",
                    "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF",
                    "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"
            };

    private void setLenOfWindwo(int lenOfWindow){this.lenOfWindow=lenOfWindow;}
    private int getLenOfWindow(){return this.lenOfWindow;}

    public void deBlockAlgorithmOfAe(){
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
            deBlockFromInputBuffer(bufferIn);
            bufferIn.close();
            fileIn.close();
            return null;
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
        MaxValue maxValue=new MaxValue();
        int fileIndex=0; //文件下标
        int startIndex=0;
        int testTimes1000=0,testTimes3000=0,testTimes5000=0,testTimes10000=0,testTimesBig=0;//测试用，判断是否落入大区间内
        int[] numOfChunk=new int[20000];
        int numOfBigChunk=0;
        StringBuilder blockString = new StringBuilder("");
        String intTo16String;
        while ((byteRead = in.read()) != -1) {

            intTo16String = HexStringTable[byteRead];//String.valueOf(byteRead);
            blockString.append(intTo16String);

            if(byteRead<=maxValue.value){
                if(fileIndex>=maxValue.index+lenOfWindow){
                    //找到一个切点
                    maxValue.value=-1;
                    maxValue.index=-1;

                    //找到一个新的切点，需要存起来数据，并情况blockString
                    dataStroe_m.add(blockString.toString());
                    blockString.delete(0,blockString.length());

                    blockIndex++;


                    //分块计数
//                    int distance=fileIndex-startIndex;
//                    if(distance<20000){
//                        numOfChunk[distance]++;
//                    }else {
//                        numOfBigChunk++;
//                    }

//                    int distance=fileIndex-startIndex;//fileIndex-startIndex;//(fileIndex-startIndex+0x10000000)&0xfffffff;
//                    if(distance<=1000){
//                        testTimes1000++;
//                    }
//                    if(distance>1000&&distance<=3000){
//                        testTimes3000++;
//                    }
//                    if(distance>3000&&distance<=5000){
//                        testTimes5000++;
//                    }
//                    if(distance>5000&&distance<=10000){
//                        testTimes10000++;
//                    }
//                    if(distance>10000){
//                        testTimesBig++;
//                    }
                    startIndex=fileIndex+1;//fileIndex+1;//(fileIndex+1)&0xfffffff;//防止超出int范围
                }
            }else{
                maxValue.value=byteRead;
                maxValue.index=fileIndex;
            }
            fileIndex++;//fileIndex+1;//(fileIndex+1)&0xfffffff;//防止超出int范围;
        }
        endTime = currentTimeMillis();
//        try {
//            FileOutputStream fileOut = new FileOutputStream("e://a_AE.txt");
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
        System.out.printf("%d\n",blockIndex);
        System.out.println("End of deblock by AE!");
        return jsonResult;
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
                StringBuilder blockString = new StringBuilder("");
                String intTo16String;
                MaxValue maxValue=new MaxValue();
                long fileIndex=0; //文件下标
                long startIndex=0;
                long sameBytes=0;
                while ((byteRead = bufferIn.read()) != -1) {

                    intTo16String = HexStringTable[byteRead];//String.valueOf(byteRead);
                    blockString.append(intTo16String);

                    if(byteRead<=maxValue.value){
                        if(fileIndex>=maxValue.index+lenOfWindow){
                            //找到一个切点
                            maxValue.value=-1;
                            maxValue.index=-1;

                            //找到一个新的切点
                            if(dataStroe_m.contains(blockString.toString())){
                                long distance=fileIndex-startIndex+1;
                                sameBytes+=distance;
                                //System.out.println("发现重复块！");
                            }
                            blockString.delete(0,blockString.length());
                            startIndex=fileIndex+1;
                        }
                    }else{
                        maxValue.value=byteRead;
                        maxValue.index=fileIndex;
                    }
                    fileIndex++;
                }
                System.out.printf("AE---%s:发现重复数据 %d 个字节\n",testFile,sameBytes);

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
