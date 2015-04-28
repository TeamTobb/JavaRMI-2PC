package Application;

import Cohort.CohortImpl;
import Cohort.Cohort;
import Coordinator.CoordinatorImpl;
import Misc.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;




public class CohortHost extends JFrame {
    private CohortImpl cohort;
    private JTextArea textArea = new JTextArea(15, 30);
    private TextAreaOutputStream taOutputStream = new TextAreaOutputStream(
            textArea, "Log");
    private String objectname;

    public CohortHost(CohortImpl cohort, String objectname){
        super();
        this.objectname = objectname;
        this.cohort = cohort;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        System.setOut(new PrintStream(taOutputStream));

        JButton recoverButton = new JButton("Recover");
        JButton exitButton = new JButton("Exit");


        recoverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cohort.recover();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Naming.unbind(objectname);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        add(recoverButton);
        add(exitButton);
        pack();
    }

    public static void main(String[] args){
        try{
            LocateRegistry.createRegistry(2020);

        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            CohortImpl cohortImpl = new CohortImpl(args[0], args[1], args[2], args[3]);
            String objectName = "//localhost:2020/CohortImpl" + args[4];
            Naming.rebind(objectName, cohortImpl);
            System.out.println("RMI-objekt er registrert");

            CohortHost gui = new CohortHost(cohortImpl, objectName);
            gui.setVisible(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}