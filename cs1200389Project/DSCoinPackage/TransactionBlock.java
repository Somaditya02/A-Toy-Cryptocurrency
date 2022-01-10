package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;
  public TransactionBlock next; 
  TransactionBlock(Transaction[] t) {
    trarray=new Transaction[t.length];
    for (int i = 0; i < t.length; i++) {
      trarray[i]=new Transaction();
      trarray[i].coinID=t[i].coinID;
      trarray[i].coinsrc_block=t[i].coinsrc_block;
      trarray[i].Destination=t[i].Destination;
      trarray[i].Source=t[i].Source;
      trarray[i].previous=t[i].previous;
      trarray[i].next=t[i].next;
    }
    MerkleTree tt=new MerkleTree();
    tt.Build(trarray);
    Tree=tt;
    trsummary=Tree.rootnode.val;
  }

  public boolean checkTransaction (Transaction t) {
    String s=t.coinID;
    TransactionBlock blk=t.coinsrc_block;
    if (blk==null){
      return true;
    }
    boolean flag=false;
    for (Transaction tt:blk.trarray) {
      if (tt.coinID.equals(s) && tt.Destination==t.Source){
        flag=true;
        break;
      }
    }
    if (flag){
      TransactionBlock curr=this;
      boolean ff=true;
      while (curr!=blk && ff){
        for (Transaction tt:curr.trarray) {
          if (tt.coinID.equals(s) && tt!=t) {
            ff=false;
            break;
          }
        }
        curr=curr.previous;
      }
      return ff;
    }
    else{
      return false;
    }
  }
}
