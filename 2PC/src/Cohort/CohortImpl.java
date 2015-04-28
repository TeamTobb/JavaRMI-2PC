package Cohort;

import java.rmi.Naming;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import Coordinator.Coordinator;

public class CohortImpl implements Cohort{
	private Coordinator coord;
	private Connection con;
    private String dbname;
    private CohortLogger logger;


	public CohortImpl(String cordUrl, String user, String pass, String name){
        this.dbname = name;
        this.logger = new CohortLogger(dbname);
        try{
            this.coord = (Coordinator)Naming.lookup("rmi://localhost:2020/coordinatorImpl");
            this.con = DriverManager.getConnection(cordUrl, user, pass);
            con.setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
        }
	}

    @Override
	public boolean voteRequest(long id, String sql){
        logger.log(new CohortLog(id, CohortLog.CohortStatus.INIT));
        try {
//            if(sql.equals("INSERT INTO biletter VALUES(DEFAULT,'passasjer')")) return false;
            Statement st = this.con.createStatement();
            int res = st.executeUpdate(sql);
            if(res==1){
                logger.log(new CohortLog(id, CohortLog.CohortStatus.READY));
                return true;
            }else{
                logger.log(new CohortLog(id, CohortLog.CohortStatus.ABORT));
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.log(new CohortLog(id, CohortLog.CohortStatus.ABORT));
            return false;
        }
	}

    @Override
    public String getDb_name() {
        return dbname;
    }

	public boolean commit(long id){
        logger.log(new CohortLog(id, CohortLog.CohortStatus.COMMIT));
        try {
            con.commit();
            logger.log(new CohortLog(id, CohortLog.CohortStatus.FINISHED));
            System.out.println("Commit. DBname: " + this.dbname);
            return true;
        } catch (SQLException e) {
            //did not crash, but no commit. WHAT TO DO
            e.printStackTrace();
            //log nothing?
            return false;
        }
    }

    public boolean rollback() {
        try {
            con.rollback();
            System.out.println("Rollback. DBname: " + this.dbname);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}