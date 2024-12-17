package showcase.scdf.json.xml.processor.function;

import nyla.solutions.core.xml.XML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import showcase.scdf.json.xml.processor.properties.JsonToXmlProperties;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonToXmlProcessorTest {

    private Map<String, Object> jsonMap = Map.of("key","expected");
    private JsonToXmlProcessor subject;
    private JsonToXmlProperties properties;
    private String rootName = "junit";

    @BeforeEach
    void setUp() {
        properties = new JsonToXmlProperties(rootName);
        subject = new JsonToXmlProcessor(properties);
    }

    @Test
    void apply() throws ParserConfigurationException, IOException, SAXException {
        var actualMessage = subject.apply(jsonMap);

        assertEquals("application/xml", actualMessage.getHeaders().get("contentType"));

        var actual = actualMessage.getPayload();
        System.out.println(actual);

        assertThat(actual).contains("expected");
        assertThat(actual).contains(rootName);

        assertNotNull(XML.toDocument(actual));
    }
}