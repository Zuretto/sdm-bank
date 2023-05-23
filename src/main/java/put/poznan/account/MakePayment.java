package put.poznan.account;

import put.poznan.Bank;
import put.poznan.interbank.InterBankPayment;
import put.poznan.interbank.InterbankPaymentAgency;
import put.poznan.interbank.PaymentStatus;
import put.poznan.transaction.Transaction;
import put.poznan.transaction.TransactionType;

import java.math.BigDecimal;

public class MakePayment extends Transaction {
    private final Bank bank;
    private final Account senderAccount;
    private final BigDecimal amount;
    private final String receiverAccountNumber;
    private final InterbankPaymentAgency interbankPaymentAgency;
    private PaymentStatus status = PaymentStatus.PENDING;

    public Account getSenderAccount() {
        return senderAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public MakePayment(Bank senderBank, Account senderAccount, BigDecimal amount, String receiverAccountNumber, InterbankPaymentAgency interbankPaymentAgency) {
        super(senderAccount.getHistoryOfTransactions());
        this.bank = senderBank;
        this.senderAccount = senderAccount;
        this.amount = amount;
        this.receiverAccountNumber = receiverAccountNumber;
        this.interbankPaymentAgency = interbankPaymentAgency;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.MAKE_PAYMENT;
    }

    @Override
    public String getDescription() {
        return String.format("Transaction to make payment from sender: %s to receiver: %s with amount: %s",
                senderAccount,
                receiverAccountNumber,
                amount);
    }

    @Override
    protected void executeImplementation() {
        if (!bank.hasAccount(senderAccount.getAccountNumber())) {
            status = PaymentStatus.ACCOUNT_NOT_FOUND;
            return;
        }
        if (!senderAccount.hasFunds(amount)) {
            status = PaymentStatus.INSUFFICIENT_FUND;
            return;
        }
        if (receiverAccountNumber.startsWith(bank.accountsPrefix)) {
            makeInnerBankPayment();
        } else {
            makeInterBankPayment();
        }
    }

    private void makeInterBankPayment() {
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        interbankPaymentAgency.addPayment(
                new InterBankPayment(receiverAccountNumber, senderAccount, amount),
                bank,
                new InterbankPaymentAgency.Callback() {
                    @Override
                    public void paymentSucceed() {
                        status = PaymentStatus.COMPLETED;
                    }

                    @Override
                    public void paymentFailed(PaymentStatus error) {
                        status = error;
                    }
                });
    }

    private void makeInnerBankPayment() {
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        bank.getAccounts().stream()
                .filter(account -> account.getAccountNumber().equals(receiverAccountNumber))
                .findFirst()
                .ifPresentOrElse(
                        account -> {
                            final var receivePayment =  new ReceivePayment(
                                    senderAccount.getAccountNumber(),
                                    account,
                                    amount
                            );
                            receivePayment.execute();
                            status = PaymentStatus.COMPLETED;
                        },
                        () -> status = PaymentStatus.ACCOUNT_NOT_FOUND
                );
    }
}
