package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

 public void AddTransactions (Transaction transaction) {
    if (numTransactions==0){
      firstTransaction=transaction;
      lastTransaction=transaction;
      numTransactions+=1;
      return;
    }
    else {
      lastTransaction.next=transaction;
      transaction.previous=lastTransaction;
      lastTransaction=transaction;
      numTransactions+=1;
      return;
    }
  }

  public Transaction RemoveTransaction () throws EmptyQueueException {
    if (numTransactions==0){
      throw new EmptyQueueException();
    }
    Transaction t=firstTransaction;
    if (numTransactions==1){
      firstTransaction=null;
      lastTransaction=null;
      numTransactions=0;
      return t;
    }
    numTransactions-=1;
    firstTransaction=t.next;
    return t;
  }

  public int size() {
    return numTransactions;
  }
}
