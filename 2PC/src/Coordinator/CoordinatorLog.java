package Coordinator;
import Transaction.Transaction;

import java.io.Serializable;


/**
 * Created by Jorgen on 28/04/15.
 */
public class CoordinatorLog implements Serializable {
    public enum CoordinatorStatus{INIT, WAIT, ABORT, COMMIT, FINISHED}
    private CoordinatorStatus status;
    private long id;
    private Transaction transaction;

    public CoordinatorLog(long id, CoordinatorStatus status) {
        this.status = status;
    }

    public CoordinatorLog(Transaction transaction){
        this.transaction = transaction;
        this.id = transaction.getTransID();
        this.status = CoordinatorStatus.INIT;
    }

    public CoordinatorStatus getStatus() {
        return status;
    }

    public void setStatus(CoordinatorStatus status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}