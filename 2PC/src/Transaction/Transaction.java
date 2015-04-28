package Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Transaction implements Serializable{
    private ArrayList<SubTransaction> subTransactions;
    private long transID;

    public Transaction(ArrayList<SubTransaction> subTransactions) {
        this.subTransactions = subTransactions;
        this.transID = new Date().getTime();
    }

    public Transaction(){
    }

    public ArrayList<SubTransaction> getSubTransactions() {
        return subTransactions;
    }

    public void setSubTransactions(ArrayList<SubTransaction> subTransactions) {
        this.subTransactions = subTransactions;
    }

    public long getTransID() {
        return transID;
    }

    public void setTransID(long transID) {
        this.transID = transID;
    }

}
