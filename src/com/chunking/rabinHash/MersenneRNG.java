package com.chunking.rabinHash;

/**
 * Created by Administrator on 2019/3/12 0012.
 */
public class MersenneRNG {
    private MersenneTwister mtr;
    private long n;
    MersenneRNG(){
        mtr=new MersenneTwister();
        seed();
    }
    MersenneRNG(long maxval){
        mtr=new MersenneTwister();
        seed();
        n=maxval;
    }
    void setMaxval(long maxval){
        n=maxval;
    }
    long getRandInt(){
        return mtr.randInt(n);
    }
    void seed(long seedval) {
        mtr.seed(seedval);
    }
    void seed() {
        //mtr.seed(714);
    }
    long rand_max() {
        return n;
    }
    long maskfnc(int bits){
        if(bits<=0) return -1;
        long x=1<<(bits-1);
        return x^(x-1);
    }
}
