package test;


import com.chunking.deBlock.*;
import com.chunking.rabinHash.MersenneTwister;
import com.chunking.sync.SyncAlgorithmOfSlideWindow;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Administrator on 2018.11.15 19:50.
 */
public class TestString {
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

    public void testRyscSlideWindwo() {
        SyncAlgorithmOfSlideWindow syncHandle = new SyncAlgorithmOfSlideWindow();
        syncHandle.SyncAlgorithm_sliwin();
    }

    public void testDeblockOfCAAM(){
        DeBlockAlgorithmOfCAAM deBlockHandle = new DeBlockAlgorithmOfCAAM();
        deBlockHandle.deBlockAlgorithmOfCaam();
    }

    public void testDeblockOfAE(){
        DeBlockAlgorithmOfAE deBlockHandle = new DeBlockAlgorithmOfAE();
        deBlockHandle.deBlockAlgorithmOfAe();
    }

    public void testDeblockOfLMC(){
        DeBlockAlgorithmOfLMC deBlockHandle = new DeBlockAlgorithmOfLMC();
        deBlockHandle.deBlockAlgorithmOfLmc();
    }

    public void testDeblockOfLMI(){
        DeBlockAlgorithmOfLMI deBlockHandle = new DeBlockAlgorithmOfLMI();
        deBlockHandle.deBlockAlgorithmOfLmi();
    }

    public void testDeblockOfBM(){
        DeBlockAlgorithmOfBM deBlockHandle = new DeBlockAlgorithmOfBM();
        deBlockHandle.deBlockAlgorithmOfBm();
    }

    public void testDeblockOfRABIN(){
        DeBlockAlgorithmOfRABIN deBlockHandle = new DeBlockAlgorithmOfRABIN();
        deBlockHandle.deBlockAlgorithmOfRabin();
    }

    public void testDeblockOfSOI(){
        DeBlockAlgorithmOfSOI deBlockHandle = new DeBlockAlgorithmOfSOI();
        deBlockHandle.deBlockAlgorithmOfSoi();
    }

    public void testDeblockOfMII(){
        DeBlockAlgorithmOfMII deBlockHandle = new DeBlockAlgorithmOfMII();
        deBlockHandle.deBlockAlgorithmOfMii();
    }

    public void testDeblockOfPCI(){
        DeBlockAlgorithmOfPCI deBlockHandle = new DeBlockAlgorithmOfPCI();
        deBlockHandle.deBlockAlgorithmOfPci();
    }

    public void testDeblockOfMII_Limit(){
        DeBlockAlgorithmOfMII_Limit deBlockHandle = new DeBlockAlgorithmOfMII_Limit();
        deBlockHandle.deBlockAlgorithmOfMii_Limit();
    }

