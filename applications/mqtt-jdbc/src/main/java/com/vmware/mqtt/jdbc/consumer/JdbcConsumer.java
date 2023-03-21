package com.vmware.mqtt.jdbc.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class JdbcConsumer implements Consumer<String> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void accept(String json) {
        String sql = "select";
        jdbcTemplate.update(sql,json);
    }
}
