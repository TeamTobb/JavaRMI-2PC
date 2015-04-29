package Coordinator;

import Cohort.Cohort;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Misc.Config;
import Misc.CoordinatorStatus;
import Transaction.Transaction;
import Transaction.SubTransaction;

public class CoordinatorImpl extends UnicastRemoteObject implements Coordinator {
	private List<Cohort> cohorts = Collections.synchronizedList(new ArrayList<>());
    private Date date;
	List<Boolean> votes = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Thread> cohortThreads = new ArrayList<>();
    private CoordinatorLogger logger = new CoordinatorLogger("Coordinatorlog");
    private Transaction transaction;
    private CoordinatorPingThread pingThread = new CoordinatorPingThread(cohorts);

	/*public boolean newCohort(String databaseURL, String user, String pass, String name, String coordinatorAdress)throws RemoteException {
        for(Cohort c : cohorts){
            if(c.getDb_name().equals(name)) return false;
        }
        this.cohorts.add(new CohortImpl(databaseURL, user, pass, name, coordinatorAdress));
        return true;
    }*/

    public boolean newCohort(Cohort cohort) throws RemoteException{
        for(Cohort c : cohorts) {
            if (c.getDb_name().equals(cohort.getDb_name())) return false;
        }
        this.cohorts.add(cohort);
        System.out.println("New cohort added: " + cohort.getDb_name());
        return true;
    }

    public CoordinatorImpl() throws RemoteException {
        pingThread.start();
    }

    public void recover(){
        try{
            new CoordinatorRecovery().recover(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

	public void voteRequest()throws RemoteException {
        //log requests to data log.write(requests.toJSON());
        System.out.println("Begin vote requests");
        cohortThreads = new ArrayList<>();
		votes = Collections.synchronizedList(new ArrayList<>());
        System.out.println("Creating " + transaction.getSubTransactions().size() + " threads, 1 per SubTrans");
        for(SubTransaction st : transaction.getSubTransactions()) {
            this.cohortThreads.add(new VoteThread(transaction.getTransID(), votes, getCohort(st.getDb_name()), st));
        }

        cohortThreads.forEach(Thread::start); // SEND VOTE REQUESTS

        System.out.println("Vote requests are sent");
        System.out.println("Logging status WAIT");
        logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorStatus.WAIT));

        for (Thread t : cohortThreads) {
            try {
                // if timeout -> rollback()
                //Timeout threads after a given time.
                t.join(Config.THREAD_WAIT_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	public Cohort getCohort(String dbname) throws RemoteException {
		for(Cohort c : this.cohorts){
			if(c.getDb_name().equals(dbname)) return c;
		}
		return null; 
	}

    private boolean isCohortInTransaction(Cohort cohort) throws RemoteException {
        for(SubTransaction st : transaction.getSubTransactions()){
            if(st.getDb_name().equals(cohort.getDb_name())) return true;
        }
        return false;
    }

    public void commit(long id) throws RemoteException {
        ArrayList<String> failureList = new ArrayList<>();
        ArrayList<Boolean> acks = new ArrayList<>();
        for(Cohort c: cohorts){
            if(isCohortInTransaction(c)){
                // this can timeout - threads?
                if(c.commit(id)) acks.add(true);
                else failureList.add(c.getDb_name());
            }
        }
        if(acks.size() == transaction.getSubTransactions().size() && !acks.contains(false)){
            System.out.println("Commit successful");
            logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorStatus.FINISHED));
        }else{
            System.out.println("Commit failure.");
            failureList.forEach(s -> System.out.println("Run recovery on: " + s));
            logger.errorLog(id);
            logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorStatus.FINISHED));
        }
    }

    public void rollback() throws RemoteException {
        for(Cohort c: cohorts){
            if(isCohortInTransaction(c)) c.rollback(transaction.getTransID());
        }
        logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorStatus.FINISHED));
    }

    // synchronized - only 1 transaction simultaneously
    public synchronized boolean transaction(ArrayList<SubTransaction> requests) throws RemoteException {
        System.out.println("Begin transaction");
        this.transaction = new Transaction(requests);
        logger.log(new CoordinatorLog(transaction)); // INIT
        System.out.println("Logging status INIT");
        voteRequest();
        System.out.println("Checking if all votes are positive, and number of votes are correct.");
        if(votes.contains(false) || votes.size()<requests.size()){
            System.out.println("Too few votes / at least 1 false vote");
            System.out.println("Logging status ABORT");
            logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorStatus.ABORT));
            System.out.println("Starting rollback");
            rollback();
            System.out.println("Rollback successful");
            return false;
        }
        System.out.println("Correct number of votes && only positive votes");
        System.out.println("Logging status COMMIT");
        logger.log(new CoordinatorLog(transaction.getTransID(), CoordinatorStatus.COMMIT));
        commit(transaction.getTransID());
        System.out.println("Logging status FINISHED");
        return true;
    }

    public CoordinatorLogger getLogger() throws RemoteException {
        return logger;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() throws RemoteException {
        return transaction;
    }

    public List<CoordinatorLog> getLogItems(long id) throws RemoteException{
        return logger.getLogItems(id);
    }
}