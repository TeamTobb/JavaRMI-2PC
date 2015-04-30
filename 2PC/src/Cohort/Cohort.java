package Cohort;

import Coordinator.Coordinator;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Cohort extends Remote {
    boolean voteRequest(long id, String sql) throws RemoteException;
    boolean commit(long id) throws RemoteException;
    String getDb_name() throws RemoteException;
    boolean rollback(long id) throws RemoteException;
    Coordinator getCoord() throws RemoteException;
    void executeAndCommit(String sql) throws RemoteException;
}