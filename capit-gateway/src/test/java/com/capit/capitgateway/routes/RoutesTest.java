package com.capit.capitgateway.routes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RoutesTest {

    @InjectMocks
    private Routes routes;

    @Test
    public void testGetServiceUrlLocal() throws Exception {
        ReflectionTestUtils.setField(routes, "activeProfile", "local");

        Method getServiceUrlMethod = Routes.class.getDeclaredMethod("getServiceUrl", String.class, int.class);
        getServiceUrlMethod.setAccessible(true);

        String result = (String) getServiceUrlMethod.invoke(routes, "test-service", 8080);
        assertEquals("http://localhost:8080", result);
    }

    @Test
    public void testGetServiceUrlNonLocal() throws Exception {
        ReflectionTestUtils.setField(routes, "activeProfile", "production");

        Method getServiceUrlMethod = Routes.class.getDeclaredMethod("getServiceUrl", String.class, int.class);
        getServiceUrlMethod.setAccessible(true);

        String result = (String) getServiceUrlMethod.invoke(routes, "test-service", 8080);
        assertEquals("http://test-service:8080", result);
    }

    @Test
    public void testInteractionServiceRoute() {
        RouterFunction<ServerResponse> route = routes.InteractionServiceRoute();
        assertNotNull(route);
    }

    @Test
    public void testScheduleServiceRoute() {
        RouterFunction<ServerResponse> route = routes.ScheduleServiceRoute();
        assertNotNull(route);
    }

    @Test
    public void testUserServiceRoute() {
        RouterFunction<ServerResponse> route = routes.UserServiceRoute();
        assertNotNull(route);
    }
}
