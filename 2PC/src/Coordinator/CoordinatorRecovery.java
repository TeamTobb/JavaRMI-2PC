package Coordinator;

import Misc.CoordinatorStatus;
import Transaction.SubTransaction;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import Transaction.Transaction;

public class CoordinatorRecovery {
    private Coordinator coordinator;

    public void recover(Coordinator coordinator) throws RemoteException {
        this.coordinator = coordinator;
        List<CoordinatorLog> logItems = coordinator.getLogger().getLogItems();
        CoordinatorLog lastItem = logItems.get(logItems.size()-1);
        switch(lastItem.getStatus()){
            case INIT:
            case WAIT:
                System.out.println("Recovering from INIT/WAIT");
                initCase();
            break;

            case ABORT:
                System.out.println("Recovering from ABORT");
                abortCase();
            break;

            case COMMIT:
                System.out.println("Recovering from COMMIT");
                commitCase();
            break;

            case FINISHED:
                System.out.println("Recovering from FINISHED");
                //Nothing to see here
            break;
        }
    }

    private void initCase() throws RemoteException {
        ArrayList<SubTransaction> sts = setTransactions();
        coordinator.rollback();
        try {
            coordinator.transaction(sts);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private void abortCase() throws RemoteException {
        setTransactions();
        coordinator.rollback();
    }

    private void commitCase() throws RemoteException {
        setTransactions();
        coordinator.commit(coordinator.getTransaction().getTransID());
    }

    private ArrayList<SubTransaction> setTransactions() throws RemoteException {
        ArrayList<SubTransaction> sts = new ArrayList<>();
        List<CoordinatorLog> coordLogItems = coordinator.getLogger().getLogItems();
        for(CoordinatorLog l : coordLogItems) {
            if (l.getStatus() == CoordinatorStatus.INIT) {
                sts=l.getTransaction().getSubTransactions();
            }
        }
        coordinator.setTransaction(new Transaction(sts));
        return sts;
    }
}
