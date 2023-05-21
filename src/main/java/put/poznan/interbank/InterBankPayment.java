package put.poznan.interbank;

import put.poznan.account.Account;

import java.math.BigDecimal;

public class InterBankPayment {
    private String receiverAccountNumber;
    private Account senderAccount;
    private BigDecimal amount;
    private PaymentStatus status = PaymentStatus.PENDING;

    public InterBankPayment(String receiverAccountNumber, Account senderAccount, BigDecimal amount) {
        this.receiverAccountNumber = receiverAccountNumber;
        this.senderAccount = senderAccount;
        this.amount = amount;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(Account senderAccount) {
        this.senderAccount = senderAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

}
