package com.chunking.rabinHash;

/**
 * Created by Administrator on 2019/3/12 0012.
 */
public class TestRabin {
    public static void main(String[] args) {
        long ret=0;
        KarpRabinHash handleOfHash;
        {
            handleOfHash=new KarpRabinHash(5,10);
        }
        int[] test={111,222,123,224,135};
        ret=handleOfHash.eat(111);
        ret=handleOfHash.eat(222);
        ret=handleOfHash.eat(123);
        ret=handleOfHash.eat(224);
        ret=handleOfHash.eat(135);
        System.out.printf("first time: %d\n",ret);
        handleOfHash.reset();
        ret=handleOfHash.hash(test);
        System.out.printf("second time: %d\n",ret);
//        long test=-9223372036L;
//        test &= 0xffffffffL;
//        test = 9223372036L;
//        test&=0xffffffffL;
//        test=test+1;
        System.exit(0);
    }
}
