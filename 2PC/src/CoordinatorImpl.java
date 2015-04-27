import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CoordinatorImpl extends UnicastRemoteObject implements Coordinator {
	private ArrayList<Cohort> cohorts = new ArrayList<>();
	List<Boolean> votes = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Thread> cohortThreads = new ArrayList<>();

	public void newCohort(String databaseURL, String user, String pass, String name)throws RemoteException {
        System.out.println("IT WORKS");
        this.cohorts.add(new CohortImpl(databaseURL, user, pass, name));
        System.out.println("LEGG TIL COHORT");
    }

    public CoordinatorImpl() throws RemoteException {
    }


	public void voteRequest(ArrayList<SubTransaction> requests)throws RemoteException {
        cohortThreads = new ArrayList<>();
		votes = Collections.synchronizedList(new ArrayList<>());
		for(SubTransaction st : requests) {
            this.cohortThreads.add(new VoteThread(votes, getCohort(st.getDb_name()), st));
        }
        for (Thread t : cohortThreads) {
            t.start();
        }
        for (Thread t : cohortThreads) {
            try {
                // if timeout -> rollback()
                t.join(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void rollback()throws RemoteException {
        for(Cohort c: cohorts){
            c.rollback();
        }
    }

    public boolean transaction(ArrayList<SubTransaction> requests)throws RemoteException {
        voteRequest(requests);
        if(votes.contains(false) || votes.size()<requests.size()){
            rollback();
            return false;
        }
        commit();
        return true;
    }
}
