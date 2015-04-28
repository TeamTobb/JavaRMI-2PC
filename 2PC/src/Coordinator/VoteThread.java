package Coordinator;

import Cohort.Cohort;
import Transaction.SubTransaction;

import java.util.*;

class VoteThread extends Thread{

    private List<Boolean> votes;
    private Cohort cohort;
    private SubTransaction st;
    private long id;

    public VoteThread(long id, List<Boolean> votes, Cohort cohort, SubTransaction st){
        this.votes = votes;
        this.cohort = cohort;
        this.st = st;
        this.id = id;
    }

    public void run(){
        System.out.println(cohort.getDb_name());
        votes.add(cohort.voteRequest(id, st.getQuery()));
    }
}