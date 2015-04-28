package Application;

import Coordinator.CoordinatorImpl;
import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

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

        pack();
    }

    public static void main(String[] args) throws Exception {
        CoordinatorHost gui = new CoordinatorHost();
        gui.setVisible(true);
        LocateRegistry.createRegistry(2020);
        CoordinatorImpl coordinatorImpl = new CoordinatorImpl();

        String objectName = "//localhost:2020/coordinatorImpl";
        Naming.rebind(objectName, coordinatorImpl);
        System.out.println("RMI-objekt er registrert");

        javax.swing.JOptionPane.showMessageDialog(null, "Trykk OK for a stoppe tjeneren.");
        Naming.unbind(objectName);
        System.exit(0);
    }

}
