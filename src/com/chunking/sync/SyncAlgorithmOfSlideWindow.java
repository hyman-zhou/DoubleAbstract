package com.chunking.sync;

import com.chunking.dataStore.DataStoreJson;
import com.chunking.commonInf.StringAndByte;
import com.chunking.commonInf.AbstractOfString;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.currentTimeMillis;

/**
 * Administrator on 2018.11.13 21:21.
 */
public class SyncAlgorithmOfSlideWindow {
    private String fileSrc;  //源文件名称，用于提供最新数据的文件  同步方向为：源文件->备份文件
    private String fileBackup;  //备份文件名称，用于更新至最新数据的文件
    private String fileNew;  //新文件名称，用于暂时存放更新后的备份数据，此数据同步后将与源文件一致
    private DataStoreJson dataStoreJson;
    private int cms; //最大分块大小
    private int blockSize; //固定分块大小
    {
        fileSrc = "e://11.docx";
        fileBackup = "e://2.docx";
        fileNew = "e://13.docx";
        dataStoreJson =new DataStoreJson();
        cms=200;
        blockSize=100;
    }
    public SyncAlgorithmOfSlideWindow(){}
    private void setSrcFileName(String fileSrc){
        this.fileSrc = fileSrc;
    }
    private String getSrcFileName(){
        return this.fileSrc;
    }
    private void setBackupFileName(String fileBackup){
        this.fileBackup = fileBackup;
    }
    private String getBackupFileName(){
        return this.fileBackup;
    }
    private void setNewFileName(String fileNew){
        this.fileNew = fileNew;
    }
    private String getNewFileName(){
        return this.fileNew;
    }

    /**
     * 按照默认参数运行滑动窗口同步算法
     */
    public final void SyncAlgorithm_sliwin(){
        JSONObject abstractOfFileBackup = getAbstractOfFileBackup();
        JSONObject abstractOfDiff = getDiffWithSlideWin(abstractOfFileBackup);
        generateFileThroughDiff(abstractOfDiff);
    }
    /**
     * 滑动窗口实现文件间同步
     * @param fileSrc 源文件名
     * @param fileBackup 备份文件名
     * @param fileNew 新文件名，如果与fileBackup名字相同，则为同步
     */
    public final void SyncAlgorithm_sliwin(String fileSrc,String fileBackup,String fileNew){
        setSrcFileName(fileSrc);
        setBackupFileName(fileBackup);
        setNewFileName(fileNew);
        JSONObject abstractOfFileBackup = getAbstractOfFileBackup();
        JSONObject abstractOfDiff = getDiffWithSlideWin(abstractOfFileBackup);
        generateFileThroughDiff(abstractOfDiff);
    }

