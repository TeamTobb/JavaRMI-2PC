package Coordinator;
import Cohort.Cohort;

import java.util.List;

public class CoordinatorPingThread extends Thread {
    private List<Cohort> cohorts;
    public CoordinatorPingThread(List<Cohort> cohorts){
        this.cohorts = cohorts;
    }

    public void run(){
        while(true) {
            for (int i = cohorts.size()-1; i>=0; i--) {
                try {
                    cohorts.get(i).getDb_name();
                } catch (Exception e) {
                    System.out.println("No answer from cohort pingthread");
                    cohorts.remove(i);
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
