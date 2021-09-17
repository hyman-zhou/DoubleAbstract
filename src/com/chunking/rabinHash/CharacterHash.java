package com.chunking.rabinHash;

/**
 * Created by Administrator on 2019/3/12 0012.
 */
public class CharacterHash {
    private int nbrofchars;
    public long[] hashvalues;
    {
        nbrofchars=1<<8;
        hashvalues=new long[1<<8];
    }
    CharacterHash(long maxval) {
        MersenneRNG randomgenerator = new MersenneRNG(maxval);
        for(int k =0; k<nbrofchars; ++k)
            hashvalues[k] = randomgenerator.getRandInt();
    }
}
