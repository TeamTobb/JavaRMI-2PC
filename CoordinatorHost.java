import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Created by Jorgen on 27/04/15.
 */
public class CoordinatorHost {
    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(2020);
        CoordinatorImpl coordinatorImpl = new CoordinatorImpl();

        String objectName = "//localhost:2020/coordinatorImpl";
        Naming.rebind(objectName, coordinatorImpl);
        System.out.println("RMI-objekt er registrert");
        coordinatorImpl.newCohort("jdbc:mysql://10.0.1.7:8889/Bil", "db", "db", "BilDB");
        coordinatorImpl.newCohort("jdbc:mysql://10.0.1.7:8889/Fly", "db", "db", "FlyDB");
        SubTransaction trans1 = new SubTransaction("BilDB", "INSERT INTO biler VALUES(DEFAULT,'brafarg')");
        SubTransaction trans2 = new SubTransaction("FlyDB", "INSERT INTO biletter VALUES(DEFaAULT,'passasjer')");
        ArrayList<SubTransaction> test = new ArrayList<>();
        test.add(trans1);
        test.add(trans2);

        coordinatorImpl.transaction(test);

        javax.swing.JOptionPane.showMessageDialog(null, "Trykk OK for a stoppe tjeneren.");
        Naming.unbind(objectName);
        System.exit(0);
    }

}
