package com.example.rubank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Demo TableView.
 * The data source is the Enum class Location. The GUI object is a TableView with
 * 2 columns: zip and county.
 * @author Lily Chang
 */
public class Controller {

    AccountDatabase accountDatabase;
    @FXML
    private ComboBox<String> terms;

    @FXML
    private ToggleGroup accountTypes;

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
    private RadioButton collegeChecking;

    @FXML
    private DatePicker dateOpened;

    @FXML
    private DatePicker dob;

    @FXML
    private TextField firstName;

    @FXML
    private TextField initialDeposit;

    @FXML
    private TextField lastName;

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
    private RadioButton saving;

    @FXML
    private TextArea output;


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

    private void print(String text) {
        output.appendText(text + "\n");
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
            print("Please fill in first name");
            return;
        }
        if (lastNameText.isEmpty()) {
            print("Please fill in last name");
            return;
        }
        if (selectedAccountType == null) {
            print("Please select account type");
            return;
        }
        if (initialDepositString.isEmpty()) {
            print("Please fill in initial deposit");
            return;
        }

        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(initialDepositString);
        } catch (NumberFormatException e) {
            print("For input string: \"" + initialDepositString + "\" - not a valid amount.");
            return;
        }

        Date dob = null;
        // Validate DOB
        try {
            dob = new Date(dobDate.getMonthValue(), dobDate.getDayOfMonth(), dobDate.getYear());
        }
        catch (NullPointerException e) {
            print("DOB invalid: " + dob + " not a valid calendar date!");
            return;
        }
        if (!dob.isValid()) {
            print("DOB invalid: " + dob + " not a valid calendar date!");
            return;
        }
        Date futureDate = new Date("3/24/2025");
        if (dob.compareTo(futureDate) >= 0) {
            print("DOB invalid: " + dob + " cannot be today or a future day.");
            return;
        }
        if (!dob.isOver18()) {
            print("Not eligible to open: " + dob + " under 18.");
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
            print("Please select account type");
            return;
        }

        if (accountType.name().equals("MONEY_MARKET") && initialDeposit < 2000) {
            print("Minimum of $2,000 to open a Money Market account.");
            return;
        }

        Branch branch;
        try {
            branch = Branch.valueOf(branchName.toUpperCase());
        } catch (IllegalArgumentException e) {
            print(branchName + " - invalid branch.");
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
                print("Not eligible to open: " + dob + " over 24.");
                return;
            }
        }
        else if(accountType.name().equals("CD")) {
            term = Integer.parseInt(terms.getValue());
            LocalDate dateOpenedValue = dateOpened.getValue();
            open = new Date(dateOpenedValue);
            if (initialDeposit < 1000) {
                print("Minimum of $1,000 to open a Certificate Deposit account.");
                return;
            }
            if (term % 3 != 0 || term > 12 || term <= 0) {
                print(term + " is not a valid term.");
                return;
            }
        }

        if (initialDeposit <= 0) {
            System.out.println("Initial deposit cannot be 0 or negative.");
            return;
        }

        Profile holder = new Profile(firstNameText, lastNameText, dob);
        if (accountDatabase.hasAccountType(holder, accountType)) {
            print(holder.getFname() + " " + holder.getLname() +
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
        print(newAccount.getNumber().getType().name() + " account " +
                newAccount.getNumber() + " has been opened.");

    }
}