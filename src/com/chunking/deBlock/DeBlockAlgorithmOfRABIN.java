package com.chunking.deBlock;

import com.chunking.commonInf.AbstractOfString;
import com.chunking.commonInf.MaxValue;
import com.chunking.dataStore.DataStoreStringHashSet;
import com.chunking.rabinHash.KarpRabinHash;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

/**
 * Administrator on 2018.12.17 14:50.
 */
public class DeBlockAlgorithmOfRABIN {
    private int sizeOfWindow; //窗口大小
    private String fileName;
    private long valueOfDest; //目标值
    private int[] valuesInWindow;
    private KarpRabinHash handleOfRabin;
    private ClassOfParameters para;

    private AbstractOfString abstractOfString_m;
    private DataStoreStringHashSet dataStroe_m;
    {
        abstractOfString_m=new AbstractOfString();
        dataStroe_m=new DataStoreStringHashSet();

        para=new ClassOfParameters();
        fileName=para.fileName;
        System.out.println(fileName);
        sizeOfWindow =7; //表示多少位用来计算Rabin，注意数值不要超过INT的范围，即n*8位二进制位的整数值要小于编译工具中的整型范围，不然此次实现将出现问题
        valueOfDest = 9L;//目标值
        handleOfRabin=new KarpRabinHash(sizeOfWindow,10); //wordsize:表示最后的结果是多少位的 例如5代表结果是5bit，即0-31
        valuesInWindow=new int[sizeOfWindow];
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

    public void deBlockAlgorithmOfRabin(){
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
        int numOfBytes=0;//记录Rabin窗口内已经有多少个字节
        handleOfRabin.reset();
        long res=-1;//Rabin结果
        int fileIndex=0; //文件下标
        int startIndex=0;
        int testTimes1000=0,testTimes3000=0,testTimes5000=0,testTimes10000=0,testTimesBig=0;//测试用，判断是否落入大区间内
        int[] numOfChunk=new int[20000];
        int numOfBigChunk=0;
        StringBuilder blockString = new StringBuilder("");
        String intTo16String;
        while ((byteRead = in.read()) != -1) {
//            intTo16String = HexStringTable[byteRead]; //blockString分块字节
//            blockString.append(intTo16String);

            if(numOfBytes<sizeOfWindow){
                valuesInWindow[numOfBytes]=byteRead;
                res=handleOfRabin.eat(byteRead);
                numOfBytes++;
            }
            else{
                //窗口内已经有sizeOfWindow个字节了
                if(res==valueOfDest){//先判断在吃字节之后的值是否符合要求，以免漏掉一次命中机会
                    //找到切点
                    for(int i=0;i<sizeOfWindow;i++)
                    {
                        valuesInWindow[i]=0;
                    }
                    handleOfRabin.reset();
                    numOfBytes=0;

                    dataStroe_m.add(blockString.toString()); //将分块存起来
                    blockString.delete(0,blockString.length());
                    blockIndex++;
                    //分块计数
                    int distance=fileIndex-startIndex;
                    if(distance<20000){
                        numOfChunk[distance]++; //分块长度
                    }else {
                        numOfBigChunk++;
                    }

//                    int distance=fileIndex-startIndex;//(fileIndex-startIndex+0x10000000)&0xfffffff;
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
                    startIndex=fileIndex+1;//(fileIndex+1)&0xfffffff;//防止超出int范围
                }else{
                    res=handleOfRabin.update(valuesInWindow[0],byteRead); //滚动计算指纹
                    //向右滑动
                    for(int i=0;i<sizeOfWindow-1;i++)
                    {
                        valuesInWindow[i]=valuesInWindow[i+1];
                    }
                    valuesInWindow[sizeOfWindow-1]=byteRead;
                }
            }
            fileIndex++;//=(fileIndex+1)&0xfffffff;//防止超出int范围;
        }
        endTime = currentTimeMillis();
        try {
            FileOutputStream fileOut = new FileOutputStream("e://a_RABIN.txt");
            BufferedOutputStream dataOut=new BufferedOutputStream(fileOut);
            for(int i=0;i<20000;i++){
                dataOut.write((i+":"+numOfChunk[i]+"\n").getBytes());
            }
            dataOut.write("OUT:".getBytes());
            dataOut.write((numOfBigChunk+"").getBytes());
            dataOut.flush();
            dataOut.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("耗时%d\n",endTime-startTime);
        System.out.printf("%d\n",blockIndex);
        System.out.println("End of deblock by RABIN!");
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
                int fileIndex=0; //文件下标
                int startIndex=0;
                int sameBytes=0;
                int numOfBytes=0;//记录Rabin窗口内已经有多少个字节
                long res=-1;//Rabin结果
                while ((byteRead = bufferIn.read()) != -1) {

                    intTo16String = HexStringTable[byteRead];//String.valueOf(byteRead);
                    blockString.append(intTo16String);

                    if(numOfBytes<sizeOfWindow){
                        valuesInWindow[numOfBytes]=byteRead;
                        res=handleOfRabin.eat(byteRead);
                        numOfBytes++;
                    }else{
                        //窗口内已经有sizeOfWindow个字节了
                        if(res==valueOfDest){//先判断在吃字节之后的值是否符合要求，以免漏掉一次命中机会
                            //找到切点
                            for(int i=0;i<sizeOfWindow;i++)
                            {
                                valuesInWindow[i]=0;
                            }
                            handleOfRabin.reset();
                            numOfBytes=0;

                            if(dataStroe_m.contains(blockString.toString())){
                                int distance=fileIndex-startIndex+1;
                                sameBytes+=distance;
                                //System.out.println("发现重复块！");
                            }

                            blockString.delete(0,blockString.length());
                            startIndex=fileIndex+1;//(fileIndex+1)&0xfffffff;//防止超出int范围
                        }else{
                            res=handleOfRabin.update(valuesInWindow[0],byteRead);
                            for(int i=0;i<sizeOfWindow-1;i++)
                            {
                                valuesInWindow[i]=valuesInWindow[i+1];
                            }
                            valuesInWindow[sizeOfWindow-1]=byteRead;
                        }
                    }
                    fileIndex++;//=(fileIndex+1)&0xfffffff;//防止超出int范围;
                }
                System.out.printf("RABIN---%s:发现重复数据 %d 个字节\n",testFile,sameBytes);

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
