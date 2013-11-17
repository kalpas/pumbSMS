package kalpas.sms.parse;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SmsParserTest {

    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        PumbSmsParser parser = new PumbSmsParser();
        int parsed = 0;
        int overall = 0;

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
                    if(parser.parsePumbSms(body.toString())!=null){
                        parsed++;
                    } else {
                        System.err.println("not matched");
                    }
                        

                }

            }
        }
        
        System.err.format("overall :%d, parsed: %d (%.2f%%)", overall, parsed, parsed * 100. / overall);

    }

    @Test
    public void test2() {
        System.out.println(PumbTransaction.PumbTransactionType.valueOf("SPYSANNIA"));
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
        String body = "111 *8499 2013-11-05 11:16:35 SPYSANNIA 600.00 UAH ZDOROVIE NAUCH KHARKOV UA (DOSTUPNO 58.86 UAH) Z NYH VLASNYH  KOSHTIV 58.86 UAH";
        if (parser.parsePumbSms(body.toString()) != null) {
        } else {
            System.err.println(body);
            throw new Exception();
        }

    }

}
