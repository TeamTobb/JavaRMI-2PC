package Cohort;

import java.io.Serializable;

/**
 * Created by Jorgen on 28/04/15.
 */
public class CohortLog implements Serializable {
    private long id;
    public enum CohortStatus{INIT, READY, ABORT, COMMIT, FINISHED};
    private CohortStatus status;

    public CohortLog(long id, CohortStatus status) {
        this.id = id;
        this.status = status;
    }

    public String toString(){
        return this.id + this.status.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CohortStatus getStatus() {
        return status;
    }

    public void setStatus(CohortStatus status) {
        this.status = status;
    }
}
