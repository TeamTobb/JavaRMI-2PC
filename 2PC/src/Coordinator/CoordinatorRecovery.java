package Coordinator;

import java.util.List;

public class CoordinatorRecovery {

    public void recover(CoordinatorImpl coordinator){
        List<CoordinatorLog> logItems = CoordinatorLogger.getLogItems();
        CoordinatorLog lastItem = logItems.get(logItems.size());
        switch(lastItem.getStatus()){
            case INIT:

            break;

            case COMMIT:

            break;

            case FINISHED:

            break;

            case WAIT:

            break;
        }
    }
}
