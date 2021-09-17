package com.chunking.commonInf;

/**
 * Administrator on 2018.11.15 19:44.
 */
public class StringAndByte {
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    /**
     * 两位16进制字符串得到对应的byte
     * @param hexString
     * @return
     */
    public byte twoHex2OneByte(String hexString){
        int length = hexString.length();
        if(length!=2){
            //TODO
        }
        char[] ca = hexString.toUpperCase().toCharArray();
        if(charToByte(ca[0])==-1||charToByte(ca[1])==-1){
            //TODO
        }
        return (byte)(charToByte(ca[0])<<4|charToByte(ca[1]));
    }
    /**
     * 长串16进制字符串转换成对应的byte[]
     * @param hexString
     * @return
     */
    public final byte[] hexString2Bytes(String hexString) {
        if ((hexString == null) || (hexString.equals(""))){
            return null;
        }
        else if (hexString.length()%2 != 0){
            return null;
        }
        else{
            hexString = hexString.toUpperCase();
            int len = hexString.length()/2;
            byte[] b = new byte[len];
            char[] hc = hexString.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1]));
            }
            return b;
        }
    }
    /**
     * 内部函数，取一个hexString中单个字节的最大值
     * @param hexString
     * @return
     */
    public final int maxByteInHexString(String hexString){
        int len = hexString.length();
        int max=-1;
        if(len<0||len%2!=0) return -1;
        for(int i=0;i<len/2;i++){
            int temp = Integer.parseInt(hexString.substring(2*i,2*i+2),16);
            if(max<temp) max=temp;
        }
        return max;
    }

    /**
     * 取一段hexString的区间和的最大值
     * @param hexString 字符串16进制
     * @param sizeOfSum 区间大小
     * @return
     */
    public final int maxSumInHexString(String hexString,int sizeOfSum){
        int len=hexString.length();
        if(len<sizeOfSum*2) return -1;
        int maxSum=0,curSum=0;//最大区间的和；当前区间的和
        for(int i=0;i<sizeOfSum;i++){
            maxSum+=Integer.parseInt(hexString.substring(i*2,i*2+2),16);
            curSum=maxSum;
        }
        for(int i=sizeOfSum*2;i<len;i=i+2){
            int newByte=Integer.parseInt(hexString.substring(i,i+2),16);
            int oldByte=Integer.parseInt(hexString.substring(i-sizeOfSum*2,i-sizeOfSum*2+2),16);
            curSum=curSum+newByte-oldByte;
            maxSum=curSum>maxSum?curSum:maxSum;
        }
        return maxSum;
    }

    /**
     * 取一段hexString的区间和的最小值
     * @param hexString 字符串16进制
     * @param sizeOfSum 区间大小
     * @return
     */
    public final int minSumInHexString(String hexString,int sizeOfSum){
        int len=hexString.length();
        if(len<sizeOfSum*2) return -1;
        int minSum=0,curSum=0;//最大区间的和；当前区间的和
        for(int i=0;i<sizeOfSum;i++){
            minSum+=Integer.parseInt(hexString.substring(i*2,i*2+2),16);
            curSum=minSum;
        }
        for(int i=sizeOfSum*2;i<len;i=i+2){
            int newByte=Integer.parseInt(hexString.substring(i,i+2),16);
            int oldByte=Integer.parseInt(hexString.substring(i-sizeOfSum*2,i-sizeOfSum*2+2),16);
            curSum=curSum+newByte-oldByte;
            minSum=curSum<minSum?curSum:minSum;
        }
        return minSum;
    }
}
