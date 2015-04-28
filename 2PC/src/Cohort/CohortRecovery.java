package Cohort;
import java.util.List;

public class CohortRecovery {
    public void recover(CohortImpl cohort) {
        List<CohortLog> logItems = CohortLogger.getLogItems(cohort.getDb_name()+ ".json");
        CohortLog lastItem = logItems.get(logItems.size());
        switch (lastItem.getStatus()){
            case INIT:
                
            break;

            case READY:
            break;

            case ABORT:
            break;

            case COMMIT:
            break;

            case FINISHED:
            break;
        }
    }
}