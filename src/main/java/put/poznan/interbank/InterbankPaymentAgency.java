package put.poznan.interbank;

import put.poznan.Bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterbankPaymentAgency {
    private final HashMap<String, Bank> banks = new HashMap<>();
    private final List<PaymentData> payments = new ArrayList<>();

    public void addBank(Bank bank) {
        if (banks.containsKey(bank.accountsPrefix))
            throw new IllegalArgumentException("Bank with this prefix is already added 😡");
        banks.put(bank.accountsPrefix, bank);
        bank.setInterbankPaymentAgency(this);
    }

    public void addPayment(InterBankPayment payment, Bank bank, Callback callback) {
        payments.add(new PaymentData(payment, bank, callback));
    }

    public void processPayments() {
        payments.forEach(data -> {
            final var receiverBank = banks.get(data.payment.getReceiverAccountNumber().substring(0, 4));
            final var result = receiverBank.processPayment(data.payment);
            switch (result) {
                case COMPLETED -> {
                    data.fromBank.paymentSucceed(data.payment);
                    data.callback.paymentSucceed();
                }
                case INSUFFICIENT_FUND, ACCOUNT_NOT_FOUND -> {
                    data.fromBank.paymentFailed(data.payment);
                    data.callback.paymentFailed(result);
                }
            }
        });
        payments.clear();
    }

    private record PaymentData(
            InterBankPayment payment,
            Bank fromBank,
            Callback callback) {}

    public interface Callback {
        void paymentSucceed();

        void paymentFailed(PaymentStatus error);
    }
}
