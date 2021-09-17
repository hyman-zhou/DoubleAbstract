package com.chunking.deBlock;


import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;

public class DeBlockAlgorithmOfRBMC {

    private String fileName;
    private ClassOfParameters para;
    private String valueOfDest;
    {
        para=new ClassOfParameters();
        fileName=para.fileName;
        valueOfDest = "sdjfgfdlkxcvueriowiqweopgfdgwejflaksd";
    }

    //预处理 快速指纹生成算法
    // BitSet即为数据指纹
    public static BitSet patternPre(String pattern){
        BitSet fp = new BitSet(pattern.length());
        char[] chars = pattern.toCharArray();
        int bIdx = 0;
        for(int i=0;i<chars.length;i=i+4){
//            System.out.println(Integer.toBinaryString(chars[i]));
            boolean bit = (chars[i] & 0x1) == 1 ? true : false;
            fp.set(bIdx,bit);
            bIdx++;
        }
//        System.out.println(fp.toString());
        return fp;
    }

    public static BitSet bytePre(byte[] bytes){
        int len = bytes.length;
        BitSet bitSet = new BitSet();
        int i = 0;
        do{
            boolean bit = (bytes[i] & 0x1) == 1 ? true : false;
            bitSet.set(i,bit);
            i++;
        }while (i<len);
        return bitSet;
    }

    //预处理 文件预处理  提取文件的最后一个字节 构成一个位图
    public static BitSet filePre(String fileName) {
        long startTime = System.currentTimeMillis();
        BitSet bitStr = new BitSet();
        try{
            InputStream fileIn = new FileInputStream(fileName);
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            DataInputStream din = new DataInputStream(bufferIn);
            int bitIdx = 0; //可能会溢出，文件过大
            do{
                int intRead;
                try{
                    intRead = din.readInt();
                    boolean bit = (intRead & 0x1) == 1 ? true : false;
                    bitStr.set(bitIdx, bit);
                    bitIdx++;
                }catch (EOFException e){
                    System.out.println("reached the end!");
//                    System.out.println(bitIdx);  402653184
                    break;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }while (true);
        }catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗时："+(endTime - startTime)); // 8487
        return bitStr;
    }

    public static void fileSize(String filename){
        File file = new File(filename);
        System.out.println(file.length()/4);
    }

    public void deBlockAlgorithmOfRBMC(){
        deBlock();

    }

    private JSONObject deBlock() {
        final long startTime = System.currentTimeMillis();
        BitSet mainStr = new BitSet();
        try {
            InputStream fileIn = new FileInputStream(fileName);
            BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
            byte[] bytes = new byte[4096];
            int rSize = 0;
            while ((rSize = bufferIn.read(bytes)) > 0){
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //BitSet mainStr = filePre(fileName);
        System.out.printf("主串长度：%d\n",mainStr.length());
        BitSet pattern = patternPre(valueOfDest);
        ArrayList<Integer> chunkSize = new ArrayList<>(1577247);
        int count = 0;
        int window = pattern.length();
        for(int i=0;i<mainStr.length()-window;i++){
            BitSet mainSet = mainStr.get(i, i + window);
            mainSet.xor(pattern);
            if(mainSet.isEmpty()) {
                if(chunkSize.isEmpty()) {
                    chunkSize.add(i);
                }else {
                    chunkSize.add(i-chunkSize.get(count-1));
                }
                count++;
            }
        }
        JSONObject jsonResult = new JSONObject();
        System.out.printf("chunk个数:%d\n",count);
        long endTime = System.currentTimeMillis();
        System.out.printf("分块总耗时：%d\n",endTime-startTime);

        return jsonResult;
    }


    public static void main(String[] args) {
//        String pattern = "sdjfgfdlkxcvueriowiqweopgfdgwer";
//        System.out.println(pattern.length()/4);
//        BitSet fp = patternPre(pattern);
//        System.out.println(fp.toString());
//        String fileName="e://test0.txt";
//        BitSet set = filePre(fileName);
        //System.out.println(set.toString());
        DeBlockAlgorithmOfRBMC deBlockAlgorithmOfRBMC = new DeBlockAlgorithmOfRBMC();
        deBlockAlgorithmOfRBMC.deBlockAlgorithmOfRBMC();
    }
}
