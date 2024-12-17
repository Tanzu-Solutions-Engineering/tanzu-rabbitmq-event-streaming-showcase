package showcase.scdf.freemarker.processor.function;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import showcase.scdf.freemarker.processor.properties.FreemarkerProperties;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.function.Function;

/**
 * Using Freemarker to create custom output
 * based on a template.
 *
 * @author gregory green
 */
@Component
public class FreemarkerFunction implements Function<Map<String,Object>, Message<String>> {
    private static final Logger log = LoggerFactory.getLogger(FreemarkerFunction.class);
    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);
    private final Template template;
    private final String contentType;

    @SneakyThrows
    public FreemarkerFunction(FreemarkerProperties freemarkerProperties) {

        log.info("Properties: {} ",freemarkerProperties);

        this.template = new Template("templateName", new StringReader(freemarkerProperties.getTemplate()), cfg);

        this.contentType = freemarkerProperties.getContentType();
    }


    @SneakyThrows
    @Override
    public Message<String> apply(Map<String, Object> map) {
        StringWriter writer = new StringWriter();
        template.process(map,writer);

        var output = writer.toString();

        log.info("TEMPLATE output: {} ",output);

        return MessageBuilder.withPayload(output)
                .setHeader(MessageHeaders.CONTENT_TYPE,contentType)
                .build();
    }
}
