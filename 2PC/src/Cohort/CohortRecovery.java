package Cohort;
import Coordinator.CoordinatorLog;
import Misc.CoordinatorStatus;
import Transaction.SubTransaction;

import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class CohortRecovery {
    private long id;
    public void recover(CohortImpl cohort) throws Exception {
        List<CohortLog> logItems = CohortLogger.getLogItems(cohort.getDb_name() + ".json");
        CohortLog lastItem = logItems.get(logItems.size()-1);
        this.id = lastItem.getId();

        switch (lastItem.getStatus()){
            case INIT:
                System.out.println("Recovering from INIT");
                cohort.rollback(id);
            break;

            case READY:
                System.out.println("Recovering from READY");
                readyCase(cohort);
            break;

            case ABORT:
                System.out.println("Recovering from ABORT");
                cohort.rollback(id);
            break;

            case COMMIT:
                System.out.println("Recovering from COMMIT");
               commitCase(cohort);
            break;

            case FINISHED:
                System.out.println("Recovering from FINISHED");
                //nothing to see here...
            break;

            default:
                cohort.rollback(id);
            break;
        }
    }

    private void readyCase(Cohort cohort) throws Exception{
        List<CoordinatorLog>  coordLogItems = cohort.getCoord().getLogItems(this.id);
        for (CoordinatorLog l : coordLogItems) {
            if(l.getStatus() == CoordinatorStatus.ABORT){
                cohort.rollback(id);
                return;
            }else if(l.getStatus() == CoordinatorStatus.COMMIT) {
                for (CoordinatorLog cl : coordLogItems) {
                    if (cl.getStatus() == CoordinatorStatus.INIT) {
                        for (SubTransaction s : cl.getTransaction().getSubTransactions()) {
                            if (s.getDb_name().equals(cohort.getDb_name())) {
                                cohort.executeAndCommit(s.getQuery());
                                return;
                            }
                        }
                    }
                }
            }
        }
        cohort.rollback(id);
    }

    private void commitCase(Cohort cohort) throws Exception{
        List<CoordinatorLog> coordLogItems = cohort.getCoord().getLogItems(this.id);
        for(CoordinatorLog l : coordLogItems){
            if(l.getStatus() == CoordinatorStatus.INIT){
                for (SubTransaction s : l.getTransaction().getSubTransactions()){
                    if(s.getDb_name().equals(cohort.getDb_name())){
                        cohort.executeAndCommit(s.getQuery());
                        return;
                    }
                }
            }
        }
        //last resort
        cohort.rollback(id);
    }
}