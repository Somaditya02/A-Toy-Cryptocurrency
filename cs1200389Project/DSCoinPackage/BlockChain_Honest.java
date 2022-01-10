package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    String s= "1000000001";
    CRF obj=new CRF(64);
    String ss;
    if (lastBlock==null){
      ss=start_string;
    }
    else{
      ss=lastBlock.dgst;
    }
    while (!obj.Fn(ss+"#"+newBlock.trsummary+"#"+s).substring(0, 4).equals("0000")){
      long a=new Long(s);
      a+=1;
      s=Long.toString(a);
    }
    newBlock.nonce=s;
    newBlock.dgst=obj.Fn(ss+"#"+newBlock.trsummary+"#"+newBlock.nonce);
    newBlock.previous=lastBlock;
    if (lastBlock!=null){
      lastBlock.next=newBlock;
    }
    lastBlock=newBlock;
  }
}
