import java.util.*;

class VoteThread extends Thread{

    private List<Boolean> votes;
    private Cohort cohort;
    private SubTransaction st;

    public VoteThread(List<Boolean> votes, Cohort cohort, SubTransaction st){
        this.votes = votes;
        this.cohort = cohort;
        this.st = st;
    }

    public void run(){
        System.out.println(cohort.getDb_name());
        votes.add(cohort.voteRequest(st.getQuery()));
//        votes.add(getCohort(st.getDb_name()).voteRequest(st.getQuery()));
    }
}