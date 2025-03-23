package com.example.rubank;

import util.Date;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TransactionManager {
    private AccountDatabase accountDatabase;
    private static final String ACTIVITIES = "activities.txt";
    private static final String ACCOUNTS = "accounts.txt";
    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public TransactionManager() {
        this.accountDatabase = new AccountDatabase();
    }

    public void run() {
        handleAccounts();
        System.out.println("Transaction Manager is running.");
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            StringTokenizer tokenizer = new StringTokenizer(line);
            String command = tokenizer.nextToken();

            switch (command) {
                case "O":
                    handleOpenAccount(tokenizer);
                    break;
                case "C":
                    handleCloseAccount(tokenizer);
                    break;
                case "D":
                    handleDeposit(tokenizer);
                    break;
                case "W":
                    handleWithdraw(tokenizer);
                    break;
                case "P":
                    pCommand();
                    break;
                case "A":
                    handleActivities();
                    break;
                case "PA":
                    handlePrintArchived();
                    break;
                case "PB":
                    handlePrintByBranch();
                    break;
                case "PH":
                    handlePrintByHolder();
                    break;
                case "PT":
                    handlePrintByType();
                    break;
                case "PS":
                    handlePrintStatements();
                    break;
                case "Q":
                    System.out.println("Transaction Manager is terminated.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command!");
            }
        }
        scanner.close();
    }

    private void handleOpenAccount(StringTokenizer tokenizer) {
        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing account type.");
            return;
        }
        String type = tokenizer.nextToken();

        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing branch name.");
            return;
        }
        String branchName = tokenizer.nextToken();

        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing account holder's first name.");
            return;
        }
        String firstName = tokenizer.nextToken();

        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing account holder's last name.");
            return;
        }
        String lastName = tokenizer.nextToken();

        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing date of birth.");
            return;
        }
        String dobStr = tokenizer.nextToken();

        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing data tokens for opening an account.");
            return;
        }

        String depositToken = tokenizer.nextToken();
        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(depositToken);
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + depositToken + "\" - not a valid amount.");
            return;
        }

        // Validate DOB
        Date dob = new Date(dobStr);
        if (!dob.isValid()) {
            System.out.println("DOB invalid: " + dobStr + " not a valid calendar date!");
            return;
        }
        Date futureDate = new Date("2/17/2025");
        if (dob.compareTo(futureDate) >= 0) {
            System.out.println("DOB invalid: " + dob + " cannot be today or a future day.");
            return;
        }
        if (!dob.isOver18()) {
            System.out.println("Not eligible to open: " + dob + " under 18.");
            return;
        }

        AccountType accountType;
        try {
            if (type.equals("college")) {
                accountType = AccountType.COLLEGE_CHECKING;
            } else if (type.equals("certificate")) {
                accountType = AccountType.CD;
            } else if (type.equals("moneymarket")) {
                accountType = AccountType.MONEY_MARKET;
            } else {
                accountType = AccountType.valueOf(type.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(type + " - invalid account type.");
            return;
        }

        // Validate minimum deposit
        if (accountType.name().equals("MONEY_MARKET") && initialDeposit < 2000) {
            System.out.println("Minimum of $2,000 to open a Money Market account.");
            return;
        }

        Branch branch;
        try {
            branch = Branch.valueOf(branchName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(branchName + " - invalid branch.");
            return;
        }

        Campus campusCode = Campus.NEW_BRUNSWICK;
        int term = 0;
        Date open = new Date("3/2/2024");
        if (accountType.name().equals("COLLEGE_CHECKING")) {
            if (!tokenizer.hasMoreTokens()) {
                System.out.println("Missing data tokens for opening an account.");
                return;
            }
            campusCode = Campus.fromCode(tokenizer.nextToken());

            if (dob.isOver24()) {
                System.out.println("Not eligible to open: " + dob + " over 24.");
                return;
            }
        } else if (accountType.name().equals("CD")) {
            if (!tokenizer.hasMoreTokens()) {
                System.out.println("Missing data tokens for opening an account.");
                return;
            }
            term = Integer.parseInt(tokenizer.nextToken());
            if (!tokenizer.hasMoreTokens()) {
                System.out.println("Missing data tokens for opening an account.");
                return;
            }
            String openDate = tokenizer.nextToken().trim();
            open = new Date(openDate);

            if (initialDeposit < 1000) {
                System.out.println("Minimum of $1,000 to open a Certificate Deposit account.");
                return;
            }
            if (term % 3 != 0 || term > 12 || term <= 0) {
                System.out.println(term + " is not a valid term.");
                return;
            }
        }

        if (initialDeposit <= 0) {
            System.out.println("Initial deposit cannot be 0 or negative.");
            return;
        }

        Profile holder = new Profile(firstName, lastName, dob);

        if (accountDatabase.hasAccountType(holder, accountType)) {
            System.out.println(holder.getFname() + " " + holder.getLname() +
                    " already has a " + accountType.name() + " account.");
            return;
        }

        AccountNumber accountNumber = new AccountNumber(branch, accountType);
        Account newAccount = null;
        switch (type.toUpperCase()) {
            case "CHECKING":
                newAccount = new Checking(accountNumber, holder, initialDeposit);
                break;
            case "SAVINGS":
                newAccount = new Savings(accountNumber, holder, initialDeposit, accountDatabase);
                break;
            case "MONEYMARKET":
                newAccount = new MoneyMarket(accountNumber, holder, initialDeposit, 0, accountDatabase);
                break;
            case "COLLEGE":
                newAccount = new CollegeChecking(accountNumber, holder, initialDeposit, campusCode);
                break;
            case "CERTIFICATE":
                newAccount = new CertificateDeposit(accountNumber, holder, initialDeposit, term, open, accountDatabase);
                break;
        }

        accountDatabase.add(newAccount);
        System.out.println(newAccount.getNumber().getType().name() + " account " +
                newAccount.getNumber() + " has been opened.");
    }

    private void handleCloseAccount(StringTokenizer tokenizer) {
        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing data for closing an account.");
            return;
        }
        Date closeDate = new Date(tokenizer.nextToken());
        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing data for closing an account.");
            return;
        }

        String nextToken = tokenizer.nextToken();

        if (nextToken.matches("\\d{9}")) {
            String accountNumberStr = nextToken;
            AccountNumber accountNumber = new AccountNumber(accountNumberStr);
            Account account = accountDatabase.getAccount(accountNumber);
            if (account == null) {
                System.out.println(accountNumberStr + " account does not exist.");
                return;
            }
            boolean closed = accountDatabase.closeAccount(account, closeDate);
            if (closed) {
                if (account.getNumber().getType().equals(AccountType.CD)) {
                    CertificateDeposit cdAccount = (CertificateDeposit) account;
                    double interest = Math.round(cdAccount.interest(closeDate) * 100.0) / 100.0;
                    System.out.println("--" + account.getNumber() + " interest earned: $" + formatter.format(interest));
                    if (closeDate.compareTo(cdAccount.getmaturityDate()) < 0) {
                        double penalty = cdAccount.calculatePenalty(closeDate);
                        System.out.println("--penalty: $" + formatter.format(penalty));
                    }
                }
                else {
                    System.out.println("Closing account " + accountNumberStr);
                    double interest = Math.round(account.dailyInterest() * closeDate.getDay() * 100.0) / 100.0;
                    System.out.println("--interest earned: $" + formatter.format(interest));
                }
            }
            else {
                System.out.println(accountNumberStr + " account does not exist.");
            }
        }
        else {
                String firstName = nextToken;
                String lastName = tokenizer.nextToken();
                Date dob = new Date(tokenizer.nextToken());
                Profile profile = new Profile(firstName, lastName, dob);
                boolean found = false;
                for (int i = accountDatabase.size() - 1; i >= 0; i--) {
                    Account acc = accountDatabase.get(i);
                    if (acc.getHolder().equals(profile)) {
                        if (!found) {
                            System.out.println("Closing accounts for " + profile);
                            found = true;
                        }
                        if (acc.getNumber().getType().equals(AccountType.CD)) {
                            CertificateDeposit cdAccount = (CertificateDeposit) acc;
                            double interest = Math.round(cdAccount.interest(closeDate) * 100.0) / 100.0;
                            System.out.println("--" + acc.getNumber() + " interest earned: $" + formatter.format(interest));
                            if (closeDate.compareTo(cdAccount.getmaturityDate()) < 0) {
                                double penalty = cdAccount.calculatePenalty(closeDate);
                                System.out.println("  [penalty] $" + formatter.format(penalty));
                            }
                        }
                        else {
                            double interest = Math.round(acc.dailyInterest() * closeDate.getDay() * 100.0) / 100.0;
                            System.out.println("--" + acc.getNumber() + " interest earned: $" + formatter.format(interest));
                        }
                        accountDatabase.closeAccount(acc, closeDate);
                    }
                }
            if (!found) {
                System.out.println(profile + " does not have any accounts in the database.");
            }
            else {
                System.out.println("All accounts for " + profile + " are closed and moved to archive.");
            }
        }
    }

    private void handleDeposit(StringTokenizer tokenizer) {
        String accountNumberStr = tokenizer.nextToken();

        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing data tokens for the deposit.");
            return;
        }
        String depositAmount = tokenizer.nextToken();

        double amount;
        try {
            amount = Double.parseDouble(depositAmount);
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + depositAmount + "\" - not a valid amount.");
            return;
        }

        if (amount <= 0) {
            System.out.println(amount + " - deposit amount cannot be 0 or negative.");
            return;
        }

        boolean deposit = accountDatabase.deposit(accountNumberStr, amount);
        if (!deposit) {
            System.out.println(accountNumberStr + " does not exist.");
            return;
        }

        System.out.println("$" + formatter.format(amount) + " deposited to " + accountNumberStr);
    }

    private void handleWithdraw(StringTokenizer tokenizer) {
        String accountNumberStr = tokenizer.nextToken();

        if (!tokenizer.hasMoreTokens()) {
            System.out.println("Missing data tokens for the withdrawal.");
            return;
        }
        String withdrawAmount = tokenizer.nextToken();

        double amount;
        try {
            amount = Double.parseDouble(withdrawAmount);
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + withdrawAmount + "\" - not a valid amount.");
            return;
        }

        if (amount <= 0) {
            System.out.println(amount + " withdrawal amount cannot be 0 or negative.");
            return;
        }

        AccountNumber accountNumber = new AccountNumber(accountNumberStr);
        Account account = accountDatabase.getAccount(accountNumber);
        if (account == null) {
            System.out.println(accountNumberStr + " does not exist.");
            return;
        }
        double previousBalance = account.getBalance(); // Store balance before withdrawal

        boolean success = accountDatabase.withdraw(accountNumberStr, amount);
        if (success) {
            double newBalance = account.getBalance(); // Store balance after withdrawal

            if (account instanceof MoneyMarket) {
                if (previousBalance < 2000 || newBalance < 2000) {
                    System.out.println(accountNumberStr + " balance below $2,000 - $"
                            + formatter.format(amount) + " withdrawn from " + accountNumberStr);
                } else {
                    System.out.println("$" + formatter.format(amount) + " withdrawn from " + accountNumberStr);
                }
            } else {
                System.out.println("$" + formatter.format(amount) + " withdrawn from " + accountNumberStr);
            }
        }
        else {
            if (previousBalance < 2000) {
                System.out.print(accountNumberStr + " balance below $2,000 - ");
            }
            System.out.print("withdrawing $" + formatter.format(amount));
            System.out.println(" - insufficient funds.");
        }
    }
    private void pCommand() {
        System.out.println("P command is deprecated!");
    }
    private void handleActivities() {
        System.out.println("Processing \"activities.txt\"...");
        File file = new File(ACTIVITIES);
        try {
            accountDatabase.processActivities(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAccounts() {
        File file = new File(ACCOUNTS);
        try {
            accountDatabase.loadAccounts(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePrintArchived() {
        System.out.println("*List of closed accounts in the archive.");
        accountDatabase.printArchive();
        System.out.println("*end of list.");
    }
    private void handlePrintByBranch() {
        System.out.println();
        System.out.println("*List of accounts ordered by branch location (county, city).");
        accountDatabase.printSortedAccounts('B');
        System.out.println("*end of list.");
        System.out.println();
    }
    private void handlePrintByHolder() {
        System.out.println();
        System.out.println("*List of accounts ordered by account holder and number.");
        accountDatabase.printSortedAccounts('H');
        System.out.println("*end of list.");
        System.out.println();
    }
    private void handlePrintByType() {
        System.out.println();
        System.out.println("*List of accounts ordered by account type and number.");
        accountDatabase.printSortedAccounts('T');
        System.out.println("*end of list.");
        System.out.println();
    }
    private void handlePrintStatements() {
        System.out.println();
        System.out.println("*Account statements by account holder.");
        accountDatabase.printStatements();
        System.out.println("*end of statements.");
        System.out.println();
    }
}