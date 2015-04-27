import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

class CoordinatorImpl extends UnicastRemoteObject implements Coordinator{
	private ArrayList<Cohort> cohorts = new ArrayList<>();
	List<Boolean> votes = Collections.synchronizedList(new ArrayList<Boolean>()); 

	public void newCohort(String databaseURL, String user, String pass, String name)throws RemoteException {
		this.cohorts.add(new CohortImpl(databaseURL, user, pass, name));
	}

    public CoordinatorImpl() throws RemoteException {

    }

	public void voteRequest(ArrayList<SubTransaction> requests)throws RemoteException {
		votes = new ArrayList<>(); 
		for(SubTransaction st : requests) {
			// do this in threads if possible
			votes.add(getCohort(st.getDb_name()).voteRequest(st.getQuery()));
		}
		// if timeout -> rollback()
	}

	public Cohort getCohort(String dbname)throws RemoteException {
		for(Cohort c : this.cohorts){
			if(c.getDb_name().equals(dbname)) return c;
		}
		return null; 
	}

    public void commit()throws RemoteException {
        for(Cohort c: cohorts){
            c.commit();
        }
    }

    public boolean transaction(ArrayList<SubTransaction> requests)throws RemoteException {
        voteRequest(requests);
        if(votes.contains(false)){
//            rollback();
            return false;
        }
        commit();
        return true;
    }
}
