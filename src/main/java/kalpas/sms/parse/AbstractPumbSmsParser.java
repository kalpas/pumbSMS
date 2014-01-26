package kalpas.sms.parse;

import java.util.regex.Matcher;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Strings;

public abstract class AbstractPumbSmsParser {

    public final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    protected Integer fetchCardNumber(Matcher m, int group) {
        String cardString = m.group(group);
        if (!Strings.isNullOrEmpty(cardString)) {
            return Integer.valueOf(cardString);
        }
        return null;
    }

    protected DateTime fetchDate(Matcher m, int group) {
        String date = m.group(group);
        if (!Strings.isNullOrEmpty(date)) {
            try {
                return dateFormat.parseDateTime(date);
            } catch (IllegalArgumentException e) {
            }
        }
        return null;
    }

}