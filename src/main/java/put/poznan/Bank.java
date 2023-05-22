package put.poznan;

import put.poznan.account.Account;
import put.poznan.account.ReceivePayment;
import put.poznan.interbank.InterBankPayment;
import put.poznan.interbank.InterbankPaymentAgency;
import put.poznan.interbank.PaymentStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank {
    /**
     * 4 characters long prefix of account numbers in this bank
     */
    public final String accountsPrefix;
    private final List<Account> accounts = new ArrayList<>();
    private InterbankPaymentAgency interbankPaymentAgency;
    private int idCounter = 0;

    public Bank(String accountsPrefix) {
        if (accountsPrefix.length() != 4)
            throw new IllegalArgumentException("Prefix must be 4 characters long 😡");
        this.accountsPrefix = accountsPrefix;
    }

    public PaymentStatus processPayment(InterBankPayment payment) {
        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(payment.getReceiverAccountNumber()))
                .findFirst()
                .map(
                        account -> {
                            final var receivePayment = new ReceivePayment(payment.getSenderAccount().getAccountNumber(), account, payment.getAmount());
                            receivePayment.execute();
                            final var newStatus = PaymentStatus.COMPLETED;
                            payment.setStatus(newStatus);
                            return newStatus;
                        })
                .orElseGet(() -> {
                            final var newStatus = PaymentStatus.ACCOUNT_NOT_FOUND;
                            payment.setStatus(newStatus);
                            return newStatus;
                        }
                );
    }

    public InterbankPaymentAgency getInterbankPaymentAgency() {
        return interbankPaymentAgency;
    }

    public void setInterbankPaymentAgency(InterbankPaymentAgency interbankPaymentAgency) {
        this.interbankPaymentAgency = interbankPaymentAgency;
    }

    public void paymentSucceed(InterBankPayment payment) {

    }

    public void paymentFailed(InterBankPayment payment) {
        Account senderAccount = payment.getSenderAccount();
        senderAccount.setBalance(senderAccount.getBalance().add(payment.getAmount()));
    }

    public boolean hasAccount(String accountNumber) {
        return accounts.stream()
                .anyMatch(account -> account.getAccountNumber().equals(accountNumber));
    }

    public void addAccount(Account account) {
        if (hasAccount(account.getAccountNumber()))
            throw new IllegalArgumentException("Account with this number is already added 😡");
        if (!account.getAccountNumber().startsWith(accountsPrefix))
            throw new IllegalArgumentException("Account number must start with bank prefix 😡");
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public String getNextId() {
        final var id = idCounter;
        idCounter++;
        return String.format("%s%08d", accountsPrefix, id);
    }
}