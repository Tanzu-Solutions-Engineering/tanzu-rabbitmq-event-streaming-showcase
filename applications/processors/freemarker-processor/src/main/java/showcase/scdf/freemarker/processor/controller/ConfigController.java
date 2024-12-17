package showcase.scdf.freemarker.processor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import showcase.scdf.freemarker.processor.properties.FreemarkerProperties;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("config")
public class ConfigController {

    private final FreemarkerProperties freemarkerProperties;

    @PostMapping("template")
    public void setTemplate(@RequestBody String template)
    {
        freemarkerProperties.setTemplate(template);

        log.info("properties: {}",freemarkerProperties);
    }
}
