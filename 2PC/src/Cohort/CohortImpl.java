package Cohort;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import Coordinator.Coordinator;
import Misc.CohortStatus;
import Misc.Config;

public class CohortImpl extends UnicastRemoteObject implements Cohort {
	private Coordinator coord;
	private Connection con;
    private String dbname;
    private CohortLogger logger;

	public CohortImpl(String cordUrl, String user, String pass, String name) throws RemoteException {
        this.dbname = name;
        this.logger = new CohortLogger(dbname);
        try{
            this.coord = (Coordinator)Naming.lookup(Config.COORD_ADRESS);
            this.con = DriverManager.getConnection(cordUrl, user, pass);
            con.setAutoCommit(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
	public boolean voteRequest(long id, String sql) throws RemoteException{
        logger.log(new CohortLog(id, CohortStatus.INIT));
        try {
//            if(sql.equals("INSERT INTO biletter VALUES(DEFAULT,'passasjer')")) return false;
            Statement st = this.con.createStatement();
            int res = st.executeUpdate(sql);
            if(res==1){
                logger.log(new CohortLog(id, CohortStatus.READY));
                return true;
            }else{
                logger.log(new CohortLog(id, CohortStatus.ABORT));
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.log(new CohortLog(id, CohortStatus.ABORT));
            return false;
        }
	}

    public void recover(){
        try{
            new CohortRecovery().recover(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getDb_name() throws RemoteException {
        return dbname;
    }

	public boolean commit(long id) throws RemoteException{
        logger.log(new CohortLog(id, CohortStatus.COMMIT));
        try {
            con.commit();
            logger.log(new CohortLog(id, CohortStatus.FINISHED));
            System.out.println("Commit. DBname: " + this.dbname);
            return true;
        } catch (SQLException e) {
            //did not crash, but no commit. WHAT TO DO
            e.printStackTrace();
            //log nothing?
            return false;
        }
    }

    public boolean rollback(long id) throws RemoteException {
        logger.log(new CohortLog(id, CohortStatus.ABORT));
        try {
            con.rollback();
            System.out.println("Rollback. DBname: " + this.dbname);
            logger.log(new CohortLog(id, CohortStatus.FINISHED));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void executeAndCommit(String sql) throws RemoteException{
        try{
            Statement st = this.con.createStatement();
            st.executeUpdate(sql);
            con.commit();
            System.out.println("ExecuteAndCommit. DBname: " + this.dbname);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Coordinator getCoord() throws RemoteException {
        return coord;
    }
}