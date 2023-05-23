package put.poznan.transaction;

public enum TransactionType {
    MAKE_PAYMENT,
    RECEIVE_PAYMENT,
    FAILED,
    OPEN_DEPOSIT,
    OPEN_LOAN,
    CHANGE_DEPOSIT_INTEREST_RATE,
    CHANGE_LOAN_INTEREST_RATE,
    CLOSE_DEPOSIT,
    REPAY_LOAN,
    WITHDRAW_MONEY,
}
