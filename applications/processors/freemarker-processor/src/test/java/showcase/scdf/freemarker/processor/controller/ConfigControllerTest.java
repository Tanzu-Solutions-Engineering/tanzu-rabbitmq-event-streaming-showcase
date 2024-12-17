package showcase.scdf.freemarker.processor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import showcase.scdf.freemarker.processor.properties.FreemarkerProperties;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigControllerTest {
    private String expected = "expected";
    private ConfigController subject;
    private FreemarkerProperties properties;

    @BeforeEach
    void setUp() {
        properties = FreemarkerProperties.builder().template(expected).build();
        subject = new ConfigController(properties);
    }

    @Test
    void setTemplate() {
        subject.setTemplate(expected);
        assertThat(properties.getTemplate()).isEqualTo(expected);
    }
}