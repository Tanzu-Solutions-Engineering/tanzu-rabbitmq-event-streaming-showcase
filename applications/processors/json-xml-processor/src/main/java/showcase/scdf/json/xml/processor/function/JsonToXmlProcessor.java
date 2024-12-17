package showcase.scdf.json.xml.processor.function;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import showcase.scdf.json.xml.processor.properties.JsonToXmlProperties;

import java.util.Map;
import java.util.function.Function;

/**
 * The application uses
 * [FasterXML/jackson](https://github.com/FasterXML/jackson) to convert
 * json to XML.
 * @author gregory green
 */
@Component
public class JsonToXmlProcessor implements Function<Map<String,Object>,Message<String>> {

    private final ObjectWriter xmlMapper;

    public JsonToXmlProcessor(JsonToXmlProperties jsonToXmlProperties) {
        this.xmlMapper = new XmlMapper()
        .writer().withRootName(jsonToXmlProperties.getRootName());
    }

    @SneakyThrows
    @Override
    public Message<String> apply(Map<String, Object> jsonNode) {
        return MessageBuilder.withPayload(xmlMapper.writeValueAsString(jsonNode))
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_XML_VALUE)
                .build();
    }
}
