package Application;

import Coordinator.CoordinatorImpl;
import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CoordinatorHost extends JFrame{
    private CoordinatorImpl coordinator;
    private JTextArea textArea = new JTextArea(15, 30);
    private TextAreaOutputStream taOutputStream = new TextAreaOutputStream(textArea, "Log");
    private String objectName;

    public CoordinatorHost(CoordinatorImpl coordinator, String objectName){
        super();
        this.objectName = objectName;
        this.setTitle("Coordinator host");
        this.coordinator = coordinator;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        System.setOut(new PrintStream(taOutputStream));

        JButton recoverButton = new JButton("Recover");
        JButton exitButton = new JButton("Exit");

        recoverButton.addActionListener(e -> coordinator.recover());
        exitButton.addActionListener(e -> {
            try {
                Naming.unbind(objectName);
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
            String objectName = "rmi://localhost:" + args[0] + "/coordinatorImpl";
            LocateRegistry.createRegistry(Integer.parseInt(args[0]));
            CoordinatorImpl coordinatorImpl = new CoordinatorImpl();
            CoordinatorHost gui = new CoordinatorHost(coordinatorImpl, objectName);
            gui.setVisible(true);

            Naming.rebind(objectName, coordinatorImpl);
            System.out.println("RMI-objekt er registrert");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
