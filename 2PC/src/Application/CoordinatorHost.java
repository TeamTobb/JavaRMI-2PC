package Application;

import Cohort.Cohort;
import Coordinator.CoordinatorImpl;
import Transaction.SubTransaction;
import Misc.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class CoordinatorHost extends JFrame{
    private CoordinatorImpl coordinator;
    private JTextArea textArea = new JTextArea(15, 30);
    private TextAreaOutputStream taOutputStream = new TextAreaOutputStream(
            textArea, "Log");

    public CoordinatorHost(CoordinatorImpl coordinator){
        super();
        this.setTitle("Coordinator host");
        this.coordinator = coordinator;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        System.setOut(new PrintStream(taOutputStream));

        JButton recoverButton = new JButton("Recover");
        JButton exitButton = new JButton("Exit");

        recoverButton.addActionListener(e -> coordinator.recover());
        exitButton.addActionListener(e -> {
            try {
                Naming.unbind(Config.COORD_ADRESS);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            System.exit(0);
        });
        add(recoverButton);
        add(exitButton);
        pack();
    }

    public static void main(String[] args){
        try{
            LocateRegistry.createRegistry(2020);
            CoordinatorImpl coordinatorImpl = new CoordinatorImpl();
            CoordinatorHost gui = new CoordinatorHost(coordinatorImpl);
            gui.setVisible(true);
            String objectName = Config.COORD_ADRESS;
            Naming.rebind(objectName, coordinatorImpl);
            System.out.println("RMI-objekt er registrert");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
