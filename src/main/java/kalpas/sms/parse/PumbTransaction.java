package kalpas.sms.parse;

import java.util.Date;

public class PumbTransaction {

    public enum PumbTransactionType {

        BLOCKED("ZABLOKOVANO"), DEBITED("SPYSANNIA"), CREDITED("NADHODZHENNIA"), REJECTED("VIDMOVA");

        private String typeName;

        private PumbTransactionType(String type) {
            typeName = type;
        }

        public static PumbTransactionType forName(String typeName) {
            for (PumbTransactionType type : PumbTransactionType.values()) {
                if (type.typeName.equals(typeName)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Illegal transaction type: " + typeName);
        }

    }

    public PumbTransactionType transactionType;

    public int                 card;

    public Date                date;

    public Double              amount;
    public String              currency;

    public Double              amountInAccountCurrency;
    public String              accountCurrency;

    public Double              remaining;
    public String              remainingCurrency;

    public Double              remainingAvailable;
    public String              remainingAvailableCurrency;

    public String              recipient;

    public String              reasonRejected;

    public String              originalMsg;

}
