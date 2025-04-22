package com.capit.capitschedule;

import com.capit.exceptions.handling.RestExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class CapitScheduleApplicationTests {

    @MockitoBean
    private RestExceptionHandler restExceptionHandler;

    @Test
    void contextLoads() {
    }

}
