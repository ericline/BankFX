## Bank Account Transaction Manager

This Java-based application simulates a simplified banking system that manages various types of personal accounts (e.g., Checking, College Checking, Savings, Money Market, Certificate of Deposit). The system is displayed through a JavaFX GUI and supports core banking functionalities such as:

- Opening/Closing Accounts

- Deposits and Withdrawals

- Transaction History Tracking

- Monthly Statement Generation

- Interest and Fee Calculation

### Features

- Open and close accounts (by account number or profile)
- Perform deposits and withdrawals with location tagging
- Track transaction history with detailed timestamps
- Generate formatted monthly statements
- Automatically calculate and apply interest and fees

### Technical Design

- Utilizes an `Account` superclass and polymorphic subclasses for each account type
- Centralized `AccountDatabase` stores active and archived accounts
- `Activity` tracking system records all transactions
- Defensive input validation for dates, balances, and account types
- Edge case handling for:
  - Age-restricted college accounts
  - Minimum deposit enforcement for CDs
  - Withdrawal limits for money market accounts

### Summary

This project demonstrates modular design, object-oriented principles, and robust input handling in a real-world banking simulation. It showcases how to build scalable systems with clear data flow and structured class hierarchies.
