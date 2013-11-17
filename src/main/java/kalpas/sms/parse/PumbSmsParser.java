package kalpas.sms.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kalpas.sms.parse.PumbTransaction.PumbTransactionType;

import org.apache.commons.lang3.StringUtils;

public class PumbSmsParser {

    // @formatter:off
    private Pattern p = Pattern
                              .compile("(RAKHUNOK\\s)?"
                                      + "111\\s"
                                      + "(\\*\\d{4}\\s)?" //card number, group 2
                                      + "([ 0-9:-]{19})\\s" //date, group 3
                                      + "(("
                                          + "([a-zA-Z]*)\\s"// type of transaction, group 6
                                          + "([0-9.]*)\\s([A-Z]{3})\\s"// amount and currency of money involved in transaction, group 7 & 8
                                          + "((.*)\\s)?"// ATM/TERMINAL id, group 10
                                          + "(\\(?DOSTUPNO\\s)"
                                          + "([0-9.]*)\\s([A-Z]{3})\\)?\\s?"// available amount and currency, group 12 & 13
                                          + "(Z\\s*NYH\\s*VLASNYH\\s*KOSHTIV\\s*" + "([0-9.]*)\\s([A-Z]{3}))?\\s?"// actually available, group 15 & 16
                                      + ")|("
                                          + "\\("
                                          + "([0-9.]*)\\s([A-Z]{3})\\s"// amount and currency asked , group 18 & 19
                                          + "(.*)\\)\\s"//ATM/TERMINAL id, group 20
                                          + "(VIDMOVA):\\s" //REJECTED, group 21
                                          + "(.*)" // reason for rejected transaction, group 22
                                      + "))");
                                      

    // @formatter:on

    public PumbTransaction parsePumbSms(String msg) {
        Matcher m = p.matcher(msg);
        PumbTransaction pumbTransaction = null;
        if (m.matches()) {
            pumbTransaction = new PumbTransaction();
            if (!StringUtils.isEmpty(m.group(6))) {
                pumbTransaction.transactionType = PumbTransactionType.forName(m.group(6));
                pumbTransaction.amount = Float.valueOf(m.group(7));
                pumbTransaction.currency = m.group(8);
                pumbTransaction.atm = m.group(10);

                pumbTransaction.remaining = Float.valueOf(m.group(12));
                pumbTransaction.remainingCurrency = m.group(13);

                if (!StringUtils.isEmpty(m.group(15))) {
                    pumbTransaction.remainingAvailable = Float.valueOf(m.group(15));
                    pumbTransaction.remainingAvailableCurrency = m.group(16);
                }

            } else if (!StringUtils.isEmpty(m.group(21))) {
                pumbTransaction.transactionType = PumbTransactionType.forName(m.group(21));// transaction
                                                                                           // rejected

                pumbTransaction.amount = Float.valueOf(m.group(18));
                pumbTransaction.currency = m.group(19);
                pumbTransaction.atm = m.group(20);
                pumbTransaction.reasonRejected = m.group(22);

            } else {
                throw new IllegalArgumentException("unknown type of transaction");
            }

            return pumbTransaction;
        }
        return pumbTransaction;
    }

    // for (int i = 0; i <= m.groupCount(); i++) {
    // System.out.println("group " + i + " " + m.group(i));
    // }
}
