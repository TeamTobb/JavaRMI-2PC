package Coordinator;

import Transaction.SubTransaction;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Coordinator extends Remote {
    void newCohort(String databaseURL, String user, String pass, String name)throws RemoteException;
    boolean transaction(ArrayList<SubTransaction> requests)throws RemoteException;

}
