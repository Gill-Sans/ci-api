package com.capit.capitgateway.routes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RoutesTest {

    @InjectMocks
    private Routes routes;

    @Test
    public void testGetServiceUrlLocal() throws Exception {
        //Arrange
        ReflectionTestUtils.setField(routes, "activeProfile", "local");

        //Act
        Method getServiceUrlMethod = Routes.class.getDeclaredMethod("getServiceUrl", String.class, int.class);
        getServiceUrlMethod.setAccessible(true);

        //Assert
        String result = (String) getServiceUrlMethod.invoke(routes, "test-service", 8080);
        assertEquals("http://localhost:8080", result);
    }

    @Test
    public void testGetServiceUrlNonLocal() throws Exception {
        //Arrange
        ReflectionTestUtils.setField(routes, "activeProfile", "production");

        //Act
        Method getServiceUrlMethod = Routes.class.getDeclaredMethod("getServiceUrl", String.class, int.class);
        getServiceUrlMethod.setAccessible(true);

        //Assert
        String result = (String) getServiceUrlMethod.invoke(routes, "test-service", 8080);
        assertEquals("http://test-service:8080", result);
    }
}
