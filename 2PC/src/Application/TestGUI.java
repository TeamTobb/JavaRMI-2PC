package Application;

import Coordinator.Coordinator;
import Misc.Config;
import Transaction.SubTransaction;
import Cohort.Cohort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.util.ArrayList;

/**
 * Created by Jorgen on 27/04/15.
 */
public class TestGUI extends JFrame{
    private Coordinator coordinator;

    public TestGUI(Coordinator coordinator) throws Exception{
        super();
        this.coordinator = coordinator;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton but1 = new JButton("Hans bestiller pakkereise. Det er nok pakkereiser.");
        but1.addActionListener(e -> {
            try {
                coordinator.newCohort((Cohort)Naming.lookup("rmi://10.0.1.12:2020/CohortImpl1"));
                coordinator.newCohort((Cohort)Naming.lookup("rmi://10.0.1.7:2020/CohortImpl2"));
                SubTransaction trans1 = new SubTransaction("BilDB", "INSERT INTO biler VALUES(DEFAULT,'brafarg')");
                SubTransaction trans2 = new SubTransaction("FlyDB", "INSERT INTO biletter VALUES(DEFAULT,'passasjer')");
                ArrayList<SubTransaction> sts = new ArrayList<>();
                sts.add(trans1);
                sts.add(trans2);

                boolean answer = coordinator.transaction(sts);
                //this can be used in the application for further validation
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        add(but1);

        JButton but2 = new JButton("Geir bestiller pakkereise. Det er nok biler, men ikke nok fly.");
        but2.addActionListener(e -> {
            try {
//                coordinator.newCohort((Cohort)Naming.lookup("rmi://localhost:2020/CohortImpl1"));
//                coordinator.newCohort((Cohort)Naming.lookup("rmi://localhost:2020/CohortImpl2"));
                coordinator.newCohort((Cohort)Naming.lookup("rmi://10.0.1.12:2020/CohortImpl1"));
                coordinator.newCohort((Cohort)Naming.lookup("rmi://10.0.1.7:2020/CohortImpl2"));
                SubTransaction trans1 = new SubTransaction("BilDB", "INSERT INTO biler VALUES(DEFAULT,'Fin Farge')");
                //Changed ".. INTO biletter" to "... INTO baasdiletter", to manually force an error.
                SubTransaction trans2 = new SubTransaction("FlyDB", "INSERT INTO baasdiletter VALUES(DEFAULT,'passasjer')");
                ArrayList<SubTransaction> sts = new ArrayList<>();
                sts.add(trans1);
                sts.add(trans2);

                boolean answer = coordinator.transaction(sts);
                //this can be used in the application for further validation
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(but2);
        pack();
    }

    public static void main(String[] args){
        try {
            Coordinator coordinatorImpl = (Coordinator) Naming.lookup(Config.COORD_ADRESS);
            System.out.println(coordinatorImpl);
            TestGUI gui = new TestGUI(coordinatorImpl);
            gui.setVisible(true);
            System.out.println("done");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}




