package com.chunking.rabinHash;

/**
 * Created by Administrator on 2019/3/12 0012.
 */

/**
 * @article{matsumoto1998mtd,
 *title={{Mersenne Twister: A 623-Dimensionally Equidistributed Uniform Pseudo-Random Number Generator}},
 *author={MATSUMOTO, M. and NISHIMURA, T.},
 *journal={ACM Transactions on Modeling and Computer Simulation},
 *volume={8},
 *number={1},
 *pages={3-30},
 *year={1998}
 *}
 */
public class MersenneTwister {
    private int N; //length of state vector
    private int SAVE;//length of array for save()
    private int M; //period parameter
    private long state[];//internal state
    private int pIndex;//next value to get from state,  C++版为指针，此处用下标代替
    private int left;//number of values left before reload needed
    {
        N=624;
        SAVE=N+1;
        M=397;
        state = new long[N];
    }

    public MersenneTwister(){
        seed(714);
    }
    public MersenneTwister(long oneSeed){
        seed(oneSeed);
    }
    void seed(long oneSeed){
        //Seed the generator with a simple uint32
        initialize(oneSeed);
        reload();
    }
    void initialize( long seed )
    {
        // Initialize generator state with seed
        // See Knuth TAOCP Vol 2, 3rd Ed, p.106 for multiplier.
        // In previous versions, most significant bits (MSBs) of the seed affect
        // only MSBs of the state array.  Modified 9 Jan 2002 by Makoto Matsumoto.
        state[0]=seed&0xffffffffL;
        for(int i=1;i<N;i++){
            state[i]=(1812433253L*(state[i-1]^(state[i-1]>>30))+i)&0xffffffffL;
        }
    }
    void reload()
    {
        // Generate N new values in state
        // Made clearer and faster by Matthew Bellew (matthew.bellew@home.com)
        int i;
        int j=0;
        for(i=N-M;i>0;++j,--i){
            state[j]=twist(state[M+j],state[j],state[j+1]);
        }
        for(i=M;i>1;++j,--i){
            state[j]=twist(state[M-N+j],state[j],state[j+1]);
        }
        state[j]=twist(state[M-N+j],state[j],state[0]);
        left=N;
        pIndex=0;
    }
    long twist( long m, long s0, long s1 )
    {
        long ret=m ^ (mixBits(s0,s1)>>1) ^ (-(loBit(s1)) & 0x9908b0dfL);
        ret&=0xffffffffL;
        return ret;
    }
    long mixBits( long u, long v )
    {
        return hiBit(u) | loBits(v);
    }
    long hiBit( long u ){
        return u & 0x80000000L;
    }
    long loBit( long u ){
        return u & 0x00000001L;
    }
    long loBits( long u ){
        return u & 0x7fffffffL;
    }
    long randInt()
    {
        // Pull a 32-bit integer from the generator state
        // Every other access function simply transforms the numbers extracted here

        if( left == 0 ) reload();
        --left;

        long s1;
        s1 = state[pIndex++];
        s1 ^= (s1 >> 11);
        s1 ^= (s1 <<  7) & 0x9d2c5680L;
        s1 ^= (s1 << 15) & 0xefc60000L;
        return ( s1 ^ (s1 >> 18) );
    }

    //[0,n]
    public long randInt(long n )
    {
        // Find which bits are used in n
        // Optimized by Magnus Jonsson (magnus@smartelectronix.com)
        long used = n;
        used |= used >> 1;
        used |= used >> 2;
        used |= used >> 4;
        used |= used >> 8;
        used |= used >> 16;

        // Draw numbers until one is found in [0,n]
        long i;
        do
            i = randInt() & used;  // toss unused bits to shorten search
        while( i > n );
        return i;
    }

}
