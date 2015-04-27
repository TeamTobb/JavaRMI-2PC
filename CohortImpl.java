import java.rmi.Naming;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class CohortImpl implements Cohort{
	private Coordinator coord;
	private Connection con;
    private String dbname;

	public CohortImpl(String cordUrl, String user, String pass, String name){
        this.dbname = name;
        try{
            this.coord = (Coordinator)Naming.lookup("rmi://localhost:2020/coordinatorImpl");
            this.con = DriverManager.getConnection(cordUrl, user, pass);
            con.setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
        }
	}

    @Override
	public boolean voteRequest(String sql){
        //log this
        try {
            Statement st = this.con.createStatement();
            return st.executeUpdate(sql) == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
	}

    @Override
    public String getDb_name() {
        return dbname;
    }


	public boolean commit(){
        try {
            con.commit();
            System.out.println("COMMIT D: ::D ");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
}