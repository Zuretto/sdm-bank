package put.poznan.reporter;

import put.poznan.account.*;

public class XMLReporter implements Visitor {
    public String export(Account... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
        sb.append("<accounts>" + "\n");
        for (Account account : args) {
            sb.append(account.accept(this)).append("\n");
        }
        sb.append("</accounts>");
        return sb.toString();
    }

    public String visitAccount(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("    <account>" + "\n");
        sb.append("        <balance>").append(account.getBalance()).append("</balance>").append("\n");
        sb.append(account.getPerson().accept(this)).append("\n");
        sb.append("        <deposits>" + "\n");
        for (Deposit deposit : account.getDeposits()) {
            sb.append(deposit.accept(this)).append("\n");
        }
        sb.append("        </deposits>" + "\n");
        sb.append("        <loans>" + "\n");
        for (Loan loan : account.getLoans()) {
            sb.append(loan.accept(this)).append("\n");
        }
        sb.append("        </loans>" + "\n");
        sb.append("        <payments>" + "\n");
        for (Object transaction : account.getHistoryOfTransactions().getTransactions()) {
            if (transaction instanceof MakePayment) {
                sb.append(((MakePayment) transaction).accept(this)).append("\n");
            }
            if (transaction instanceof ReceivePayment) {
                sb.append(((ReceivePayment) transaction).accept(this)).append("\n");
            }
        }
        sb.append("        </payments>" + "\n");
        sb.append("    </account>");
        return sb.toString();
    }

    @Override
    public String visitLoan(Loan loan) {
        return "            <loan>" + "\n" +
                "                <amount>" + loan.getAmount() + "</amount>" + "\n" +
                "                <startDate>" + loan.getStartDate() + "</startDate>" + "\n" +
                "                <endDate>" + loan.getEndDate() + "</endDate>" + "\n" +
                "            </loan>";
    }

    @Override
    public String visitDeposit(Deposit deposit) {
        return "            <deposit>" + "\n" +
                "                <amount>" + deposit.getAmount() + "</amount>" + "\n" +
                "                <startDate>" + deposit.getStartDate() + "</startDate>" + "\n" +
                "                <endDate>" + deposit.getEndDate() + "</endDate>" + "\n" +
                "            </deposit>";
    }

    @Override
    public String visitPerson(Person person) {
        return "        <owner>" + "\n" +
                "            <name>" + person.getName() + "</name>" + "\n" +
                "            <phoneNumber>" + person.getPhoneNumber() + "</phoneNumber>" + "\n" +
                "            <email>" + person.getEmail() + "</email>" + "\n" +
                "        </owner>";
    }

    @Override
    public String visitMakePayment(MakePayment payment) {
        return  "           <payment>" + "\n" +
                "               <sender>" + payment.getSenderAccount().getAccountNumber() + "</sender>" + "\n" +
                "               <receiver>" + payment.getReceiverAccountNumber()+ "</phoneNumber>" + "\n" +
                "               <amount>" + payment.getAmount() + "</email>" + "\n" +
                "               <status>" + payment.getStatus() + "</status>" + "\n" +
                "           </payment>";
    }

    @Override
    public String visitReceivePayment(ReceivePayment payment) {
        return  "           <payment>" + "\n" +
                "               <sender>" + payment.getFromAccountNumber() + "</sender>" + "\n" +
                "               <receiver>" + payment.getToAccount().getAccountNumber() + "</phoneNumber>" + "\n" +
                "               <amount>" + payment.getAmount() + "</email>" + "\n" +
                "           </payment>";
    }

}
