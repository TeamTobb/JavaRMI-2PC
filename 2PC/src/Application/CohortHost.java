package Application;

import Cohort.CohortImpl;
import Coordinator.Coordinator;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CohortHost extends JFrame {
    boolean color = true;
    private CohortImpl cohort;
    private JTextArea textArea = new JTextArea(15, 30);
    private TextAreaOutputStream taOutputStream = new TextAreaOutputStream(textArea, "Log");
    private String objectname;

    public CohortHost(CohortImpl cohort, String objectname){
        super();
        this.objectname = objectname;
        this.cohort = cohort;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        System.setOut(new PrintStream(taOutputStream));

        JButton recoverButton = new JButton("Recover");
        JButton exitButton = new JButton("Exit");
        JToggleButton shouldNotRecoverButton = new JToggleButton("Disable commit");

        shouldNotRecoverButton.addItemListener(ev -> cohort.changeShouldCommit());

        recoverButton.addActionListener(e -> cohort.recover());
        exitButton.addActionListener(e -> {
            try {
                Naming.unbind(objectname);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });
        add(recoverButton);
        add(exitButton);
        add(shouldNotRecoverButton);
        pack();
    }

    public static void main(String[] args){
        try{
            LocateRegistry.createRegistry(2020);

        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            CohortImpl cohortImpl = new CohortImpl(args[0], args[1], args[2], args[3], "rmi://" + args[4] + ":" + args[5] + "/coordinatorImpl" );
            String objectName = "//localhost:2020/CohortImpl";
            Naming.rebind(objectName, cohortImpl);
            System.out.println("RMI-objekt er registrert");
            Coordinator coordinator = (Coordinator) Naming.lookup("rmi://" + args[4] + ":" + args[5] + "/coordinatorImpl");
            coordinator.newCohort(cohortImpl);
            CohortHost gui = new CohortHost(cohortImpl, objectName);
            gui.setTitle("CohortHOST: " + args[3]);
            gui.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}