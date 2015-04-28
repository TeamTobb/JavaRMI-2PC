package Coordinator;

import Cohort.Cohort;
import Transaction.SubTransaction;
import Transaction.Transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface Coordinator extends Remote {
    boolean newCohort(String databaseURL, String user, String pass, String name) throws RemoteException;
    boolean newCohort(Cohort cohort) throws RemoteException;
    boolean transaction(ArrayList<SubTransaction> requests) throws RemoteException;
    CoordinatorLogger getLogger() throws RemoteException ;
    void rollback() throws RemoteException ;
    void setTransaction(Transaction transaction) throws RemoteException ;
    void commit(long id) throws RemoteException ;
    Transaction getTransaction() throws RemoteException ;
    List<CoordinatorLog> getLogItems() throws RemoteException;
}
