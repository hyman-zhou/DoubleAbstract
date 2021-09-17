package test;

/**
 * Administrator on 2018.11.15 17:13.
 */
public class TestMain {
    public static void main(String[] args) {
        TestString pTestString=new TestString();
        if(true){
//            pTestString.testDeblockOfSOI();
//            System.gc();
//            pTestString.testDeblockOfLMC();
//            System.gc();
//            pTestString.testDeblockOfAE();
//            System.gc();
//            pTestString.testDeblockOfCAAM();
//            System.gc();
            pTestString.testDeblockOfRABIN();
//            System.gc();
//            pTestString.testDeblockOfMII();
//            System.gc();//pTestString.testDeblockOfPCI();
            System.gc();
//            pTestString.testDeblockOfMII_Limit();
//            System.gc();
        }else if(false){
            System.gc();
            pTestString.testDeblockOfMII();
            System.gc();
//            pTestString.testDeblockOfMII_Limit();
//            System.gc();
//            pTestString.ChangeFileSeries();
        }else {
            pTestString.geneFile();
            pTestString.ChangeFileSeries();
            //pTestString.fileModifySerious();
        }
        //pTestString.testDeblockOfBM();
        //pTestString.testDeblockOfLMI();
        //pTestString.testPartition();
        //pTestString.testPartition3();
        //pTestString.testMOd();
        //pTestString.testcos();
    }
}