    /**
     *???????????????????????????????????????????????????0~255????????????
     */
    public void testPartition() {
        InputStream fileIn = null;
        long[] array=new long[257];
        try {
            fileIn = new FileInputStream("e://all.txt");
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            int byteRead;
            while ((byteRead = bufferIn.read()) != -1) {
                if(byteRead<0) array[256]++;
                else array[byteRead]++;
            }
            for(int i=0;i<257;i++){
                System.out.printf("index %d:data %d \n",i,array[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????Box-Muler?????????????????????????????????????????????
     * ???????????????????????????????????????????????????..???????????????
     */
    public void testPartition2(){
        InputStream fileIn = null;
        long[] array=new long[10];
        try {
            fileIn = new FileInputStream("e://ubuntu-18.04.1-desktop-amd64.iso");
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            int byteReadOld,byteReadNew;
            if((byteReadOld = bufferIn.read()) != -1){
                while ((byteReadNew = bufferIn.read()) != -1) {
                    double a=byteReadOld*1.0/255;
                    double b=byteReadNew*1.0/255;
                    double normalDistribution = Math.sqrt(-2*Math.log(a))*Math.cos(2*Math.PI*b);
                    if(normalDistribution<-3.0){
                        array[0]++;
                    }else if(normalDistribution<=0.0){
                        array[1]++;
                    }else if(normalDistribution<=3.0){
                        array[2]++;
                    }else{
                        array[3]++;
                    }
                    byteReadOld=byteReadNew;
                }
            }
            for(int i=0;i<10;i++){
                System.out.printf("index %d:data %d \n",i,array[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     */
    public void testPartition3(){
        InputStream fileIn = null;
        long[] array=new long[10];
        try {
            fileIn = new FileInputStream("e://ubuntu-18.04.1-desktop-amd64.iso");
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            int byteReadOld,byteReadNew;
            int blockSize=0;
            if((byteReadOld = bufferIn.read()) != -1){
                while ((byteReadNew = bufferIn.read()) != -1) {
                    double a=byteReadOld*1.0/255;
                    double b=byteReadNew*1.0/255;
                    blockSize++;
                    double normalDistribution = Math.sqrt(-2*Math.log(a))*Math.cos(2*Math.PI*b);
                    if(normalDistribution<=-3.0||normalDistribution>=3.0){
                        if(blockSize<=12){
                            array[0]++;
                        }else if(blockSize<=2370){
                            array[1]++;
                        }else{
                            array[2]++;
                        }
                        blockSize=0;
                    }
                    byteReadOld=byteReadNew;
                }
            }
            for(int i=0;i<10;i++){
                System.out.printf("index %d:data %d \n",i,array[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testMOd(){
        for(int i=1;i<9999;i++){
            System.out.printf("??????????????????i:%d\n",i);
            for(int j=1;j<9999;j++){
                for(int c=1;c<9999;c++){
                    int m=(i*j)%c;
                    int n=((i%c)*j)%c;
                    if(m!=n) System.out.printf("i:%d,j:%d,c:%d\n",i,j,c);
                }
            }
        }
        System.out.println("done!");
    }

    public void testcos(){
        long startTime,endTime;
        startTime=currentTimeMillis();
        for(int j=0;j<999999;j++){
            double a = Math.cos(1);
        }
        endTime=currentTimeMillis();
        System.out.printf("time for cos 999999 times:%d ms\n",(endTime-startTime));
        startTime=currentTimeMillis();
        for(int j=0;j<999999;j++){
            double a = 1+1;
        }
        endTime=currentTimeMillis();
        System.out.printf("time for addition 999999 times:%d ms\n",(endTime-startTime));

        startTime=currentTimeMillis();
        for(int j=0;j<999999;j++){
            double a = 9999%23;
        }
        endTime=currentTimeMillis();
        System.out.printf("time for modular 999999 times:%d ms\n",(endTime-startTime));
    }

    public void geneFile(){
        MersenneTwister randomMT=new MersenneTwister();
        try {
            for(int j=0;j<10;j++){
                FileOutputStream fileOut = new FileOutputStream("e://test"+j+".txt");
                BufferedOutputStream dataOut=new BufferedOutputStream(fileOut);
                for(long i=0;i<1610612736L;i++){
                    //1.5GB
                    int b=(int)randomMT.randInt(255L);
                    //System.out.printf("%d:%d\n",i,b);
                    dataOut.write(b);
                }
                dataOut.flush();
                dataOut.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ChangeFileSeries(){
        for(int i=0;i<10;i++){
            ChangeFile(i);
        }
    }

    /**
     * ????????????????????????
     */
    public void ChangeFile(int num){
        String ff="e://test"+num;
        String fileName=ff+".txt";
        String fileNameInsert=ff+"_Insert.txt";
        String fileNameDelete=ff+"_Delete.txt";
        String fileNameAdd=ff+"_Add.txt";

        InputStream fileIn = null;
        FileOutputStream fileOutInsert =null;
        FileOutputStream fileOutAdd =null;
        FileOutputStream fileOutDelete =null;
        MersenneTwister randomMT=new MersenneTwister();
        //int b=(int)randomMT.randInt(10000L); //????????????????????????????????????????????????????????????????????????
        int b=10000;//???10000?????????????????????????????????????????????
        int index=0;//??????????????????????????????
        int deleteNum=0;
        try {
            fileIn = new FileInputStream(fileName);
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            //??????
            fileOutInsert  = new FileOutputStream(fileNameInsert);
            BufferedOutputStream dataOutInsert=new BufferedOutputStream(fileOutInsert);
            //??????
            fileOutAdd  = new FileOutputStream(fileNameAdd);
            BufferedOutputStream dataOutAdd=new BufferedOutputStream(fileOutAdd);
            //??????
            fileOutDelete  = new FileOutputStream(fileNameDelete);
            BufferedOutputStream dataOutDelete=new BufferedOutputStream(fileOutDelete);

            int byteRead;
            while((byteRead = bufferIn.read()) != -1){
                index++;//????????????
                //????????????
                dataOutAdd.write(byteRead);
                //????????????
                dataOutInsert.write(byteRead);
                if(index%10000==0){//???10000??????????????????100?????????
                    for(int i=0;i<100;i++){
                        int by=(int)randomMT.randInt(255);
                        //by=255;
                        dataOutInsert.write(by);
                    }
                    //????????????10000??????????????????100?????????
                    deleteNum=100;
                }
                //????????????
                if(deleteNum==0){
                    dataOutDelete.write(byteRead);
                }else{
                    deleteNum--;
                }
            }
            //????????????
            for(int i=0;i<20000;i++){
                //??????20000?????????
                dataOutAdd.write(255);
            }
            dataOutInsert.flush();
            dataOutInsert.close();
            dataOutDelete.flush();
            dataOutDelete.close();
            dataOutAdd.flush();
            dataOutAdd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void TestLongPrintf(){
        Long long_int = new Long(100000000000L);
        System.out.printf("%ld\n",long_int.longValue());
        System.out.printf("%d\n",long_int.longValue());

        System.out.printf("%ld\n",long_int.intValue());
        System.out.printf("%d\n",long_int.intValue());
    }
    //?????????????????????10???all?????????????????????????????????????????????????????????????????????
    public void testCommonData(){
        String testFile="e://all9.txt";
        String aimFile="e://all9_Delete.txt";
        int numOfCommon=0;
        try {
            InputStream fileIn = new FileInputStream(testFile);
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);

            InputStream fileIn1 = new FileInputStream(aimFile);
            BufferedInputStream bufferIn_aim = new BufferedInputStream(fileIn1);
            int byteRead=0;
            int byteRead_aim=0;

            byteRead=bufferIn.read();
            while ((byteRead_aim=bufferIn_aim.read())!=-1){
                if(byteRead==byteRead_aim){
                    numOfCommon++;
                    byteRead=bufferIn.read();
                    if(byteRead==-1){
                        break;
                    }
                }
            }

            bufferIn.close();
            fileIn.close();
            bufferIn_aim.close();
            fileIn1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(numOfCommon);
        return;
    }

    //????????????????????????
    public void fileModifySerious(){
        List<String> fileList = new ArrayList<String>();
        fileList.add("2kb");
        fileList.add("4kb");
        fileList.add("8kb");
        fileList.add("16kb");
        fileList.add("32kb");
        fileList.add("64kb");
        fileList.add("128kb");
        for(String it:fileList){
            fileModify(it);
        }
        return;
    }

    //????????????????????????????????????????????????modified??????????????????????????????????????????????????????????????????
    public void fileModify(String filename){
        //StringBuilder strList = new StringBuilder("");
        String filePrefix = "e://";
        String fileName=filePrefix+filename+".txt";
        String fileNameInsert=filePrefix+filename+"_Insert.txt";
        String fileNameDelete=filePrefix+filename+"_Delete.txt";
        String fileNameAdd=filePrefix+filename+"_Add.txt";

        InputStream fileIn = null;
        FileOutputStream fileOutInsert =null;
        FileOutputStream fileOutAdd =null;
        FileOutputStream fileOutDelete =null;
        MersenneTwister randomMT=new MersenneTwister();
        //int b=(int)randomMT.randInt(10000L); //????????????????????????????????????????????????????????????????????????
        int b=10000;//???10000?????????????????????????????????????????????
        int index=0;//??????????????????????????????
        int deleteSize=0,addSize=0,insertSize=0;//??????????????????????????????????????????????????????????????????
        int deleteNum=0;
        try {
            fileIn = new FileInputStream(fileName);
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            //??????
            fileOutInsert  = new FileOutputStream(fileNameInsert);
            BufferedOutputStream dataOutInsert=new BufferedOutputStream(fileOutInsert);
            //??????
            fileOutAdd  = new FileOutputStream(fileNameAdd);
            BufferedOutputStream dataOutAdd=new BufferedOutputStream(fileOutAdd);
            //??????
            fileOutDelete  = new FileOutputStream(fileNameDelete);
            BufferedOutputStream dataOutDelete=new BufferedOutputStream(fileOutDelete);

            int byteRead;
            while((byteRead = bufferIn.read()) != -1){
                //strList.append(HexStringTable[byteRead]);
                index++;//????????????
                //????????????
                dataOutAdd.write(byteRead);
                //????????????
                dataOutInsert.write(byteRead);
//                if(index%1000==0){
//                    //??????????????????????????????
//                    System.out.println(strList.toString());
//                    strList.delete(0,strList.length());
//                }
                if(index%150000==0){//???30000??????????????????1000?????????
                    for(int i=0;i<5000;i++){
                        int by=(int)randomMT.randInt(255);
                        by=0;
                        dataOutInsert.write(by);
                        insertSize++;
                    }
                    //????????????30000??????????????????1000?????????
                    deleteNum=1000;
                }
                //????????????
                if(deleteNum==0){
                    dataOutDelete.write(byteRead);
                }else{
                    deleteNum--;
                    deleteSize++;
                }
            }
            //????????????
            for(int i=0;i<20000;i++){
                //??????20000?????????
                dataOutAdd.write((int)randomMT.randInt(255));
                addSize++;
            }
            dataOutInsert.flush();
            dataOutInsert.close();
            dataOutDelete.flush();
            dataOutDelete.close();
            dataOutAdd.flush();
            dataOutAdd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(fileName+"--  addSize:"+addSize+"; deleteSize:"+deleteSize+"; insertSize:"+insertSize);

        return;
    }
}
