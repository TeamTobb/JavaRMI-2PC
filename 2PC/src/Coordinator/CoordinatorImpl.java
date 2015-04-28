package Coordinator;

import Cohort.Cohort;
import Cohort.CohortImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import Transaction.Transaction;
import Transaction.SubTransaction;

public class CoordinatorImpl extends UnicastRemoteObject implements Coordinator {
	private ArrayList<Cohort> cohorts = new ArrayList<>();
    private Date date;
	List<Boolean> votes = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Thread> cohortThreads = new ArrayList<>();
    private CoordinatorLogger logger = new CoordinatorLogger("Coordinatorlog");

	public void newCohort(String databaseURL, String user, String pass, String name)throws RemoteException {
        this.cohorts.add(new CohortImpl(databaseURL, user, pass, name));
    }

    public CoordinatorImpl() throws RemoteException {
    }


	public void voteRequest(Transaction transaction)throws RemoteException {
        //log requests to data log.write(requests.toJSON());
        cohortThreads = new ArrayList<>();
		votes = Collections.synchronizedList(new ArrayList<>());
		for(SubTransaction st : transaction.getSubTransactions()) {
            this.cohortThreads.add(new VoteThread(transaction.getTransID(), votes, getCohort(st.getDb_name()), st));
        }
        for (Thread t : cohortThreads) {
            t.start();
        }

        logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorLog.CoordinatorStatus.WAIT));

        for (Thread t : cohortThreads) {
            try {
                // if timeout -> rollback()
                t.join(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } //if crash, wait abit and redo whole shit
	}

	public Cohort getCohort(String dbname)throws RemoteException {
		for(Cohort c : this.cohorts){
			if(c.getDb_name().equals(dbname)) return c;
		}
		return null; 
	}

    public void commit(long id)throws RemoteException {
        for(Cohort c: cohorts){
            c.commit(id);
        }
        //log everything ok
    }

    public void rollback()throws RemoteException {

        for(Cohort c: cohorts){
            c.rollback();
        }
    }

    public boolean transaction(ArrayList<SubTransaction> requests)throws RemoteException {
        Transaction transaction = new Transaction(requests);
        logger.log(new CoordinatorLog(transaction));
        voteRequest(transaction);
        if(votes.contains(false) || votes.size()<requests.size()){
            logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorLog.CoordinatorStatus.ABORT));
            rollback();
            return false;
        }

        logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorLog.CoordinatorStatus.COMMIT));
        commit(transaction.getTransID());
        return true;
    }
}
