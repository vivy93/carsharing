package hu.uniobuda.nik.carsharing.model;

/**
 * Created by igyartoimre on 2017. 05. 05..
 */
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;

public class SimpleParser {
    String URLXML="";

    public SimpleParser(String URLXML) {
        this.URLXML = URLXML;
    }

    public static void main(String[] args) throws IOException {

        try {

            File xmlFile = new File("https://maps.googleapis.com/maps/api/distancematrix/xml?origins=place_id:ChIJyc_U0TTDQUcRYBEeDCnEAAQ&destinations=place_id:ChIJgyte_ioMR0cRcBEeDCnEAAQ|place_id:ChIJ04zIKBKFQUcRsFgeDCnEAAQ&mode=walk&language=hu-HU&key=AIzaSyB5YMQI8YQ8l8cj1F6aC1rIQ3pvQjmvz0s");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            Element statusElement = (Element) doc.getElementsByTagName("status").item(0);
            if(statusElement.getNodeValue().equals("OK")) {

            NodeList objectList = doc.getElementsByTagName("element");
                for (int i = 0; i < objectList.getLength(); i++) {

                    Node objectNode = objectList.item(i);

                    if (objectNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element objectElement = (Element) objectNode;
                        createObjectFromElement(objectElement);
                    }

                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void createObjectFromElement(Element objectElement) {
        if(objectElement.getElementsByTagName("status").item(0).getAttributes().equals("OK"))
        {
            NamedNodeMap distanceAttributes = objectElement.getElementsByTagName("distance").item(0).getAttributes();
            int distance = Integer.getInteger(distanceAttributes.item(0).getTextContent());
        }
    }
}