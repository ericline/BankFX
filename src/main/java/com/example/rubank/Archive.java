package com.example.rubank;

import util.Date;

/**
 * Archive class implements a linked list in the Archive class,
 * where each node stores a closed Account object. It provides methods to
 * add new accounts to the front of the list and print the list of accounts.
 * @author Eric Lin, Anish Mande
 */
public class Archive {

    private AccountNode first; // Head node of the linked list

    private static class AccountNode {
        private Account account; // Account data
        private Date close; // Date the account was closed
        private AccountNode next; // Link to the next node

        /**
         * Constructor to create a new AccountNode.
         * @param account The Account object to store in the node.
         * @param close The date the account was closed.
         */
        private AccountNode(Account account, Date close) {
            this.account = account;
            this.close = close;
            this.next = null;
        }

        /**
         * Get a string representation of this node's data.
         * @return Formatted string with account data and close date.
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(account.toString()); // Ensure Account has a properly formatted output method
            sb.append(" Closed[").append(close.toString()).append("]");
            if (account.activities != null) {
                sb.append("\n\t[Activity]");
                for (Activity activity : account.activities) {
                    sb.append("\n\t\t").append(activity.toString());
                }
            }
            return sb.toString();
        }
    }

    /**
     * Adds a new account to the front of the linked list.
     * The most recently closed account is always at the front.
     * @param account The Account object to be added to the archive.
     * @param close The date the account was closed.
     */
    public void add(Account account, Date close) {
        AccountNode newNode = new AccountNode(account, close);
        newNode.next = first;
        first = newNode;
    }

    /**
     * Prints the list of archived accounts.
     * The accounts are printed in the order they were added (most recent first).
     */
    public void print() {
        AccountNode current = first;
        while (current != null) {
            System.out.println(current.toString());
            current = current.next;
        }
    }
}