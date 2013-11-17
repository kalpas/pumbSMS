package kalpas.sms.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                                          + "([0-9.]*)\\s([A-Z]{3})\\s"// amount and currency asked , group 19 & 19
                                          + "(.*)\\)\\s"//ATM/TERMINAL id, group 20
                                          + "VIDMOVA:\\s"
                                          + "(.*)" // reason for discarding, group 16
                                      + "))");
                                      

    // @formatter:on

    public PumbTransaction parsePumbSms(String msg) {
        Matcher m = p.matcher(msg);
        if (m.matches()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println("group " + i + " " + m.group(i));
            }
            return new PumbTransaction();
        }
        return null;
    }

}
