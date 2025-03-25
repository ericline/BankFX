package com.example.rubank;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import util.Date;
import util.Sort;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;


public class Controller {

    AccountDatabase accountDatabase;
    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    @FXML
    private TextField accountNumber;

    @FXML
    private ToggleGroup accountTypes;

    @FXML
    private TextField amount;

    @FXML
    private ComboBox<String> branches;

    @FXML
    private RadioButton camden;

    @FXML
    private ToggleGroup campus;

    @FXML
    private ToggleGroup campus1;

    @FXML
    private RadioButton cd;

    @FXML
    private RadioButton checking;

    @FXML
    private Button clearButton;

    @FXML
    private Button closeAccount;

    @FXML
    private Button closeAll;

    @FXML
    private DatePicker closingDate;

    @FXML
    private RadioButton collegeChecking;

    @FXML
    private DatePicker dateOpened;

    @FXML
    private Button depositButton;

    @FXML
    private DatePicker dob;

    @FXML
    private DatePicker dobTransaction;

    @FXML
    private TextField firstName;

    @FXML
    private TextField firstNameTransaction;

    @FXML
    private TextField initialDeposit;

    @FXML
    private TextField lastName;

    @FXML
    private TextField lastNameTransaction;

    @FXML
    private Button loadAccounts;

    @FXML
    private Button loadActivities;

    @FXML
    private CheckBox loyalty;

    @FXML
    private RadioButton moneyMarket;

    @FXML
    private RadioButton newBrunswick;

    @FXML
    private RadioButton newark;

    @FXML
    private Button openButton;

    @FXML
    private TextArea output;

    @FXML
    private Button printArchive;

    @FXML
    private Button printBranch;

    @FXML
    private Button printHolder;

    @FXML
    private Button printStatements;

    @FXML
    private Button printType;

    @FXML
    private RadioButton saving;

    @FXML
    private ComboBox<String> terms;

    @FXML
    private Button withdrawButton;


    /**
     * This method will be performed automatically after the fxml is loaded.
     * Write code to set the initial data for the GUI objects.
     * Typically, set a list of objects from the backend to the frontend objects,
     * such as ComboBox (dropdown), or ListView.
     */
    public void initialize() {

        this.accountDatabase = new AccountDatabase();
        terms.getItems().addAll("3","6","9","12");
        branches.getItems().addAll("Edison","Bridgewater","Princeton","Piscataway", "Warren");
        disableDefault();
        catchDateExeception(dob);
        catchDateExeception(dateOpened);

    }

