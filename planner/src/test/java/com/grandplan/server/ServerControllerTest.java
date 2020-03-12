package com.grandplan.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grandplan.util.Event;
import com.grandplan.util.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUserEvents() throws Exception {

        User user = User.builder().email("lindama@bbd.co.za").build();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("http://localhost:8080/api/getUserEvents");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        mockHttpServletRequestBuilder.content(asJsonString(user));

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Event")))
                .andExpect(content().string(containsString("Grad Meetup")));
    }

    @Test
    public void testGetEventById() throws Exception {
        Event event = Event.builder().id(1L).build();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("http://localhost:8080/api/getEventById");
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        mockHttpServletRequestBuilder.content(asJsonString(event));

        this.mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Grad Meetup")));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
