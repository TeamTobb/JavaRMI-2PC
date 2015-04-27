import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Created by Jorgen on 27/04/15.
 */
public class CoordinatorHost extends JFrame{
    private JTextArea textArea = new JTextArea(15, 30);
    private TextAreaOutputStream taOutputStream = new TextAreaOutputStream(
            textArea, "Test");

    public CoordinatorHost(){
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        System.setOut(new PrintStream(taOutputStream));

        add(new JButton("hei"));
        setVisible(true);
        pack();
    }

    public static void main(String[] args) throws Exception {
        CoordinatorHost gui = new CoordinatorHost();
        LocateRegistry.createRegistry(2020);
        CoordinatorImpl coordinatorImpl = new CoordinatorImpl();


//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Oving4PU");

        String objectName = "//localhost:2020/coordinatorImpl";
        Naming.rebind(objectName, coordinatorImpl);
        System.out.println("RMI-objekt er registrert");

//        Coordinator coordinatorImpl2 = (Coordinator) Naming.lookup("rmi://localhost:2020/coordinatorImpl");
//        coordinatorImpl2.newCohort("jdbc:mysql://10.0.1.7:8889/Bil", "db", "db", "BilDB");


        javax.swing.JOptionPane.showMessageDialog(null, "Trykk OK for a stoppe tjeneren.");
        Naming.unbind(objectName);
        System.exit(0);
    }

}