    private void catchDateExeception(DatePicker datePicker) {
        datePicker.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                try {
                    String input = datePicker.getEditor().getText();
                    if (input != null && !input.isEmpty()) {
                        Date date = new Date(input);
                    }
                } catch (NumberFormatException e) {
                    print("Invalid date: Please enter a date in the following format: dd/MM/yyyy");
                    datePicker.getEditor().clear();
                }
            }
        });

    }
    private void disableDefault() {
        newBrunswick.setDisable(true);
        newark.setDisable(true);
        camden.setDisable(true);
        loyalty.setDisable(true);
        terms.setDisable(true);
        dateOpened.setDisable(true);
        newBrunswick.setSelected(false);
        newark.setSelected(false);
        camden.setSelected(false);
        loyalty.setSelected(false);
        terms.getSelectionModel().clearSelection();
        dateOpened.getEditor().clear();
    }

    @FXML
    private void handleAccountTypes(ActionEvent event) {
        RadioButton selectedType = (RadioButton) accountTypes.getSelectedToggle();
        if (selectedType != null) {
            disableDefault();
            if(selectedType.equals(collegeChecking)) {
                newBrunswick.setDisable(false);
                newark.setDisable(false);
                camden.setDisable(false);
                newBrunswick.setSelected(true);
            }
            else if(selectedType.equals(saving) || selectedType.equals(moneyMarket)) {
                loyalty.setDisable(false);
            }
            else if(selectedType.equals(cd)) {
                terms.setDisable(false);
                dateOpened.setDisable(false);
            }
        }
    }
    @FXML
    private void handleClear(ActionEvent event) {
        firstName.clear();
        lastName.clear();
        dob.getEditor().clear();
        collegeChecking.setSelected(false);
        saving.setSelected(false);
        moneyMarket.setSelected(false);
        cd.setSelected(false);
        newBrunswick.setSelected(false);
        newark.setSelected(false);
        camden.setSelected(false);
        loyalty.setSelected(false);
        terms.getSelectionModel().clearSelection();
        branches.getSelectionModel().clearSelection();
        dateOpened.getEditor().clear();
        initialDeposit.clear();
        disableDefault();
    }

    public void println(String text) {
        output.appendText(text + "\n");
    }

    public void print(String text) {
        output.appendText(text);
    }

    @FXML
    private void handleOpen(ActionEvent event) {
        String firstNameText = firstName.getText().trim();
        String lastNameText = lastName.getText().trim();
        LocalDate dobDate = dob.getValue();
        RadioButton selectedAccountType = (RadioButton) accountTypes.getSelectedToggle();
        String initialDepositString = initialDeposit.getText().trim();
        String branchName = branches.getValue();

        if (firstNameText.isEmpty()) {
            println("Please fill in first name");
            return;
        }
        if (lastNameText.isEmpty()) {
            println("Please fill in last name");
            return;
        }
        if (selectedAccountType == null) {
            println("Please select account type");
            return;
        }
        if (initialDepositString.isEmpty()) {
            println("Please fill in initial deposit");
            return;
        }

        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(initialDepositString);
        } catch (NumberFormatException e) {
            println("For input string: \"" + initialDepositString + "\" - not a valid amount.");
            return;
        }

        Date dob = null;
        // Validate DOB
        try {
            dob = new Date(dobDate.getMonthValue(), dobDate.getDayOfMonth(), dobDate.getYear());
        }
        catch (NullPointerException e) {
            println("DOB invalid: " + dob + " not a valid calendar date!");
            return;
        }
        if (!dob.isValid()) {
            println("DOB invalid: " + dob + " not a valid calendar date!");
            return;
        }
        Date futureDate = new Date("3/24/2025");
        if (dob.compareTo(futureDate) >= 0) {
            println("DOB invalid: " + dob + " cannot be today or a future day.");
            return;
        }
        if (!dob.isOver18()) {
            println("Not eligible to open: " + dob + " under 18.");
            return;
        }

        AccountType accountType;
        if (selectedAccountType.equals(checking)) {
            accountType = AccountType.CHECKING;
        }
        else if(selectedAccountType.equals(saving)) {
            accountType = AccountType.SAVINGS;
        }
        else if(selectedAccountType.equals(collegeChecking)) {
            accountType = AccountType.COLLEGE_CHECKING;
        }
        else if(selectedAccountType.equals(moneyMarket)) {
            accountType = AccountType.MONEY_MARKET;
        }
        else if(selectedAccountType.equals(cd)) {
            accountType = AccountType.CD;
        }
        else {
            println("Please select account type");
            return;
        }

        if (accountType.name().equals("MONEY_MARKET") && initialDeposit < 2000) {
            println("Minimum of $2,000 to open a Money Market account.");
            return;
        }

        Branch branch;
        try {
            branch = Branch.valueOf(branchName.toUpperCase());
        } catch (IllegalArgumentException e) {
            println(branchName + " - invalid branch.");
            return;
        }

        Campus campusCode = Campus.NEW_BRUNSWICK;
        int term = 0;
        Date open = new Date("3/24/2025");
        if(accountType.name().equals("COLLEGE_CHECKING")) {
            RadioButton selectedCampus = (RadioButton) campus.getSelectedToggle();
            if (selectedCampus.equals(newark)) {
                campusCode = Campus.NEWARK;
            }
            else if(selectedCampus.equals(camden)) {
                campusCode = Campus.CAMDEN;
            }
            if (dob.isOver24()) {
                println("Not eligible to open: " + dob + " over 24.");
                return;
            }
        }
        else if(accountType.name().equals("CD")) {
            term = Integer.parseInt(terms.getValue());
            LocalDate dateOpenedValue = dateOpened.getValue();
            open = new Date(dateOpenedValue);
            if (initialDeposit < 1000) {
                println("Minimum of $1,000 to open a Certificate Deposit account.");
                return;
            }
            if (term % 3 != 0 || term > 12 || term <= 0) {
                println(term + " is not a valid term.");
                return;
            }
        }

        if (initialDeposit <= 0) {
            println("Initial deposit cannot be 0 or negative.");
            return;
        }

        Profile holder = new Profile(firstNameText, lastNameText, dob);
        if (accountDatabase.hasAccountType(holder, accountType)) {
            println(holder.getFname() + " " + holder.getLname() +
                    " already has a " + accountType.name() + " account.");
            return;
        }
        AccountNumber accountNumber = new AccountNumber(branch, accountType);
        Account newAccount = null;
        switch (accountType.name()) {
            case "CHECKING":
                newAccount = new Checking(accountNumber, holder, initialDeposit);
                break;
            case "SAVINGS":
                newAccount = new Savings(accountNumber, holder, initialDeposit, accountDatabase);
                break;
            case "MONEY_MARKET":
                newAccount = new MoneyMarket(accountNumber, holder, initialDeposit, 0, accountDatabase);
                break;
            case "COLLEGE_CHECKING":
                newAccount = new CollegeChecking(accountNumber, holder, initialDeposit, campusCode);
                break;
            case "CD":
                newAccount = new CertificateDeposit(accountNumber, holder, initialDeposit, term, open, accountDatabase);
                break;
        }
        accountDatabase.add(newAccount);
        println(newAccount.getNumber().getType().name() + " account " +
                newAccount.getNumber() + " has been opened.");
    }

    @FXML
    private void handleDeposit() {
        String accountNumberStr = accountNumber.getText().trim();
        if (accountNumberStr.isEmpty()) {
            println("Account number is empty.");
            return;
        }

        String depositAmount = amount.getText().trim();
        if (depositAmount.isEmpty()) {
            print("Deposit amount is empty.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(depositAmount);
        } catch (NumberFormatException e) {
            println("For input string: \"" + depositAmount + "\" - not a valid amount.");
            return;
        }

        if (amount <= 0) {
            println(amount + " - deposit amount cannot be 0 or negative.");
            return;
        }

        boolean deposit = accountDatabase.deposit(accountNumberStr, amount);
        if (!deposit) {
            println(accountNumberStr + " does not exist.");
            return;
        }

        println("$" + formatter.format(amount) + " deposited to " + accountNumberStr);
    }

    @FXML
    private void handleWithdraw() {
        String accountNumberStr = accountNumber.getText().trim();
        if (accountNumberStr.isEmpty()) {
            println("Account number is empty.");
            return;
        }

        String withdrawAmount = amount.getText().trim();
        double amount;
        try {
            amount = Double.parseDouble(withdrawAmount);
        } catch (NumberFormatException e) {
            println("For input string: \"" + withdrawAmount + "\" - not a valid amount.");
            return;
        }

        if (amount <= 0) {
            println(amount + " withdrawal amount cannot be 0 or negative.");
            return;
        }

        AccountNumber accountNumber = new AccountNumber(accountNumberStr);
        Account account = accountDatabase.getAccount(accountNumber);
        if (account == null) {
            println(accountNumberStr + " does not exist.");
            return;
        }
        double previousBalance = account.getBalance(); // Store balance before withdrawal

        boolean success = accountDatabase.withdraw(accountNumberStr, amount);
        if (success) {
            double newBalance = account.getBalance(); // Store balance after withdrawal

            if (account instanceof MoneyMarket) {
                if (previousBalance < 2000 || newBalance < 2000) {
                    println(accountNumberStr + " balance below $2,000 - $"
                            + formatter.format(amount) + " withdrawn from " + accountNumberStr);
                } else {
                    println("$" + formatter.format(amount) + " withdrawn from " + accountNumberStr);
                }
            } else {
                println("$" + formatter.format(amount) + " withdrawn from " + accountNumberStr);
            }
        }
        else {
            if (previousBalance < 2000) {
                print(accountNumberStr + " balance below $2,000 - ");
            }
            print("withdrawing $" + formatter.format(amount));
            println(" - insufficient funds.");
        }
    }

    @FXML
    private void handleAccounts() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Accounts File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            println("Processing \"" + selectedFile.getName() + "\"...");
            try {
                accountDatabase.loadAccounts(selectedFile);
            } catch (IOException e) {
                println("Invalid file: " + selectedFile.getName());
                return;
            } catch (ArrayIndexOutOfBoundsException e) {
                println("Invalid file: " + selectedFile.getName());
                return;
            }
        } else {
            println("No file selected.");
            return;
        }

        println("Accounts in \"" + selectedFile.getName() + "\" loaded to the database.");
    }

    @FXML
    private void processActivities() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Activities File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            println("Processing \"" + selectedFile.getName() + "\"...");
            try {
                accountDatabase.processActivities(selectedFile);
            } catch (IOException e) {
                println("Invalid file: " + selectedFile.getName());
                return;
            }
        } else {
            println("No file selected.");
            return;
        }

        println("Accounts in \"" + selectedFile.getName() + "\" loaded to the database.");
    }

    @FXML
    private void handlePrintByBranch() {
        println("");
        println("*List of accounts ordered by branch location (county, city).");
        printSortedAccounts('B');
        println("*end of list.");
        println("");
    }
    @FXML
    private void handlePrintByHolder() {
        println("");
        println("*List of accounts ordered by account holder and number.");
        printSortedAccounts('H');
        println("*end of list.");
        println("");
    }
    @FXML
    private void handlePrintByType() {
        println("");
        println("*List of accounts ordered by account type and number.");
        printSortedAccounts('T');
        println("*end of list.");
        println("");
    }
    @FXML
    private void handlePrintStatements() {
        println("");
        println("*Account statements by account holder.");
        accountDatabase.printStatements(this);
        println("*end of statements.");
        println("");
    }

    public void printSortedAccounts(char key) {
        if (accountDatabase.isEmpty()) {
            println("Account database is empty.");
            return;
        }

        // Sort accounts based on the given key
        Sort.account(accountDatabase, key);

        // Print accounts in sorted order
        if (key == 'H') {
            for (Account account : accountDatabase) {
                println(account.toString());
            }
        } else if (key == 'T') {
            AccountType currentType = null;

            for (Account account : accountDatabase) {
                if (account == null) continue;

                AccountType accountType = account.getNumber().getType();
                if (currentType == null || !currentType.equals(accountType)) {
                    println("Account Type: " + accountType);
                    currentType = accountType;
                }
                println(account.toString());
            }
        } else if (key == 'B') {
            String currentCounty = null;

            for (Account account : accountDatabase) {
                if (account == null) continue;

                Branch branch = account.getNumber().getBranch();
                String county = branch.getCounty();
                if (currentCounty == null || !currentCounty.equalsIgnoreCase(county)) {
                    println("County: " + county);
                    currentCounty = county;
                }
                println(account.toString());
            }
        }
    }
}