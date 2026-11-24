package Q1;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Console ATM simulation for assignment demonstration.
 */
public class ATMSystem {
    private double balance;

    public ATMSystem(double openingBalance) {
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        this.balance = openingBalance;
    }

    public void deposit(String amountText) throws InvalidAmountException, NumberFormatException {
        double amount = parseAmount(amountText);
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }
        balance += amount;
    }

    public void withdraw(String amountText)
            throws InvalidAmountException, InsufficientFundsException, NumberFormatException {
        double amount = parseAmount(amountText);
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Attempted withdrawal " + amount + " exceeds balance " + balance);
        }
        balance -= amount;
    }

    private double parseAmount(String amountText) throws NumberFormatException {
        if (amountText == null || amountText.trim().isEmpty()) {
            throw new NumberFormatException("Amount is empty");
        }
        return Double.parseDouble(amountText.trim());
    }

    public double getBalance() {
        return balance;
    }

    public static final class InvalidAmountException extends Exception {
        public InvalidAmountException(String message) {
            super(message);
        }
    }

    public static final class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        ATMSystem atm = new ATMSystem(1000.0);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n1. Deposit  2. Withdraw  3. Balance  4. Exit");
            System.out.print("Choose option: ");
            String optionText = scanner.nextLine();

            try {
                int option = Integer.parseInt(optionText);
                switch (option) {
                    case 1:
                        System.out.print("Enter deposit amount: ");
                        atm.deposit(scanner.nextLine());
                        System.out.printf("Deposit successful. Balance: %.2f%n", atm.getBalance());
                        break;
                    case 2:
                        System.out.print("Enter withdrawal amount: ");
                        atm.withdraw(scanner.nextLine());
                        System.out.printf("Withdrawal successful. Balance: %.2f%n", atm.getBalance());
                        break;
                    case 3:
                        System.out.printf("Current balance: %.2f%n", atm.getBalance());
                        break;
                    case 4:
                        running = false;
                        break;
                    default:
                        throw new InvalidAmountException("Menu option must be from 1 to 4");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERROR_ATM_001: A numeric value is required.");
            } catch (InsufficientFundsException e) {
                System.out.println("ERROR_ATM_002: " + e.getMessage());
            } catch (InvalidAmountException e) {
                System.out.println("ERROR_ATM_003: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("ERROR_ATM_004: Unexpected runtime error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
