import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.rmi.Naming;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class CohortImpl implements Cohort{
	private Coordinator coord;
	private Connection con;
    private String dbname;
//    private EntityManagerFactory emf;
//    private EntityManager em;
	public CohortImpl(String cordUrl, String user, String pass, String name){
        System.out.println("HEI");
        this.dbname = name;
        try{
            this.coord = (Coordinator)Naming.lookup("rmi://localhost:2020/coordinatorImpl");
            this.con = DriverManager.getConnection(cordUrl, user, pass);
            con.setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
/*
    public CohortImpl(EntityManagerFactory emf, String name) {
        this.dbname = name;
        this.emf = emf;
    }*/


    @Override
	public boolean voteRequest(String sql){
       /* em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(sql);*/
//        em.createQuery(sql);

        //log this
        try {
//            if(sql.equals("INSERT INTO biletter VALUES(DEFAULT,'passasjer')")) return false;
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
//        em.getTransaction().commit();
        try {
            con.commit();
            System.out.println("COMMIT D: ::D ");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rollback(){
        try {
            con.rollback();
            System.out.println("ROLLBACK D: ::D ");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
}