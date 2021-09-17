package com.chunking.commonInf;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Administrator on 2018.11.15 22:04.
 */
public class AbstractOfString {
    /**
     * 获取Mark Adler的adler-32校验，此校验是按字节滚动校验，也即正常的参数应该是一个Byte[].
     * 此处将参数设为Byte[]中字节按照16进制转换得到的字符串，即每两位hexString是一个字节
     * @param hexString 需要取校验的任意长度HexString
     * @return 8位HexString
     */
    public String getAdler32(String hexString){
        int s1=0,s2=0,intRes;
        int length = hexString.length();
        if (length % 2 != 0) { //如果hexString长度不是偶数
            return "";
        }
        length=length/2;
        for(int i=0;i<length;i++){
            String subStr=hexString.substring(2*i,2*i+2);
            StringAndByte hex2ByteHandle = new StringAndByte();
            s1+=(int)hex2ByteHandle.twoHex2OneByte(subStr);
            s2+=s1;
        }
        intRes=(s2&0xffff)<<16|(s1&0xffff);
        return String.format("%08x",intRes);//format("%08x",intRes)
    }

    /**
     * 求取rolling checknum，通过上一个窗口的adler32值，减去第一个字节，增加一个新的字节后的新adler32的值
     * @param rollCheck 原adler32值
     * @param len  需要求取adler32值得字符串长度
     * @param hexCut 截取掉的第一个字节的hex字符串形式
     * @param hexNext 增加新的字节的hex字符串形式
     * @return 新adler32值 hex字符串格式 8位
     */
    public String getNextAdler32(String rollCheck,int len,String hexCut,String hexNext){
        int s1=0,s2=0,intRes=0;
        s1=Integer.parseInt(rollCheck.substring(4),16);
        s2=Integer.parseInt(rollCheck.substring(0,4),16);
        s1=s1-Integer.parseInt(hexCut,16)+Integer.parseInt(hexNext,16);
        s2=s2-len*Integer.parseInt(hexCut,16)+s1;
        intRes=(s2&0xffff)<<16|(s1&0xffff);
        return String.format("%08x",intRes);
    }

    /**
     * 获取Rabin校验值，原理是计算窗口长度的二进制字符串的值，除以预定的值（基数），然后得到的余数即为结果
     * 此次代码实现采取计算转换后的整数值，除以基数，得到余数。Rabin的精髓在于可以滚动计算
     * 此处将参数是分块中所有字节按照16进制转换得到的字符串，即每两位hexString是一个字节
     * @param hexString
     * @param valueOfDividor 除数
     * @param sizeOfWindow 窗口大小，注：
     * @return
     */
    public int getRabin(String hexString,int valueOfDividor,int sizeOfWindow){
        int len = hexString.length();
        if(len<sizeOfWindow*2) return -1;
        String hexStringOfWin = hexString.substring(len-sizeOfWindow*2);
        int len_win=hexStringOfWin.length();
        int valueOfRes=0;
        for(int i=0;i<len_win;i=i+2){
            valueOfRes=valueOfRes*256+Integer.parseInt(hexStringOfWin.substring(i,i+2),16);
            valueOfRes=valueOfRes%valueOfDividor;
        }
        return valueOfRes;
    }

    /**
     * 求取rolling valueOfRabin，通过上一个窗口的rabin值，减去第一个字节，增加一个新的字节后的新rabin的值
     * 注：
     * @param valueOfRabin 原rabin值
     * @param len  需要求取rabin值得字符串长度
     * @param hexCut 截取掉的第一个字节的hex字符串形式
     * @param hexNext 增加新的字节的hex字符串形式
     * @param dividor 余数
     * @return 新adler32值 hex字符串格式 8位
     */
    public int getNextRabin(int valueOfRabin,int len,String hexCut,String hexNext,int dividor){
        int res=0;
        res=valueOfRabin-getResOfBignumber(Integer.parseInt(hexCut,16),256,len,dividor)+Integer.parseInt(hexNext,16);
        return res%dividor;
    }

    /**
     * 计算 (value*pow^radix)%dividor
     * @param value
     * @param radix
     * @param pow
     * @param dividor
     * @return
     */
    private int getResOfBignumber(int value,int radix,int pow,int dividor){
        int res=value%dividor;
        for(int i=0;i<pow;i++){
            res=(res*radix)%dividor;
        }
        return res;
    }

    /**
     * 获取指定字符串的MD5编码
     * @param stringSrc 指定字符串
     * @return MD5编码
     * @throws NoSuchAlgorithmException
     */
    public String getMD5(String stringSrc){
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[] secretBytes=md.digest(stringSrc.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : secretBytes) {
                stringBuilder.append(String.format("%02x", b & 0xff));
            }
            String secretString=stringBuilder.toString();
            return secretString;
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
