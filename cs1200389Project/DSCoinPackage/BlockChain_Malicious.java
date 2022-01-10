package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    boolean flag=true;
    CRF obj=new CRF(64);
    flag=tB.dgst.substring(0, 4).equals("0000");
    if(!flag)return flag;
    String ss;
    if (tB.previous==null){
      ss=start_string;
    }
    else{
      ss=tB.previous.dgst;
    }
    flag&=tB.dgst.equals(obj.Fn(ss+"#"+tB.trsummary+"#"+tB.nonce));
    if(!flag)return flag;
    MerkleTree mt=new MerkleTree();
    String s=mt.Build(tB.trarray);
    flag&=s.equals(tB.trsummary);
    for (Transaction tt:tB.trarray) {
      flag&=tB.checkTransaction(tt);
    }
    return flag;
  }

  public TransactionBlock FindLongestValidChain () {
    int siz=0;
    TransactionBlock ans=null;
    for (TransactionBlock tt:lastBlocksList) {
      TransactionBlock curr=tt;
      TransactionBlock last;
      while (curr!=null){
        int temp=0;
        while(curr!=null && !checkTransactionBlock(curr)) {
          curr=curr.previous;
        }
        last=curr;
        while (curr!=null && checkTransactionBlock(curr)) {
          temp += 1;
          curr = curr.previous;
        }
        siz=Math.max(siz, temp);
        if (siz==temp){
          ans=last;
        }
      }
    }
    return ans;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock=FindLongestValidChain();
    newBlock.previous=lastBlock;
    String pro= "1000000001";
    CRF obj=new CRF(64);
    String ss;
    if (lastBlock==null) {
      ss=start_string;
    }
    else {
      ss=lastBlock.dgst;
    }
    while (!obj.Fn(ss+"#"+newBlock.trsummary+"#"+pro).substring(0, 4).equals("0000")) {
      int a=new Integer(pro);
      a+=1;
      pro=Integer.toString(a);
    }
    newBlock.nonce=pro;
    newBlock.dgst=obj.Fn(ss+"#"+newBlock.trsummary+"#"+newBlock.nonce);
    boolean flag=false;
    for (TransactionBlock tb: lastBlocksList) {
      if (tb!=null) flag|=(lastBlock==tb);
    }
    if (flag) {
      for (int i=0;i< lastBlocksList.length && lastBlocksList[i]!=null;i++) {
        if (lastBlocksList[i]==lastBlock) {
          lastBlocksList[i]=newBlock;
        }
      }
    }
    else {
      int ind=0;
      while (lastBlocksList[ind]!=null){
        ind+=1;
      }
      lastBlocksList[ind]=newBlock;
    }
  }
}
