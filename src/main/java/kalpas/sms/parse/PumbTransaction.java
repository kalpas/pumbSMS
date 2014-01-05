package kalpas.sms.parse;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;

public class PumbTransaction {

    public enum PumbTransactionType {

        BLOCKED("ZABLOKOVANO", "HOLD"), DEBITED("SPYSANNIA", "DEBIT"), CREDITED("NADHODZHENNIA", "CREDIT"), REJECTED(
                "VIDMOVA");

        private List<String> typeNames;

        private PumbTransactionType(String... types) {
            typeNames = Lists.newArrayList(types);
        }

        public static PumbTransactionType forName(String typeName) {
            for (PumbTransactionType type : PumbTransactionType.values()) {
                if (type.typeNames.contains(typeName)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Illegal transaction type: " + typeName);
        }

    }

    public PumbTransactionType type;

    public int                 card;

    public DateTime            date;

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
