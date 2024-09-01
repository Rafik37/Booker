package org.example.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.example.api.*;
import org.example.httpclient.ClientBooker;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.example.Constans.BASE_URL;
import static java.net.HttpURLConnection.HTTP_OK;

public class BookerHelper {

    ClientBooker clientBooker = new ClientBooker();

    ObjectMapper jacksonMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    public String token;

    @Step("Создаем токен авторизации" )
    public String createToken() throws JsonProcessingException {
        Autorization aut = new Autorization("admin","password123");
        String postBodyString = jacksonMapper.writeValueAsString(aut);
        var token = jacksonMapper.readValue(clientBooker.httpPost(BASE_URL+"/auth", postBodyString), Token.class);
        this.token = token.token();
        Allure.step("Создан токен авторизации - " + token);
        return token.token();
    }

    @Step("Получаем идентификаторы всех бронирований" )
    public BookingIds[] getBookingIds() throws JsonProcessingException{
        return jacksonMapper.readValue(clientBooker.httpGet(BASE_URL+"/booking",HTTP_OK), BookingIds[].class);
    }

    @Step("Получаем конкретное бронирование на основании идентификатора бронирования - {id}")
    public UserBooking getBooking(String id) throws JsonProcessingException{
        return jacksonMapper.readValue(clientBooker.httpGet(BASE_URL+"/booking/"+id, HTTP_OK), UserBooking.class);
    }

    @Step("Создаем новое бронирование со следующим наполнением -  {userBooking}" )
    public CreateBooking  createBooking(UserBooking userBooking) throws JsonProcessingException {
        String postBodyString = jacksonMapper.writeValueAsString(userBooking);
        return jacksonMapper.readValue(clientBooker.httpPost(BASE_URL+"/booking",postBodyString), CreateBooking.class);
    }

    @Step("Обнавляем бронирование под индефикатором {id} " )
    public UserBooking updateBooking(UserBooking userBooking,String id) throws JsonProcessingException {
        String putBodyString = jacksonMapper.writeValueAsString(userBooking);
        return jacksonMapper.readValue(clientBooker.httpPut(BASE_URL+"/booking/"+id,putBodyString,createToken()),
                UserBooking.class);
    }

    @Step("Обнавляем частично бронирование под индефикатором {id} " )
    public UserBooking partialUpdateBooking(UserBooking userBooking,String id) throws JsonProcessingException{
        String patchBodyString = jacksonMapper.writeValueAsString(userBooking);
        return jacksonMapper.readValue(clientBooker.httpPatch(BASE_URL+"/booking/"+id,patchBodyString,createToken()),
                UserBooking.class);
    }

    @Step("Удаляем бронирование под индефикатором {id} " )
    public String deleteBooking(String id) throws IOException {
        return clientBooker.httpDelete(BASE_URL+"/booking/"+id, createToken());
    }

    @Step("Проверяем работоспособности API")
    public String healthCheck()  {
        return clientBooker.httpGet(BASE_URL+"/ping",HTTP_CREATED);
    }
}
