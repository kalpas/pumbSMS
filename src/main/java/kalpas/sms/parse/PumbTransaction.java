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

    public Date                date;

    public float               amount;
    public String              currency;
    
    public float               amountInAccountCurrency;
    public String              accountCurrency;

    public float               remaining;
    public String              remainingCurrency;

    public float               remainingAvailable;
    public String              remainingAvailableCurrency;

    public String              atm;

    public String              reasonRejected;
    
    public String              originalMsg;

}
