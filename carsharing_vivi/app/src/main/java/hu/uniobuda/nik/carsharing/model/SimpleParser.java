package hu.uniobuda.nik.carsharing.model;

/**
 * Created by igyartoimre on 2017. 05. 05..
 */
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import java.io.*;

public class SimpleParser {

    public static void main(String[] args) throws IOException {

        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();

        try {
            //System.out.print("Web Service Parser 1.0\n");

            // In practice, you'd retrieve your XML via an HTTP request.
            // Here we simply access an existing file.
            File xmlFile = new File("XML_FILE");

            // The xpath evaluator requires the XML be in the format of an InputSource
            InputSource inputXml = new InputSource(new FileInputStream(xmlFile));

            // Because the evaluator may return multiple entries, we specify that the expression
            // return a NODESET and place the result in a NodeList.
            NodeList nodes = (NodeList) xpath.evaluate("XPATH_EXPRESSION", inputXml, XPathConstants.NODESET);

            // We can then iterate over the NodeList and extract the content via getTextContent().
            // NOTE: this will only return text for element nodes at the returned context.
            for (int i = 0, n = nodes.getLength(); i < n; i++) {
                String nodeString = nodes.item(i).getTextContent();
                System.out.print(nodeString);
                System.out.print("\n");
            }
        } catch (XPathExpressionException ex) {
            System.out.print("XPath Error");
        } catch (FileNotFoundException ex) {
            System.out.print("File Error");
        }
    }
}