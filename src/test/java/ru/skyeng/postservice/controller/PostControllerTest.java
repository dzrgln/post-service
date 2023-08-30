package ru.skyeng.postservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.dto.DeliveryId;
import ru.skyeng.postservice.model.dto.NewPostDelivery;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.skyeng.postservice.util.Fixtures.getDeliveryId;
import static ru.skyeng.postservice.util.Fixtures.getNewPostItem;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test01() throws Exception {
        NewPostDelivery postItem = getNewPostItem();
        String body = objectMapper.writeValueAsString(postItem);
        mockMvc.perform(post("/e-posts/new/letter").header("X-Post-Office-Id", 123456)
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.index").value(123456))
                .andExpect(jsonPath("$.typePostItem.type").value("Письмо"))
                .andExpect(jsonPath("$.address.index").value(987654))
                .andExpect(jsonPath("$.address.city").value("C3"))
                .andExpect(jsonPath("$.sender").value("Alex"));
    }
    @Test
    void test02() throws Exception {

        DeliveryId deliveryId = getDeliveryId();
        String body = objectMapper.writeValueAsString(deliveryId);

        mockMvc.perform(put("/e-posts/register/departure?where=987654")
                        .header("X-Post-Office-Id", 123456)
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.index").value(123456))
                .andExpect(jsonPath("$.typePostItem.type").value("Письмо"))
                .andExpect(jsonPath("$.address.index").value(987654))
                .andExpect(jsonPath("$.address.city").value("C3"))
                .andExpect(jsonPath("$.sender").value("Alex"));
    }

    @Test
    void test03() throws Exception {

        DeliveryId deliveryId = getDeliveryId();
        String body = objectMapper.writeValueAsString(deliveryId);

        mockMvc.perform(put("/e-posts/register/arrival")
                        .header("X-Post-Office-Id", 987654)
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.index").value(123456))
                .andExpect(jsonPath("$.typePostItem.type").value("Письмо"))
                .andExpect(jsonPath("$.address.index").value(987654))
                .andExpect(jsonPath("$.address.city").value("C3"))
                .andExpect(jsonPath("$.sender").value("Alex"));
    }

    @Test
    void test04() throws Exception {

        DeliveryId deliveryId = getDeliveryId();
        String body = objectMapper.writeValueAsString(deliveryId);

        mockMvc.perform(put("/e-posts/register/receiving")
                        .header("X-Post-Office-Id", 987654)
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.index").value(123456))
                .andExpect(jsonPath("$.typePostItem.type").value("Письмо"))
                .andExpect(jsonPath("$.address.index").value(987654))
                .andExpect(jsonPath("$.address.city").value("C3"))
                .andExpect(jsonPath("$.sender").value("Alex"));
    }

    @Test
    void test05() throws Exception {

        DeliveryId deliveryId = getDeliveryId();
        String body = objectMapper.writeValueAsString(deliveryId);

        mockMvc.perform(put("/e-posts/register/receive")
                        .header("X-Post-Office-Id", 987654)
                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.index").value(123456))
                .andExpect(jsonPath("$.typePostItem.type").value("Письмо"))
                .andExpect(jsonPath("$.address.index").value(987654))
                .andExpect(jsonPath("$.address.city").value("C3"))
                .andExpect(jsonPath("$.sender").value("Alex"));
    }

    @Test
    void test06() throws Exception {

        mockMvc.perform(get("/e-posts/tracking/1"))
//                        .header("X-Post-Office-Id", 987654)
//                        .content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.stageDeliveryList").isArray());
    }

//    @Test
//    void test02() throws Exception {
//        NewPostDelivery postItem = getPostItem();
//        String body = objectMapper.writeValueAsString(postItem);
//        mockMvc.perform(post("/e-posts/new/someitem").header("X-Post-Office-Id", 123456)
//                        .content(body).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertTrue(
//                        Objects.requireNonNull(result.getResolvedException())
//                                .getMessage().contains("Ошибка в указании типа отправления")));
//    }
//
//    @Test
//    void test03() throws Exception {
//        NewPostDelivery postItem = getPostItem();
//        String body = objectMapper.writeValueAsString(postItem);
//        mockMvc.perform(post("/e-posts/new/letter").header("X-Post-Office-Id", 777)
//                        .content(body).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertTrue(
//                        Objects.requireNonNull(result.getResolvedException())
//                                .getMessage().contains("Почтового отделения с индексом 777 не существует")));
//    }
//
//    @Test
//    void test04() throws Exception {
//        NewPostDelivery postItem = getPostItem();
//        postItem.getAddress().setCity("ДругаяStreet");
//        String body = objectMapper.writeValueAsString(postItem);
//        mockMvc.perform(post("/e-posts/new/letter").header("X-Post-Office-Id", 123456)
//                        .content(body).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError())
//                .andExpect(result -> assertTrue(
//                        Objects.requireNonNull(result.getResolvedException())
//                                .getMessage().contains("Указанный адрес отсутствует в базе")));
//    }


}
