package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;
import HelperClasses.TreeNode;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins=new ArrayList<Pair<String, TransactionBlock>>();
  public Transaction[] in_process_trans=new Transaction[100];
  public int curr=0;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
     Pair<String, TransactionBlock> pp=mycoins.get(0);
     mycoins.remove(0);
     Transaction tt=new Transaction();
     tt.coinID=pp.first;
     tt.Source=this;
     tt.coinsrc_block=pp.second;
     in_process_trans[curr]=tt;
     curr+=1;
     DSobj.pendingTransactions.AddTransactions(tt);
     for (Members mem:DSobj.memberlist) {
         if (mem.UID.equals(destUID)){
             tt.Destination=mem;
             break;
         }
     }
     return;
  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
     Pair<String, TransactionBlock> pp=mycoins.get(0);
     mycoins.remove(0);
     Transaction tt=new Transaction();
     tt.coinID=pp.first;
     tt.Source=this;
     tt.coinsrc_block=pp.second;
     in_process_trans[curr]=tt;
     curr+=1;
     DSobj.pendingTransactions.AddTransactions(tt);
     for (Members mem:DSobj.memberlist) {
         if (mem.UID.equals(destUID)){
             tt.Destination=mem;
             break;
         }
     }
     return;
  }
  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    BlockChain_Honest bh=DSObj.bChain;
    TransactionBlock tB=bh.lastBlock;
    boolean flag=false;
    int ind=-1;
    while (!flag && tB!=null){
        ind=0;
        for (Transaction tt:tB.trarray) {
            if (tt==tobj){
                flag=true;
                break;
            }
            ind+=1;
        }
        if (!flag){
            tB=tB.previous;
        }
    }
    if (tB==null) {
        throw new MissingTransactionException();
    }
    ind+=1;
    TreeNode tn=tB.Tree.rootnode;
    int siz=tB.trarray.length;
    while (tn.left!=null){
        if (ind<=siz/2){
            tn=tn.left;
        }
        else{
            tn=tn.right;
            ind-=siz/2;
        }
        siz/=2;
    }
    List<Pair<String, String>> ll=new ArrayList<Pair<String, String>>();
    while (tn.parent!=null){
        if (tn==tn.parent.right){
            ll.add(new Pair(tn.parent.left.val, tn.val));
        }
        else{
            ll.add(new Pair(tn.val, tn.parent.right.val));
        }
        tn=tn.parent;
    }
    ll.add(new Pair(tn.val, null));
    List<Pair<String, String>> pp=new ArrayList<Pair<String, String>>();
    if (tB.previous==null) {
        pp.add(new Pair(bh.start_string, null));
        pp.add(new Pair(tB.dgst, bh.start_string+"#"+tB.trsummary+"#"+tB.nonce));
    }
    else {
        pp.add(new Pair(tB.previous.dgst, null));
        pp.add(new Pair(tB.dgst, tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce));
    }
    TransactionBlock curr=tB.next;
    while (curr!=null){
        pp.add(new Pair(curr.dgst, curr.previous.dgst+"#"+curr.trsummary+"#"+curr.nonce));
        curr=curr.next;
    }
    for (int i = 0; i < 100; i++) {
        if (in_process_trans[i]==tobj) {
            for (int j = i; j < 99; j++) {
                in_process_trans[j]=in_process_trans[j+1];
            }
            break;
        }
    }
    ind=0;
    while (ind<tobj.Destination.mycoins.size() && tobj.Destination.mycoins.get(ind).first.compareTo(tobj.coinID)<0) {
        ind+=1;
    }
    if (ind<tobj.Destination.mycoins.size()) {
        tobj.Destination.mycoins.add(ind, new Pair(tobj.coinID, tB));
    }
    else {
        tobj.Destination.mycoins.add(new Pair(tobj.coinID, tB));
    }
    return new Pair(ll, pp);
  }

  public void MineCoin(DSCoin_Honest DSObj) {
      BlockChain_Honest bh=DSObj.bChain;
      int cnt=bh.tr_count-1;
      Transaction[] arr=new Transaction[cnt+1];
      HashMap<String, String> hm=new HashMap<String, String>();
      while (cnt>0){
          try {
              Transaction tt = DSObj.pendingTransactions.RemoveTransaction();
              if (!hm.containsKey(tt.coinID)) {
                  if (bh.lastBlock.checkTransaction(tt)) {
                      hm.put(tt.coinID, null);
                      arr[arr.length - 1 - cnt] = tt;
                      cnt-=1;
                  }
              }
              //System.out.println("The size of queue is:"+DSObj.pendingTransactions.size()+" and the valid transactions are:"+(3-cnt));
          }
          catch (Exception e) {
              System.out.println("ERROR TERROR");
              break;
          }
      }
      Transaction tt=new Transaction();
      tt.Destination=this;
      arr[arr.length-1]=tt;
      int abc=new Integer(DSObj.latestCoinID);
      abc+=1;
      DSObj.latestCoinID=Integer.toString(abc);
      tt.coinID=DSObj.latestCoinID;
      TransactionBlock tB=new TransactionBlock(arr);
      bh.InsertBlock_Honest(tB);
      mycoins.add(new Pair(DSObj.latestCoinID, tB));
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
      BlockChain_Malicious bh=DSObj.bChain;
      int cnt=bh.tr_count-1;
      Transaction[] arr=new Transaction[cnt+1];
      HashMap<String, String> hm=new HashMap<String, String>();
      while (cnt>0){
          try {
              Transaction tt = DSObj.pendingTransactions.RemoveTransaction();
              if (!hm.containsKey(tt.coinID)){
                  if(bh.FindLongestValidChain().checkTransaction(tt)) {
                      hm.put(tt.coinID, null);
                      arr[arr.length - 1 - cnt] = tt;
                      cnt-=1;
                  }
              }
          }
          catch (EmptyQueueException e){}
      }
      Transaction tt=new Transaction();
      tt.Destination=this;
      arr[arr.length-1]=tt;
      int abc=new Integer(DSObj.latestCoinID);
      abc+=1;
      DSObj.latestCoinID=Integer.toString(abc);
      tt.coinID=DSObj.latestCoinID;
      TransactionBlock tB=new TransactionBlock(arr);
      bh.InsertBlock_Malicious(tB);
      mycoins.add(new Pair(DSObj.latestCoinID, tB));
  }
}