    /**
     * 滑动窗口实现文件间同步
     * @param fileSrc
     * @param fileBackup
     * @param fileNew
     * @param blockSize
     * @param cms
     */
    public final void SyncAlgorithm_sliwin(String fileSrc,String fileBackup,String fileNew,int blockSize,int cms){
        this.cms=cms;
        this.blockSize=blockSize;
        setSrcFileName(fileSrc);
        setBackupFileName(fileBackup);
        setNewFileName(fileNew);
        JSONObject abstractOfFileBackup = getAbstractOfFileBackup();
        JSONObject abstractOfDiff = getDiffWithSlideWin(abstractOfFileBackup);
        generateFileThroughDiff(abstractOfDiff);
    }
    /**
     * 此处为备份数据的滑动窗口中的固定分块过程
     * @return json数据，格式为 {littleAbstract1:{bigAbstract1:blockIndex1,bigAbstract2:blockIndex2,...}
     *                        ,littleAbstract2:{bigAbstract1:blockIndex1,bigAbstract2:blockIndex2,...}
     *                        ...
     *                        }
     *其中littleAbstract为快速摘要，碰撞率较大。以littleAbstract为key，碰撞部分作为json的value。碰撞部分则以
     * bigAbstract：blockIndex的json对表示，前者采用MD5取到的区块fingerprint，后者为当前块的下标
     */
    private JSONObject getAbstractOfFileBackup(){
        try {
            InputStream fileIn = new FileInputStream(getBackupFileName());
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            return getAbstractJsonFromInputBuffer(bufferIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过buffer获取分块的摘要，其中分块的边界判定可能会采取多种方式
     * @param in
     * @return
     * @throws IOException
     */
    private JSONObject getAbstractJsonFromInputBuffer(BufferedInputStream in) throws IOException {
        long startTime,endTime;
        startTime=currentTimeMillis();
        JSONObject jsonResult = new JSONObject();
        int byteRead;
        int blockIndex=0;
        String blockString="";
        while ((byteRead = in.read()) != -1) {
            String intTo16String = String.format("%02x",byteRead);
            blockString+=intTo16String;
            if (blockString.length()==blockSize){  //固定分块，blockSize个字节；摘要算法采取MD5，消息格式采取 {"摘要":{"分块下标":index,"完整数据":data},...}
                JSONObject tempJson=new JSONObject();
                AbstractOfString absOS=new AbstractOfString();
                String abs_adler32=absOS.getAdler32(blockString);
                String abs_MD5=absOS.getMD5(blockString);
                if (jsonResult.has(abs_adler32)){
                    tempJson=jsonResult.getJSONObject(abs_adler32);
                }
                //将下标和数据对应关系存储起来
                dataStoreJson.addIndex2Src(String.valueOf(blockIndex),blockString);
                //摘要json数据
                tempJson.put(abs_MD5,String.valueOf(blockIndex));
                jsonResult.put(abs_adler32,tempJson);
                blockString="";
                blockIndex++;
            }
        }
        endTime = currentTimeMillis();
        System.out.printf("Time to get abstract: %d ms.\n",endTime-startTime);
        return jsonResult;
    }

    /**
     * 滑动窗口的方式，通过备份数据的摘要，以及源文件比较得出差异数据的json数据
     * @param jsonOfAbstract 备份数据的摘要json字段，此处为两级摘要
     * @return 差异json数据
     */
    private JSONObject getDiffWithSlideWin(JSONObject jsonOfAbstract){
        long startTime,endTime;
        startTime=currentTimeMillis();
        JSONArray jsonArrayOfDiff = new JSONArray();
        try {
            InputStream fileIn = new FileInputStream(getSrcFileName());
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            int byteRead;//按字节读取
            int countOfWindow=0;//窗口大小计数
            int countIndex=0;//差异数据块下标
            String stringOfWindow="";//窗口数据
            String stringOfDiff="";//差异数据块
            String abs_adler32="";
            String abs_AbandonTwo="";
            while ((countOfWindow<=blockSize-2)&&(byteRead = bufferIn.read()) != -1){//预先读取blockSize-1个字节数据
                String intTo16String = String.format("%02x",byteRead);
                stringOfWindow+=intTo16String;
                countOfWindow++;
            }
            while ((byteRead = bufferIn.read()) != -1){//读入一个字节使字节长度达到blockSize，然后进行判断是否命中
                String intTo16String = String.format("%02x",byteRead);  //将读入的int转换成16进制字符串
                stringOfWindow+=intTo16String;//将字符串添加入窗口
                AbstractOfString absOS=new AbstractOfString();
                if(abs_AbandonTwo.equals("")){ //首次生成adler32校验，则需要完整计算
                    abs_adler32=absOS.getAdler32(stringOfWindow);
                }else {//非首次生成，则采取滚动校验
                    abs_adler32=absOS.getNextAdler32(abs_adler32,blockSize,abs_AbandonTwo,intTo16String);
                }
                if(jsonOfAbstract.has(abs_adler32)){//如果命中，即该窗口数据有可能包含在摘要json数据中
                    String abs_MD5=absOS.getMD5(stringOfWindow);
                    JSONObject valueOfJson = jsonOfAbstract.getJSONObject(abs_adler32);
                    if(valueOfJson.has(abs_MD5)){//强校验也命中，认为区块数据确实包含在json数据中
                        if(!stringOfDiff.equals("")){//如果差异数据非空，需先将差异数据填入差异json数据中
                            JSONObject jsonResult = new JSONObject();
                            jsonResult.put("index",String.format("%d",countIndex++));
                            jsonResult.put("flag","1");
                            jsonResult.put("DataOrDataIndex",stringOfDiff);
                            jsonArrayOfDiff.put(jsonResult);
                            stringOfDiff="";
                        }
                        //将命中的数据放入差异json数据中
                        JSONObject jsonResult = new JSONObject();
                        jsonResult.put("index",String.format("%d",countIndex++));
                        jsonResult.put("flag","0");
                        jsonResult.put("DataOrDataIndex",String.valueOf(valueOfJson.getInt(abs_MD5)));
                        jsonArrayOfDiff.put(jsonResult);
                        stringOfWindow="";
                    }
                    countOfWindow=0;
                    while ((countOfWindow<=blockSize-2)&&(byteRead = bufferIn.read()) != -1){//预先读取blockSize-1个字节数据
                            String int2HexString = String.format("%02x",byteRead);
                            stringOfWindow+=int2HexString;
                            countOfWindow++;
                            abs_adler32="";  //此时需要情况之前的adler32校验
                            abs_AbandonTwo="";//同时将丢弃的一个字节，两位hex字符串赋值为空
                    }
                }else{//没有命中，则移动窗口，按字节移动
                    stringOfDiff+=stringOfWindow.substring(0,2);
                    abs_AbandonTwo=stringOfWindow.substring(0,2);
                    stringOfWindow=stringOfWindow.substring(2);
                }
                if(stringOfDiff.length()==cms){//如果达到最大分块长度都没有命中，则将差异数据加入到差异json数据中，此时窗口中数据不变
                    JSONObject jsonResult = new JSONObject();
                    jsonResult.put("index",String.format("%d",countIndex++));
                    jsonResult.put("flag","1");
                    jsonResult.put("DataOrDataIndex",stringOfDiff);
                    jsonArrayOfDiff.put(jsonResult);
                    stringOfDiff="";
                }
            }//此处while循环结束，即全部遍历完src二进制文件
            if(!stringOfDiff.equals("")){//1.遍历完之后窗口之前的差异数据要先加入差异json数据中
                JSONObject jsonResult = new JSONObject();
                jsonResult.put("index",String.format("%d",countIndex++));
                jsonResult.put("flag","1");
                jsonResult.put("DataOrDataIndex",stringOfDiff);
                jsonArrayOfDiff.put(jsonResult);
                stringOfDiff="";
            }
            if(!stringOfWindow.equals("")){//1.可能整个src文件不足一个blockSize 2.遍历完之后窗口中的数据，大小为blockSize
                JSONObject jsonResult = new JSONObject();
                jsonResult.put("index",String.format("%d",countIndex));
                jsonResult.put("flag","1");
                jsonResult.put("DataOrDataIndex",stringOfWindow);
                jsonArrayOfDiff.put(jsonResult);
                stringOfWindow="";
            }
            bufferIn.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonOfResult = new JSONObject();
        jsonOfResult.put("data",jsonArrayOfDiff);
        endTime = currentTimeMillis();
        System.out.printf("Time to get diffJson: %d ms.\n",endTime-startTime);
        return jsonOfResult;
    }

    /**
     * 按照差异json数据，以及备份数据的index2Data中的数据，拼接成新的文件
     * @param jsonOfBlockDiff  差异json数据
     * @return 0成功 1失败
     */
    private int generateFileThroughDiff(JSONObject jsonOfBlockDiff){
        long startTime,endTime;
        startTime=currentTimeMillis();
        JSONArray jsonArrayOfDiff = jsonOfBlockDiff.getJSONArray("data");
        StringAndByte strAndByte = new StringAndByte();
        try {
            FileOutputStream fileOut = new FileOutputStream(getNewFileName());
            BufferedOutputStream dataOut=new BufferedOutputStream(fileOut);
            for(int i=0;i<jsonArrayOfDiff.length();i++){
                JSONObject jsonTemp = jsonArrayOfDiff.getJSONObject(i);
                if(jsonTemp.getString("flag").equals("0")){
                    String dataSame= dataStoreJson.getIndex2Src().getString(jsonTemp.getString("DataOrDataIndex"));
                    dataOut.write(strAndByte.hexString2Bytes(dataSame),0,dataSame.length()/2);
                }
                if(jsonTemp.getString("flag").equals("1")){
                    String dataSame=jsonTemp.getString("DataOrDataIndex");
                    dataOut.write(strAndByte.hexString2Bytes(dataSame),0,dataSame.length()/2);
                }
            }
            dataOut.flush();
            dataOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        endTime = currentTimeMillis();
        System.out.printf("Time to generate new file: %d ms.\n",endTime-startTime);
        return 0;
    }
}
