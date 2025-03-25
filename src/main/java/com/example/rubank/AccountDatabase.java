package com.example.rubank;

import util.Date;
import util.List;
import util.Sort;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * AccountDatabase class implements a list for Account objects.
 * Grows dynamically and supports account operations.
 * @author Eric Lin Anish Mande
 */

public class AccountDatabase extends List<Account> {
    private Archive archive;
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#,##0.00");

    public AccountDatabase() {
        this.archive = new Archive();
    }
    //print closed accounts
    public void printArchive() {
        if (archive != null) {
            archive.print();
        }
    }

    //print account statements
    public void printStatements(Controller controller) {
        Sort.account(this, 'H');
        int count = 1;
        Profile previous = null;
        for (Account account : this) {
            if (previous == null || !previous.equals(account.getHolder())) {
                controller.println(count + "." + account.getHolder().getFname()
                        + " " + account.getHolder().getLname() + " " + account.getHolder().getDob());
                count++;
            }
            controller.println("\t[Account#] " + account.getNumber().toString());
            account.statement(controller);
            controller.println("\n");

            previous = account.getHolder();

        }
    }

    public Account getAccount(AccountNumber accountNumber) {
        for (Account account : this) {
            if (account.getNumber().toString().equals(accountNumber.toString())) {
                return account;
            }
        }
        return null;
    }

    public void loadAccounts(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] tokens = line.split(",");
            AccountType type = AccountType.CHECKING;
            if (tokens[0].equalsIgnoreCase("college")) {
                type = AccountType.valueOf("COLLEGE_CHECKING");
            } else if (tokens[0].equalsIgnoreCase("certificate")) {
                type = AccountType.valueOf("CD");
            } else if (tokens[0].equalsIgnoreCase("moneymarket")) {
                type = AccountType.valueOf("MONEY_MARKET");
            } else {
                try {
                    type = AccountType.valueOf(tokens[0].toUpperCase());
                }
                catch(Exception e) {
                    System.out.println("Invalid file: ");
                }
            }
            Branch branch = Branch.valueOf(tokens[1].toUpperCase());
            Date dob = new Date(tokens[4]);
            Profile holder = new Profile(tokens[2], tokens[3], dob);
            double balance = Double.parseDouble(tokens[5]);
            AccountNumber number = new AccountNumber(branch, type);
            Account account = null;
            switch (type.name()) {
                case "CHECKING":
                    account = new Checking(number, holder, balance);
                    break;
                case "SAVINGS":
                    account = new Savings(number, holder, balance, this);
                    break;
                case "MONEY_MARKET":
                    account = new MoneyMarket(number, holder, balance, 0, this);
                    break;
                case "COLLEGE_CHECKING":
                    Campus campus = Campus.fromCode(tokens[6]);
                    account = new CollegeChecking(number, holder, balance, campus);
                    break;
                case "CD":
                    int term = Integer.parseInt(tokens[6]);
                    Date open = new Date(tokens[7]);
                    account = new CertificateDeposit(number, holder, balance, term, open, this);
                    break;
            }
            if (account != null) {
                this.add(account);
            } else {
                System.out.println("Error: Account creation failed for " + type.name() + " at branch " + branch.name());
            }
        }
        scanner.close();
        System.out.println("Accounts in \"" + file.getName() + "\" loaded to the database.");
    }

    public void processActivities(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] tokens = line.split(",");

            char type = tokens[0].charAt(0);
            String accountNumber = tokens[1];
            Date date = new Date(tokens[2]);
            Branch branch = Branch.valueOf(tokens[3].toUpperCase());
            double amount = Double.parseDouble(tokens[4]);

            Account foundAccount = null;
            for (Account account : this) { // Iterate over accounts
                if (account.getNumber().toString().equals(accountNumber)) {
                    foundAccount = account;
                    break;
                }
            }
            if (foundAccount == null) {
                System.out.println("Account " + accountNumber + " not found.");
                continue;
            }

            Activity activity = new Activity(date, branch, type, amount, true);

            String transactionType = "deposit";
            if (type == 'W')
                transactionType = "withdrawal";

            if (transactionType.equals("deposit")) {
                foundAccount.deposit(amount);
            } else if (transactionType.equals("withdrawal")) {
                foundAccount.withdraw(amount);
            }

            foundAccount.addActivity(activity);

            System.out.println(accountNumber + "::" + activity.toString());
        }
        System.out.println("Account activities in \"" + file.getName() + "\" processed.");
    }

    public void printSortedAccounts(char key) {
        if (this.isEmpty()) {
            System.out.println("Account database is empty.");
            return;
        }

        // Sort accounts based on the given key
        Sort.account(this, key);

        // Print accounts in sorted order
        if (key == 'H') {
            for (Account account : this) {
                System.out.println(account.toString());
            }
        } else if (key == 'T') {
            AccountType currentType = null;

            for (Account account : this) {
                if (account == null) continue;

                AccountType accountType = account.getNumber().getType();
                if (currentType == null || !currentType.equals(accountType)) {
                    System.out.println("Account Type: " + accountType);
                    currentType = accountType;
                }
                System.out.println(account);
            }
        } else if (key == 'B') {
            String currentCounty = null;

            for (Account account : this) {
                if (account == null) continue;

                Branch branch = account.getNumber().getBranch();
                String county = branch.getCounty();
                if (currentCounty == null || !currentCounty.equalsIgnoreCase(county)) {
                    System.out.println("County: " + county);
                    currentCounty = county;
                }
                System.out.println(account.toString());
            }
        }
    }

    /**
     * Checks if the holder already has an account of the given type.
     * Ensures only one of each account type per customer.
     *
     * @param holder The profile of the account holder.
     * @param type   The type of account to check for.
     * @return true if the holder already has an account of this type, false otherwise.
     */
    public boolean hasAccountType(Profile holder, AccountType type) {
        if (type == AccountType.CD) {
            return false;
        }
        for (Account account : this) {
            if (account.getHolder().equals(holder) && account.getNumber().getType() == type) {
                return true;
            }
        }
        return false;
    }

    public boolean deposit(String number, double amount) {
        for (Account account : this) {
            if (account.getNumber().toString().equals(number)) {
                account.deposit(amount);
                Date today = new Date();
                Activity deposit = new Activity(today, account.getNumber().getBranch(), 'D', amount, false);
                account.addActivity(deposit);
                return true;
            }
        }
        return false;
    }

    public boolean withdraw(String number, double amount) {
        for (Account account : this) {
            if (account.getNumber().toString().equals(number)) {
                if (amount > 0 && account.getBalance() >= amount) {
                    account.withdraw(amount);
                    Date today = new Date();
                    Activity withdraw = new Activity(today, account.getNumber().getBranch(), 'W', amount, false);
                    account.addActivity(withdraw);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean closeAccount(Account account, Date close) {
        if (!this.contains(account)) {
            return false;
        }
        if (account.getNumber().getType().equals(AccountType.CHECKING))
        {
            for (Account acc : this) {
                if (acc.getHolder().equals(account.getHolder())
                    && acc.getNumber().getType().equals(AccountType.SAVINGS)) {
                    ((Savings) acc).setLoyal(false);
                }
            }
        }
        this.remove(account);
        archive.add(account, close);
        return true;
    }
}
