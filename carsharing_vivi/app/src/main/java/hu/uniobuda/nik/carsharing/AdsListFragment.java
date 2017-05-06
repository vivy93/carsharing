package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.sax2.Driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.SimpleParser;
import hu.uniobuda.nik.carsharing.model.TravelMode;

public class AdsListFragment extends Fragment {
    private static final String TAG = "AdListFragment";

    View rootView;

    public static AdsListFragment newInstance(/*user id vagy dupla konstruktor újrafelhasználtó legyen*/)
    {
        Bundle args = new Bundle();

        AdsListFragment fragment = new AdsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_ads_list,container,false);
        return rootView;
    }

    public void SimpleParserr()
    {


        XPath xxxx = new XPath() {
            @Override
            public void reset() {

            }

            @Override
            public void setXPathVariableResolver(XPathVariableResolver resolver) {

            }

            @Override
            public XPathVariableResolver getXPathVariableResolver() {
                return null;
            }

            @Override
            public void setXPathFunctionResolver(XPathFunctionResolver resolver) {

            }

            @Override
            public XPathFunctionResolver getXPathFunctionResolver() {
                return null;
            }

            @Override
            public void setNamespaceContext(NamespaceContext nsContext) {

            }

            @Override
            public NamespaceContext getNamespaceContext() {
                return null;
            }

            @Override
            public XPathExpression compile(String expression) throws XPathExpressionException {
                return null;
            }

            @Override
            public Object evaluate(String expression, Object item, QName returnType) throws XPathExpressionException {
                return null;
            }

            @Override
            public String evaluate(String expression, Object item) throws XPathExpressionException {
                return null;
            }

            @Override
            public Object evaluate(String expression, InputSource source, QName returnType) throws XPathExpressionException {
                return null;
            }

            @Override
            public String evaluate(String expression, InputSource source) throws XPathExpressionException {
                return null;
            }
        };
        xxx.
        try {


            File xmlFile = new File("https://maps.googleapis.com/maps/api/distancematrix/xml?origins=place_id:ChIJyc_U0TTDQUcRYBEeDCnEAAQ&destinations=place_id:ChIJgyte_ioMR0cRcBEeDCnEAAQ|place_id:ChIJ04zIKBKFQUcRsFgeDCnEAAQ&mode=walk&language=hu-HU&key=AIzaSyB5YMQI8YQ8l8cj1F6aC1rIQ3pvQjmvz0s");
            boolean x =xmlFile.canRead();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("https://maps.googleapis.com/maps/api/distancematrix/xml?origins=place_id:ChIJyc_U0TTDQUcRYBEeDCnEAAQ&destinations=place_id:ChIJgyte_ioMR0cRcBEeDCnEAAQ|place_id:ChIJ04zIKBKFQUcRsFgeDCnEAAQ&mode=walk&language=hu-HU&key=AIzaSyB5YMQI8YQ8l8cj1F6aC1rIQ3pvQjmvz0s");


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
        } catch (IOException e) {
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



    private List<Advertisement> relevantAdsOnFoot(/*user id kell majd h a saját hirdetéseimet ne listázza TravelMode travelMode,*/
                                                    Date date, String fromid, List<Advertisement> adListFull)
    {
        List<Advertisement> adList = new ArrayList<>();

        for(int i =0; i<adListFull.size(); i++) {// kiválogatom a kocsis hirdetéseket akik aznap indulna mint én
            if ( adListFull.get(i).getMode().equals( TravelMode.BY_CAR))
                if(adListFull.get(i).getWhen().equals(date) || adListFull.get(i).getWhen().after(date))// aznap = akkor vagy utánna-> refactorálás később
                {
                    adList.add(adListFull.get(i));
                }

        }
//praser kell!

        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();

        try {
            //System.out.print("Web Service Parser 1.0\n");

            // In practice, you'd retrieve your XML via an HTTP request.
            // Here we simply access an existing file.
            File xmlFile = new File("https://maps.googleapis.com/maps/api/distancematrix/json?origins=place_id:ChIJyc_U0TTDQUcRYBEeDCnEAAQ&destinations=place_id:ChIJgyte_ioMR0cRcBEeDCnEAAQ|place_id:ChIJ04zIKBKFQUcRsFgeDCnEAAQ&mode=walk&language=hu-HU&key=AIzaSyB5YMQI8YQ8l8cj1F6aC1rIQ3pvQjmvz0s");

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


        for(int i = 0; i<adList.size();i++)
        {


        }


        return  adList;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Advertisement> adList = new ArrayList<>();

        SimpleParserr();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateInString = "06-05-2017 10:20:00";
        String dateInString2 = "10-05-2017 10:20:00";
        Date date;
        Date date2;

        try {
            date = sdf.parse(dateInString);
            date2 = sdf.parse(dateInString2);
            Log.d(TAG, "trying to parse date");
            Random random = new Random();

            //for(int i =0; i<10; i++) {// Teszt adatok -> van megjelenítés
                Log.d(TAG, "add element to advertisement list");
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Lurdy Ház", "ChIJDS0Ugd7cQUcRf2iJF_ktiA0", "Debrecen", "Népliget","ChIJ5T0Xg97cQUcRGeP7pI9T0nU","Nagyvárad tér","ChIJ1WyAm-bcQUcRVMj1mLIjBXo", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date2, "Lurdy Ház", "ChIJDS0Ugd7cQUcRf2iJF_ktiA0", "Debrecen", "Népliget","ChIJ5T0Xg97cQUcRGeP7pI9T0nU","Nagyvárad tér","ChIJ1WyAm-bcQUcRVMj1mLIjBXo", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Nyugati pu", "ChIJU05ZAQ3cQUcRf5IgNd3ONqk", "Hernad", "Oktogon","ChIJiXy4eG7cQUcRQOdbiM3lrfQ","Corvin Plaza","ChIJDbyKvffcQUcRCxObD4qECw4", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Budapest Bécsi út 96", "ChIJ8bksZFrZQUcRDF9qdcEH2hI", "Miskolc", "Budapest, Kolosy tér","ChIJc5DTZlbZQUcRipml-U4xDEI","Margit híd, budai hídfő","ChIJYR_27QPcQUcRkT1s8EXCBnU", random.nextInt(4) ));
                adList.add(new Advertisement(/*"1",*/ TravelMode.BY_CAR, date, "Debrecen", "ChIJgyte_ioMR0cRcBEeDCnEAAQ", "Bocskaikert", "node1","node1id","node2","node2id", random.nextInt(4) ));
                //adList.add(new Advertisement(/*"1",*/ TravelMode.ON_FOOT, date, "Népliget", "ChIJ5T0Xg97cQUcRGeP7pI9T0nU", null,  null, null,null,null,null ));
            //}

            final AdAdapter adapter = new AdAdapter(adList);
            ListView listView = (ListView) rootView.findViewById(R.id.ads_lstview);
            listView.setAdapter(adapter);

            // klikk egy listaelemre: új DetailsActivity()
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Advertisement selectedAd = adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("selected_ad", selectedAd);
                    startActivity(intent);
                }
            });

        } catch (ParseException e) {
            // a catch blokk az  sdf.parse miatt kell, de ez majd úgyis eltűnik ha valós adatokkal dolgozunk a DB-ből
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }

        //vege
    }
}
