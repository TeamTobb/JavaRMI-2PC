import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
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
        createCohorts();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton but1 = new JButton("Hans bestiller pakkereise. Det er nok pakkereiser.");
        but1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    SubTransaction trans1 = new SubTransaction("BilDB", "INSERT INTO biler VALUES(DEFAULT,'brafarg')");
                    SubTransaction trans2 = new SubTransaction("FlyDB", "INSERT INTO biletter VALUES(DEFAULT,'passasjer')");
                    ArrayList<SubTransaction> test = new ArrayList<>();
                    test.add(trans1);
                    test.add(trans2);

                    boolean transaction = coordinator.transaction(test);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                System.out.println("You clicked the button");
            }
        });
        add(but1);

        JButton but2 = new JButton("Geir bestiller pakkereise. Det er nok biler & hotell, ikke nok fly. ");
        add(but2);
//        but1.addActionListener();

        JButton but3 = new JButton("Herman bestiller pakkereise, men en database er sprengt. ");
        add(but3);

        pack();
    }

    public void createCohorts() throws Exception{
        coordinator.newCohort("jdbc:mysql://10.0.1.7:8889/Bil", "db", "db", "BilDB");
        coordinator.newCohort("jdbc:mysql://10.0.1.7:8889/Fly", "db", "db", "FlyDB");
    }

    public static void main(String[] args) throws Exception{
        Coordinator coordinatorImpl = (Coordinator) Naming.lookup("rmi://localhost:2020/coordinatorImpl");
        System.out.println(coordinatorImpl);
        TestGUI gui = new TestGUI(coordinatorImpl);
        gui.setVisible(true);
        System.out.println("done");
    }

}




