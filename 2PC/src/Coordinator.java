import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Jorgen on 27/04/15.
 */
public interface Coordinator extends Remote {
    public void newCohort(String databaseURL, String user, String pass, String name)throws RemoteException;
    public boolean transaction(ArrayList<SubTransaction> requests)throws RemoteException;

}
