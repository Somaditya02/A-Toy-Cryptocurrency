package DSCoinPackage;
import HelperClasses.*;
public class Moderator
 {

public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    Members mem=new Members();
    mem.UID="Moderator";
    Transaction[] arr=new Transaction[DSObj.bChain.tr_count];
    int cnt1=0;
    int cnt2=0;
    DSObj.latestCoinID="99999";
    while (coinCount>0){
        Transaction tt=new Transaction();
        tt.Source=mem;
        tt.Destination=DSObj.memberlist[cnt2];
        int abc=new Integer(DSObj.latestCoinID);
        abc+=1;
        DSObj.latestCoinID=Integer.toString(abc);
        tt.coinID= DSObj.latestCoinID;
        arr[cnt1]=tt;
        cnt1+=1;
        if (cnt1==DSObj.bChain.tr_count) {
            cnt1=0;
            TransactionBlock tB=new TransactionBlock(arr);
            int ind=(cnt2-arr.length+1+1000*DSObj.memberlist.length)%DSObj.memberlist.length;
            for (int i = 0; i < arr.length; i++) {
                DSObj.memberlist[(ind+i)%DSObj.memberlist.length].mycoins.add(new Pair(arr[i].coinID, tB));
            }
            DSObj.bChain.InsertBlock_Honest(tB);
        }
        cnt2+=1;
        cnt2%=DSObj.memberlist.length;
        coinCount-=1;
    }
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
      Members mem=new Members();
      mem.UID="Moderator";
      Transaction[] arr=new Transaction[DSObj.bChain.tr_count];
      int cnt1=0;
      int cnt2=0;
      DSObj.latestCoinID="99999";
      while (coinCount>0){
          Transaction tt=new Transaction();
          tt.Source=mem;
          tt.Destination=DSObj.memberlist[cnt2];
          int abc=new Integer(DSObj.latestCoinID);
          abc+=1;
          DSObj.latestCoinID=Integer.toString(abc);
          tt.coinID= DSObj.latestCoinID;
          arr[cnt1]=tt;
          cnt1+=1;
          if (cnt1==DSObj.bChain.tr_count){
              cnt1=0;
              TransactionBlock tB=new TransactionBlock(arr);
              int ind=(cnt2-arr.length+1+1000*DSObj.memberlist.length)%DSObj.memberlist.length;
              for (int i = 0; i < arr.length; i++) {
                  DSObj.memberlist[(ind+i)%DSObj.memberlist.length].mycoins.add(new Pair(arr[i].coinID, tB));
              }
              DSObj.bChain.InsertBlock_Malicious(tB);
          }
          cnt2+=1;
          cnt2%=DSObj.memberlist.length;
          coinCount-=1;
      }
  }
}
