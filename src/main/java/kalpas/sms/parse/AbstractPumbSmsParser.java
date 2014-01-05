package kalpas.sms.parse;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class AbstractPumbSmsParser {

    public final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    protected Integer fetchCardNumber(Matcher m, int group) {
        String cardString = m.group(group);
        if (!StringUtils.isEmpty(cardString)) {
            return Integer.valueOf(cardString);
        }
        return null;
    }

    protected DateTime fetchDate(Matcher m, int group) {
        String date = m.group(group);
        if (!StringUtils.isEmpty(date)) {
            try {
                return dateFormat.parseDateTime(date);
            } catch (IllegalArgumentException e) {
            }
        }
        return null;
    }

}