package com.example.rubank;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import util.Date;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.StringTokenizer;

public class Controller {
    private AccountDatabase accountDatabase;
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    @FXML private TextField firstName, lastName, initialBalance, userFirstName, userLastName, userAmount;
    @FXML private DatePicker dob, userDOB;
    @FXML private RadioButton checking, collegeChecking, savings, moneyMarket, userChecking, userCollegeChecking, userSavings, userMoneyMarket;
    @FXML private ToggleGroup accountTypes, userAccountTypes;
    @FXML private TextArea output;

    public void initialize() {
        this.accountDatabase = new AccountDatabase();
    }

    @FXML
    private void handleOpenButtonAction(ActionEvent event) {
        processAccountAction("O");
    }

    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        processAccountAction("C");
    }

    @FXML
    private void handleDepositButton(ActionEvent event) {
        processTransaction("D");
    }

    @FXML
    private void handleWithdrawButton(ActionEvent event) {
        processTransaction("W");
    }

    private void processAccountAction(String command) {
        if (command.equals("O")) {
            if (firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty() || dob.getValue() == null || initialBalance.getText().trim().isEmpty()) {
                output.appendText("Please fill in all required fields.\n");
                return;
            }

            double initialDeposit;
            try {
                initialDeposit = Double.parseDouble(initialBalance.getText().trim());
            } catch (NumberFormatException e) {
                output.appendText("Invalid deposit amount.\n");
                return;
            }

            Date dobObj = new Date(dob.getValue().getMonthValue(), dob.getValue().getDayOfMonth(), dob.getValue().getYear());
            if (!dobObj.isValid()) {
                output.appendText("Invalid date of birth.\n");
                return;
            }
            if (!dobObj.isOver18()) {
                output.appendText("Not eligible to open: " + dobObj + " under 18.\n");
                return;
            }

            Profile profile = new Profile(firstName.getText().trim(), lastName.getText().trim(), dobObj);
            AccountNumber accountNumber = new AccountNumber(accountDatabase.generateAccountNumber());
            Account newAccount = new Checking(accountNumber, profile, initialDeposit);

            if (accountDatabase.hasAccountType(profile, AccountType.CHECKING)) {
                output.appendText(profile.getFname() + " " + profile.getLname() + " already has a Checking account.\n");
                return;
            }

            if (initialDeposit <= 0) {
                output.appendText("Initial deposit cannot be 0 or negative.\n");
                return;
            }

            if (accountDatabase.add(newAccount)) {
                output.appendText("Account opened successfully.\n");
            } else {
                output.appendText("Account already exists.\n");
            }
        } else if (command.equals("C")) {
            Profile profile = new Profile(firstName.getText().trim(), lastName.getText().trim(), new Date(dob.getValue().getMonthValue(), dob.getValue().getDayOfMonth(), dob.getValue().getYear()));
            AccountNumber accountNumber = new AccountNumber(accountDatabase.getAccountNumber(profile));
            Account accountToClose = new Checking(accountNumber, profile, 0);

            if (accountDatabase.close(accountToClose)) {
                output.appendText("Account closed successfully.\n");
            } else {
                output.appendText("Account not found.\n");
            }
        }
    }

    private void processTransaction(String command) {
        String fName = userFirstName.getText().trim();
        String lName = userLastName.getText().trim();
        LocalDate dateOfBirth = userDOB.getValue();
        String amountStr = userAmount.getText().trim();

        if (fName.isEmpty() || lName.isEmpty() || dateOfBirth == null || amountStr.isEmpty()) {
            output.appendText("Please fill in all required fields.\n");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            output.appendText("Invalid amount.\n");
            return;
        }

        Date dob = new Date(dateOfBirth.getMonthValue(), dateOfBirth.getDayOfMonth(), dateOfBirth.getYear());
        Profile profile = new Profile(fName, lName, dob);
        AccountNumber accountNumber = new AccountNumber(accountDatabase.getAccountNumber(profile));
        Account account = new Checking(accountNumber, profile, 0);

        if (command.equals("D")) {
            if (accountDatabase.deposit(account, amount)) {
                output.appendText("Deposit successful.\n");
            } else {
                output.appendText("Account not found.\n");
            }
        } else if (command.equals("W")) {
            int result = accountDatabase.withdraw(account, amount);
            if (result == 2) {
                output.appendText("Withdrawal successful.\n");
            } else if (result == 0) {
                output.appendText("Account not found.\n");
            } else {
                output.appendText("Insufficient funds.\n");
            }
        }
    }
}
