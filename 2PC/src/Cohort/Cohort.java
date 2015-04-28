package Cohort;

public interface Cohort{
    public boolean voteRequest(long id, String sql);
    public boolean commit(long id);
    public String getDb_name();
    public boolean rollback();
}