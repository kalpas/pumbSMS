package kalpas.sms.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.Multisets;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class SmsParserTest {

    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        PumbSmsParser parser = new PumbSmsParser();
        int parsed = 0;
        int overall = 0;
        List<PumbTransaction> transactions = new ArrayList<PumbTransaction>();

        File fXmlFile = new File("sms-20131105131035.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        NodeList nList = doc.getElementsByTagName("sms");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                if ("PUMB".equals(eElement.getAttribute("address"))) {
                    String body = eElement.getAttribute("body");
                    System.out.println("msg : " + body);
                    overall++;
                    PumbTransaction transaction = parser.parsePumbSms(body.toString());
                    if (transaction != null) {
                        transactions.add(transaction);
                        parsed++;
                    } else {
                        System.err.println("not matched");
                    }

                }

            }
        }

        System.err.format("overall :%d, parsed: %d (%.2f%%)%n", overall, parsed, parsed * 100. / overall);

        // Set<String> atms = new HashSet<String>();
        SortedMultiset<String> atms = TreeMultiset.create();
        for (PumbTransaction tx : transactions) {
            if (StringUtils.isEmpty(tx.recipient)) {
                System.err.println(tx.originalMsg);
            } else {
                atms.add(tx.recipient);
            }
        }

        Iterator<String> iterator = Multisets.copyHighestCountFirst(atms).elementSet().iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.print(element);
            System.out.println(" " + atms.count(element));
        }

        //
        // for (String atm : atms) {
        // System.out.println(atm);
        // }

    }

    @Test
    public void test2() {
        System.out.println(PumbTransaction.PumbTransactionType.forName("SPYSANNIA"));
    }

    @Test
    public void test3() throws Exception {
        PumbSmsParser parser = new PumbSmsParser();
        String body = "RAKHUNOK 111 2013-10-10 13:07:40 NADHODZHENNIA 12457.86 UAH DOSTUPNO 13722.63 UAH Z NYH VLASNYH KOSHTIV 13722.63 UAH";
        if (parser.parsePumbSms(body.toString()) != null) {
        } else {
            System.err.println(body);
            throw new Exception();
        }

    }

    @Test
    public void test4() throws Exception {
        PumbSmsParser parser = new PumbSmsParser();
        String body = "RAKHUNOK 111 *8499 2013-08-05 12:53:43 (41.00 UAH PORTMONE.MOBILE Kyiv UA) VIDMOVA: Nedostatno koshtiv";
        if (parser.parsePumbSms(body.toString()) != null) {
        } else {
            System.err.println(body);
            throw new Exception();
        }

    }

    @Test
    public void test5() throws Exception {
        PumbSmsParser parser = new PumbSmsParser();
        String body = "111 *8499 2013-11-05 11:16:35 SPYSANNIA 600.00 UAH ZDOROVIE NAUCH KHARKOV UA (DOSTUPNO 58.86 UAH) Z NYH VLASNYH  KOSHTIV 158.86 UAH";
        if (parser.parsePumbSms(body.toString()) != null) {
        } else {
            System.err.println(body);
            throw new Exception();
        }

    }

    @Test
    public void test6() throws Exception {
        PumbSmsParser parser = new PumbSmsParser();
        String body = "RAKHUNOK 111 *8499 2013-09-10 05:11:21 ZABLOKOVANO 0.99 USD (U VALIUTI RAKHUNKU 8.08 UAH)    GOOGLE *MapMyFITNESS GOOGLE.COM/CH US (DOSTUPNO 90.52 UAH)";
        PumbTransaction tx = parser.parsePumbSms(body.toString());
        if (tx != null) {

        } else {
            System.err.println(body);
            throw new Exception();
        }

    }

}
