package kalpas.sms.parse;

import java.util.Date;

public class PumbTransaction {

    public enum PumbTransactionType {

        BLOCKED("ZABLOKOVANO"), DEBITED("SPYSANNIA"), CREDITED("NADHODZHENNIA");

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

    public float               remaining;

    public float               remainingAvailable;

}
