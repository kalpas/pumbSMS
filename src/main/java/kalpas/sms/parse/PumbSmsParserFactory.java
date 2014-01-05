package kalpas.sms.parse;

public class PumbSmsParserFactory {

    public enum SmsLocale {
        UA, EN;
    }

    public static PumbSmsParser getInstance(SmsLocale locale) {
        switch (locale) {
        case UA:
            return new PumbSmsParserUA();
        case EN:
            return new PumbSmsParserEN();
        default:
            throw new IllegalArgumentException("Sms locale not implemented");
        }

    }

}
