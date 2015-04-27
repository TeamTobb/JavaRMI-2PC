interface Cohort{
    public boolean voteRequest(String query);
    public boolean commit();
    public String getDb_name();
    public boolean rollback();
}