package kalpas.sms.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kalpas.sms.parse.PumbTransaction.PumbTransactionType;

import org.apache.commons.lang3.StringUtils;

public class PumbSmsParserUA extends AbstractPumbSmsParser implements PumbSmsParser {

    // @formatter:off
    private Pattern transactionSmsPattern = Pattern
                              .compile("(RAKHUNOK\\s)?"
                                      + "111\\s"
                                      + "\\*?(\\d{4})?\\s?" //card number, group 2
                                      + "([ 0-9:-]{19})\\s" //date, group 3
                                      + "(("
                                          + "(OTMENA\\sOPERATSII\\s)?"//transaction rolled back
                                          + "([a-zA-Z]*)\\s"// type of transaction, group 7
                                          + "([0-9.]*)\\s([A-Z]{3})\\s"// amount and currency of money involved in transaction, group 8 & 9
                                          + "(\\(U\\s*VALIUTI\\s*RAKHUNKU\\s*([0-9.]*)\\s([A-Z]{3})\\)\\s*)?"//in account currency, group 11 & 12
                                          + "((.*)\\s)?"// ATM/TERMINAL id, group 14
                                          + "(\\(?DOSTUPNO\\s)"
                                          + "([0-9.-]*)\\s([A-Z]{3})\\)?\\s?"// available amount and currency, group 16 & 17
                                          + "(Z\\s*NYH\\s*VLASNYH\\s*KOSHTIV\\s*" + "([0-9.-]*)\\s([A-Z]{3}))?\\s*"// actually available, group 19 & 20
                                      + ")|("
                                          + "\\("
                                          + "([0-9.-]*)\\s([A-Z]{3})\\s"// amount and currency asked , group 22 & 23
                                          + "(.*)\\)\\s"//ATM/TERMINAL id, group 24
                                          + "(VIDMOVA):\\s" //REJECTED, group 25
                                          + "(.*)" // reason for rejected transaction, group 26
                                      + "))");
                                      

    // @formatter:on

    public PumbTransaction parsePumbSms(String msg) {
        Matcher m = transactionSmsPattern.matcher(msg);
        if (m.find()) {

            // for (int i = 0; i <= m.groupCount(); i++) {
            // System.out.println("group " + i + " " + m.group(i));
            // }

            PumbTransaction pumbTransaction = new PumbTransaction();
            pumbTransaction.originalMsg = m.group();

            pumbTransaction.card = fetchCardNumber(m, 2);

            pumbTransaction.date = fetchDate(m, 3);

            if (!StringUtils.isEmpty(m.group(6))) {
                pumbTransaction.rolledBack = true;
            }

            if (!StringUtils.isEmpty(m.group(7))) {
                pumbTransaction.type = PumbTransactionType.forName(m.group(7));
                pumbTransaction.amount = Double.valueOf(m.group(8));
                pumbTransaction.currency = m.group(9);
                pumbTransaction.recipient = m.group(14);

                if (!StringUtils.isEmpty(m.group(11))) {
                    pumbTransaction.amountInAccountCurrency = Double.parseDouble(m.group(11));
                    pumbTransaction.accountCurrency = m.group(12);
                }

                pumbTransaction.remaining = Double.valueOf(m.group(16));
                pumbTransaction.remainingCurrency = m.group(17);

                if (!StringUtils.isEmpty(m.group(19))) {
                    pumbTransaction.remainingAvailable = Double.valueOf(m.group(19));
                    pumbTransaction.remainingAvailableCurrency = m.group(20);
                }

            } else if (!StringUtils.isEmpty(m.group(22))) {
                pumbTransaction.type = PumbTransactionType.forName(m.group(25));// transaction
                                                                                // rejected

                pumbTransaction.amount = Double.valueOf(m.group(22));
                pumbTransaction.currency = m.group(23);
                pumbTransaction.recipient = m.group(24);
                pumbTransaction.reasonRejected = m.group(26);

            } else {
                throw new IllegalArgumentException("unknown type of transaction");
            }
            return pumbTransaction;
        } else if (msg != null && (msg.contains("Shanovnuy") || msg.contains("Shanovnyj"))) {
            return null;
        }
        throw new IllegalArgumentException("message is not matched");
    }
}
