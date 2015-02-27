package com.codebreeze.incubator.xml;

import com.codebreeze.incubator.generated.model.Gbom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static com.jpmorgan.dcpp.commons.Classpath.resourceAsString;

@ContextConfiguration("classpath:application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class XmlBestPracticseTest {

    @Autowired
    private Marshaller marshaller;

    @Autowired
    private Unmarshaller unmarshaller;

    @Test
    public void useGenericTools() throws IOException {
        final Gbom gbom = asGbom("gbom-example.xml");

        final String xmlAgain = asXml(gbom);

        System.out.println(gbom);
        System.out.println(xmlAgain);
        //* do not write boiler plate code
        //Xmls
        //Xpath.value/values/attribute/attribues
        //Xslt.apply
    }

    private Gbom asGbom(String fileName) throws IOException {
        final String xml = resourceAsString(fileName);
        System.out.println(xml);
        return Gbom.class.cast(unmarshaller.unmarshal(new StreamSource(new StringReader(xml))));
    }

    private String asXml(Gbom obj) throws IOException {
        StringWriter writer = new StringWriter();
        final Result result = new StreamResult(writer);
        marshaller.marshal(obj, result);
        return writer.toString();
    }

    @Test
    public void useGroovy(){
        //C:\dev\transmitDocumentFort\trunk\transmitDocumentFort-core\src\test\groovy\com\jpmorgan\dcpp\tdf\messaging
        //C:\dev\transmitDocumentFort\trunk\transmitDocumentFort-core\src\test\groovy\com\jpmorgan\dcpp\tdf\service
        //easier syntax
        //updating
        //object model without object model
    }

    @Test
    public void useConfiguredMarshallerUnmarshallers(){
        //no manual creation of xml or reading of it
    }

    @Test
    public void watchForInvalidCharsInXml1AndXml11(){
        //http://en.wikipedia.org/wiki/Valid_characters_in_XML
    }

    @Test
    public void useSchemasAndJaxbAndSpring(){
        //marshall, unmarshall, and validate
    }

    @Test
    public void understandWhyThisIsAProblem(){
        final String a = "some value is < another value";
        System.out.println("<a>" + a + "</a>");
    }

    @Test
    public void useNameSpacesAndVersions(){
        //learn how to do version management in a big system
    }

    /**
     int
     class [I
     class java.lang.Integer
     class [Ljava.lang.Integer;
     */
    @Test
    public void print(){
        System.out.println(int.class);
        System.out.println(int[].class);
        System.out.println(Integer.class);
        System.out.println(Integer[].class);
    }
}