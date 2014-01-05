package kalpas.sms.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kalpas.sms.parse.PumbTransaction.PumbTransactionType;

import org.apache.commons.lang3.StringUtils;

public class PumbSmsParserEN extends AbstractPumbSmsParser implements PumbSmsParser {

    // @formatter:off
    private Pattern p = Pattern
            .compile("(FUIB ACCOUNT CODE\\s)?"
                    + "\\d{4}\\s"
                    + "\\*?(\\d{4})?\\s?" //card number, group 2
                    + "([ 0-9:-]{19})\\s" //date, group 3
                    + "(("
                        + "\\*"
                        + "(OTMENA OPERATSII\\s)?"
                        + "([a-zA-Z]*)\\s"// type of transaction, group 6
                        + "([0-9.]*)\\s([A-Z]{3})\\s"// amount and currency of money involved in transaction, group 7 & 8
                        + "((.*)\\s)?"// ATM/TERMINAL id, group 13
                        + "(\\*?BALANCE\\s)"
                        + "([0-9.-]*)\\s([A-Z]{3})?\\s?"// available amount and currency, group 15 & 16
                    + "))");
    // @formatter:on

    public PumbTransaction parsePumbSms(String msg) {

        Matcher m = p.matcher(msg);
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
                pumbTransaction.recipient = m.group(11);

                pumbTransaction.remaining = Double.valueOf(m.group(13));
                pumbTransaction.remainingCurrency = m.group(14);

            }

            return pumbTransaction;
        }
        throw new IllegalArgumentException("message is not matched");
    }
}
