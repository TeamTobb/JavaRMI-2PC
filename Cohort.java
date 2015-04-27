interface Cohort{
    public boolean voteRequest(String db_name);
    public boolean commit();
    public String getDb_name();
}