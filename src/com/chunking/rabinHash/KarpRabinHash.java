package com.chunking.rabinHash;

/**
 * Created by Administrator on 2019/3/12 0012.
 */

// myn is the length of the sequences, e.g., 3 means that you want to hash sequences of 3 characters
// mywordsize is the number of bits you which to receive as hash values, e.g., 19 means that the hash values are 19-bit integers
public class KarpRabinHash {
    private long hashvalue;
    private int n;
    private int wordsize;
    private CharacterHash hasher;
    private long HASHMASK;
    private long BtoN;
    private long B;
    private MersenneRNG merRng;
    {
        B=37;
        wordsize=9;
    }
    public KarpRabinHash(int myn,int wordsize){
        hashvalue=0;
        n=myn;
        wordsize=wordsize;
        merRng=new MersenneRNG();
        hasher=new CharacterHash(merRng.maskfnc(wordsize));
        HASHMASK=merRng.maskfnc(wordsize);
        BtoN=1;
        for(int i=0;i<n;++i){
            BtoN *=B;
            BtoN &=HASHMASK;
        }
    }
    public void reset(){
        hashvalue=0;
    }
    public long  hash(int[] c) {
        long answer=0;
        for(int k = 0; k<c.length; ++k) {
            long x=1;
            for(long j = 0; j< c.length-1-k; ++j) {
                x= (x * B) & HASHMASK;
            }
            x= (x * hasher.hashvalues[c[k]]) & HASHMASK;
            answer=(answer+x) & HASHMASK;
        }
        return answer;
    }
    // add inchar as an input, this is used typically only at the start
    // the hash value is updated to that of a longer string (one where inchar was appended)
    public long eat(int inchar){
        hashvalue=(B*hashvalue +  hasher.hashvalues[inchar] )& HASHMASK;
        return hashvalue;
    }
    // add inchar as an input and remove outchar, the hashvalue is updated
    // this function can be used to update the hash value from the hash value of [outchar]ABC to the hash value of ABC[inchar]
    public long update(int outchar, int inchar) {
        hashvalue = (B*hashvalue +  hasher.hashvalues[inchar] - BtoN *  hasher.hashvalues[outchar]) & HASHMASK;
        return hashvalue;
    }
}
