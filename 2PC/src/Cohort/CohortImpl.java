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

public class CohortImpl extends UnicastRemoteObject implements Cohort {
	private Coordinator coord;
	private Connection con;
    private String dbname;
    private CohortLogger logger;
    private boolean shouldNotCommit = false;

	public CohortImpl(String cordUrl, String user, String pass, String name, String coordAdress) throws RemoteException {
        this.dbname = name;
        this.logger = new CohortLogger(dbname);
        try{
            this.coord = (Coordinator)Naming.lookup(coordAdress);
            this.con = DriverManager.getConnection(cordUrl, user, pass);
            con.setAutoCommit(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
	public boolean voteRequest(long id, String sql) throws RemoteException{
        System.out.println("Vote request ");
        logger.log(new CohortLog(id, CohortStatus.INIT));
        System.out.println("Logged status INIT: " + id);
        try {
            Statement st = this.con.createStatement();
            System.out.println("Created statement");
            int res = st.executeUpdate(sql);
            if(res==1){
                logger.log(new CohortLog(id, CohortStatus.READY));
                System.out.println("ExecuteUpdate successful. ");
                System.out.println("Logged status READY" + id);
                return true;
            }else{
                System.out.println("ExecuteUpdate failure");
                System.out.println("Logged status ABORT: " + id);
                logger.log(new CohortLog(id, CohortStatus.ABORT));
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ExecuteUpdate failure");
            System.out.println("Logged status ABORT: " + id);
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

    @Override
	public boolean commit(long id) throws RemoteException{
        if(shouldNotCommit) return false;
        System.out.println("Logging status COMMIT: " + id);
        logger.log(new CohortLog(id, CohortStatus.COMMIT));
        try {
            con.commit();
            System.out.println("Commit successful.");
            logger.log(new CohortLog(id, CohortStatus.FINISHED));
            System.out.println("Logging status FINISHED: " + id);
            return true;
        } catch (SQLException e) {
            System.out.println("Commit failure. ");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rollback(long id) throws RemoteException {
        System.out.println("Logging status ABORT: " + id);
        logger.log(new CohortLog(id, CohortStatus.ABORT));
        try {
            con.rollback();
            System.out.println("Rollback successful.");
            logger.log(new CohortLog(id, CohortStatus.FINISHED));
            System.out.println("Logging status FINISHED: " + id);
            return true;
        } catch (Exception e) {
            System.out.println("Rollback failure");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void executeAndCommit(String sql) throws RemoteException{
        try{
            Statement st = this.con.createStatement();
            st.executeUpdate(sql);
            con.commit();
            System.out.println("ExecuteAndCommit successful");

        }catch(Exception e){
            System.out.println("ExecuteAndCommit failure");
            e.printStackTrace();
        }
    }

    @Override
    public Coordinator getCoord() throws RemoteException {
        return coord;
    }

    public void changeShouldCommit() {
        this.shouldNotCommit = !this.shouldNotCommit;
    }
}