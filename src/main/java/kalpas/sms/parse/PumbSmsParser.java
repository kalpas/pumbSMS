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
                                          + "(\\(U\\s*VALIUTI\\s*RAKHUNKU\\s*([0-9.]*)\\s([A-Z]{3})\\)\\s*)?"//in account currency, group 10 & 11
                                          + "((.*)\\s)?"// ATM/TERMINAL id, group 13
                                          + "(\\(?DOSTUPNO\\s)"
                                          + "([0-9.]*)\\s([A-Z]{3})\\)?\\s?"// available amount and currency, group 15 & 16
                                          + "(Z\\s*NYH\\s*VLASNYH\\s*KOSHTIV\\s*" + "([0-9.]*)\\s([A-Z]{3}))?\\s?"// actually available, group 18 & 19
                                      + ")|("
                                          + "\\("
                                          + "([0-9.]*)\\s([A-Z]{3})\\s"// amount and currency asked , group 21 & 22
                                          + "(.*)\\)\\s"//ATM/TERMINAL id, group 23
                                          + "(VIDMOVA):\\s" //REJECTED, group 24
                                          + "(.*)" // reason for rejected transaction, group 25
                                      + "))");
                                      

    // @formatter:on

    public PumbTransaction parsePumbSms(String msg) {
        Matcher m = p.matcher(msg);
        PumbTransaction pumbTransaction = null;
        if (m.matches()) {
            pumbTransaction = new PumbTransaction();
            pumbTransaction.originalMsg = m.group();
            if (!StringUtils.isEmpty(m.group(6))) {
                pumbTransaction.transactionType = PumbTransactionType.forName(m.group(6));
                pumbTransaction.amount = Float.valueOf(m.group(7));
                pumbTransaction.currency = m.group(8);
                pumbTransaction.atm = m.group(13);

                if (!StringUtils.isEmpty(m.group(10))) {
                    pumbTransaction.amountInAccountCurrency = Float.parseFloat(m.group(10));
                    pumbTransaction.accountCurrency = m.group(11);
                }

                pumbTransaction.remaining = Float.valueOf(m.group(15));
                pumbTransaction.remainingCurrency = m.group(16);

                if (!StringUtils.isEmpty(m.group(15))) {
                    pumbTransaction.remainingAvailable = Float.valueOf(m.group(15));
                    pumbTransaction.remainingAvailableCurrency = m.group(16);
                }

            } else if (!StringUtils.isEmpty(m.group(21))) {
                pumbTransaction.transactionType = PumbTransactionType.forName(m.group(24));// transaction
                                                                                           // rejected

                pumbTransaction.amount = Float.valueOf(m.group(21));
                pumbTransaction.currency = m.group(22);
                pumbTransaction.atm = m.group(23);
                pumbTransaction.reasonRejected = m.group(25);

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
