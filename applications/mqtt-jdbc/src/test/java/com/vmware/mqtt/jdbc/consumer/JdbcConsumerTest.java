package com.vmware.mqtt.jdbc.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JdbcConsumerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private JdbcConsumer subject;
    private String json = "{}";

    @BeforeEach
    void setUp() {
        subject = new JdbcConsumer(jdbcTemplate);
    }

   @Test
   void save() {

       subject.accept(json);

       verify(jdbcTemplate).update(anyString(),anyString());
   }
}