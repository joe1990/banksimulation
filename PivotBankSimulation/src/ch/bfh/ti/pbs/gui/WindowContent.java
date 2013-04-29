package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.*;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.content.*;
import ch.bfh.ti.pbs.bankaccounts.Bank;
import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.bankactivities.*;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.exceptions.UnderFlowException;
import ch.bfh.ti.pbs.helpers.BankReaderWriter;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

/**
 * Klasse welche für das GUI unterhalb des Menüs zuständig ist. Erbt von der PivotKlasse "TablePane".
 * Klasse gehört zum BXML-File "WindowContent.bxml" 
 */
public class WindowContent extends TablePane implements Bindable
{
    @BXML
    private TextInput txtFirstName;
    @BXML
    private TextInput txtLastName;
    @BXML
    private PushButton psbSaveCustomer;
    @BXML
    private PushButton psbNewTransaction;
    @BXML
    private TreeView trvUsers;
    @BXML
    private TableView tvTransactions;
    @BXML
    private NewTransactionDialog dlgNewTransaction;
    @BXML
    private TabPane tbpAccountDetails;

    private Customer customer;
    private BankAccount bankAccount;
    private Bank bank;
    private static int alertCounter = 0;

    public WindowContent()
    {

    }

    /**
     * Initialisiert das TablePane "WindowsContent".
     * Blendet das TabPane standardmässig aus. 
     */
    @Override public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
        tbpAccountDetails.setVisible(false);
        tbpAccountDetails.getTabs().get(1).setEnabled(false);
    }

    /**
     * Zeichnet das GUI dynamisch anhand des Inhalts im geladenen Bank.dat-File
     * 
     * @param bankFile Bank.dat File, welches geladen werden soll.
     */
    public void setWindowsOutput(File bankFile) throws FileNotFoundException, IOException, ClassNotFoundException,
            StreamCorruptedException {
        BankReaderWriter.getInstance().setWorkFile(bankFile);

        this.bank = BankReaderWriter.getInstance().readFile();
        this.fillTreeView(bank.Customers);

        txtFirstName.setText("");
        txtLastName.setText("");

        setTreeViewActions();
        setTableViewActions();
        setTransactions(bankAccount);
    }

    /**
     * Setzt den BankAccount
     * @param bankAccount
     */
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * Setzt den "NewTransaction"-Dialog
     * @param dlgNewTransaction
     */
    public void setNewTransactionDialog(NewTransactionDialog dlgNewTransaction) {
        this.dlgNewTransaction = dlgNewTransaction;
    }

    /**
     * Erzeugt die TreeView-Aktionen.
     * Implementiert ist nur die SelectedNodeChange-Aktion (wenn sich ausgewählter Kunde oder Transaktion im TreeView ändert).
     */
    private void setTreeViewActions() {
        this.trvUsers.getTreeViewSelectionListeners().add(new TreeViewSelectionListener() {

            @Override public void selectedPathsChanged(TreeView arg0, Sequence<Path> arg1) {
            }

            @Override public void selectedPathRemoved(TreeView arg0, Path arg1) {
            }

            @Override public void selectedPathAdded(TreeView arg0, Path arg1) {
            }

            @Override public void selectedNodeChanged(TreeView arg0, Object arg1) {
                TreeNode treeNode = (TreeNode) arg0.getSelectedNode();
                tbpAccountDetails.setVisible(true);

                if (arg0.getSelectedNode() instanceof TreeBranch) {
                    customer = (Customer) treeNode.getUserData();
                    txtFirstName.setText(customer.getFirstname());
                    txtLastName.setText(customer.getLastname());
                    setSaveCustomerActions();
                    tbpAccountDetails.getTabs().get(1).setEnabled(false);
                } else if (arg0.getSelectedNode() instanceof TreeNode) {
                    bankAccount = (BankAccount) treeNode.getUserData();
                    customer = (Customer) treeNode.getParent().getUserData();
                    txtFirstName.setText(customer.getFirstname());
                    txtLastName.setText(customer.getLastname());
                    setTransactions(bankAccount);
                    setSaveCustomerActions();
                    setSaveTransactionActions();
                    tbpAccountDetails.getTabs().get(1).setEnabled(true);
                    
                }
            }
        });
    }

    /**
     * Erzeugt die Aktionen der Transaktions-Tabelle (Sortierung).
     */
    private void setTableViewActions() {
        this.tvTransactions.getTableViewSelectionListeners().add(new TableViewSelectionListener.Adapter() {
            public void selectedRowChanged(TableView tableView, Object previousSelectedRow) {

            }
        });
        this.tvTransactions.getTableViewSortListeners().add(new TableViewSortListener() {

            @Override public void sortAdded(TableView tableView, String columnName) {
                System.out.println("SortAdded event fired.");

            }

            @Override public void sortUpdated(TableView tableView, String columnName,
                    SortDirection previousSortDirection) {
                System.out.println("sortUpdated event fired.");

            }

            @Override public void sortRemoved(TableView tableView, String columnName, SortDirection sortDirection) {
                System.out.println("sortRemoved event fired.");

            }

            @Override public void sortChanged(TableView tableView) {
                // System.out.println("Sort event fired.");

                @SuppressWarnings("unchecked")
                List<Object> tableData = (List<Object>) tableView.getTableData();
                tableData.setComparator(new org.apache.pivot.wtk.content.TableViewRowComparator(tableView));
            }

        });

    };

    /**
     * Erzeugt die Aktion für das Speichern mit dem "Save-Customer"-Button
     */
    private void setSaveCustomerActions() {
        psbSaveCustomer.getButtonPressListeners().add(new ButtonPressListener() {
            @Override public void buttonPressed(Button button) {
                customer.setLastname(txtLastName.getText());
                customer.setFirstname(txtFirstName.getText());
                fillTreeView(bank.Customers);
            }
        });
    }

    /**
     * Erzeugt die Aktion für das Speichern mit dem "Save-Transaction"-Button
     */
    private void setSaveTransactionActions() {
        dlgNewTransaction.getSaveTransactionButton().getButtonPressListeners().add(new ButtonPressListener() {
            public void buttonPressed(Button arg0) {
                saveTransaction();
            }
        });
    }

    /**
     * Speichert das aktuelle Bank-Objekt ins .Dat-File.
     */
    public void saveBankToFile() throws FileNotFoundException, IOException {
        BankReaderWriter.getInstance().writeFile();
    }

    /**
     * Gibt den Button "New Transaction" zurück.
     * @return PushButton "New Transaction".
     */
    public PushButton getNewTransactionButton() {
        return psbNewTransaction;
    }

    private void fillTreeView(ArrayList<Customer> customers) {

        TreeBranch rootBranch = new TreeBranch();
        for (Customer customer : customers) {
            TreeBranch customerBranch = new TreeBranch();

            customerBranch.setText(customer.getName());
            customerBranch.setIcon("/ch/bfh/ti/pbs/gui/user.png");
            customerBranch.setUserData(customer);

            for (BankAccount account : customer.getAccounts()) {
                TreeNode accountNode = new TreeNode();
                accountNode.setText(String.valueOf(account.getAccountNumber()));
                accountNode.setIcon("/ch/bfh/ti/pbs/gui/bankaccount.png");
                accountNode.setUserData(account);
                customerBranch.add(accountNode);
            }
            rootBranch.add(customerBranch);
        }

        this.trvUsers.setTreeData(rootBranch);
    }

    /**
     * Füllt die Transaction-Liste mit den Transaktionen eines spezifischen BankAccounts ab.
     * @param account BankAccount, wessen Transaktionen in der Transaction-Liste angezeigt werden sollen. 
     */
    private void setTransactions(BankAccount account) {
        if (bankAccount == null) {
            return;
        }

        List<Transaction> trans = new org.apache.pivot.collections.ArrayList<Transaction>();
        for (Transaction t : account.getTransactions()) {
            trans.add(t);
        }
        tvTransactions.clear();
        if (trans != null) {
            tvTransactions.setTableData(trans);
        }
    };

    /**
     * Prüft den im "New Transaction"-Dialog eingegebener Wert auf Gültigkeit (Double oder Int).
     * Wenn korrekter Wert--> Transaktion wird in Bank-Objekt gespeichert.
     * Wenn nicht korrekter Wert --> Fehler (AlertDialog) wird ausgegeben, mit der Info dass ein ungültiger Wert eingegeben wurde.
     */
    private void saveTransaction() {
        
        Scanner scanner = new Scanner(dlgNewTransaction.getAmount());
        if (scanner.hasNextDouble()) {
            double amount = scanner.nextDouble();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar currentDate = Calendar.getInstance();
            String currentDateString = dateFormat.format(currentDate.getTime());
            DateTime currentDateTime = new DateTime(currentDateString);

            if (amount > 0) {
                try {
                    bankAccount.deposit(currentDateTime, new Decimal(amount), "");
                    dlgNewTransaction.reset();
                    dlgNewTransaction.close();
                    setTransactions(bankAccount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (amount < 0) {
                amount = Math.abs(amount);
                try {
                    bankAccount.withdraw(currentDateTime, new Decimal(amount), "");
                    dlgNewTransaction.reset();
                    dlgNewTransaction.close();
                    setTransactions(bankAccount);
                } catch (UnderFlowException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (scanner != null && scanner.hasNext() != false && alertCounter == 0)
            {
                alertCounter = 1;
                dlgNewTransaction.close();
                Alert.alert(MessageType.INFO, "Please give a correct value.", BankSimulationWindow.getActiveWindow().getWindow());
            }
        }
    }
}