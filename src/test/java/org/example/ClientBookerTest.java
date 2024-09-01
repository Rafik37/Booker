package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Description;
import org.example.api.Bookingdates;
import org.example.api.CreateBooking;
import org.example.api.UserBooking;
import org.example.helper.BookerHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;


class ClientBookerTest {
    AssertionsBooker assertion = new AssertionsBooker();

    @Test
    @DisplayName("Проверка создания токена")
    @Description(value = "Проверяем создание не null-го токена")
    void createToken() throws JsonProcessingException {
        BookerHelper bookerHelper = new BookerHelper();
        String actualToken =bookerHelper.createToken();
        assertion.assertNotEqualsString(null, actualToken,"Токен авторизации не может быть " +
                "равен - 'null'");
    }

    @Test
    void getBookingIds() throws JsonProcessingException {
        BookerHelper bookerHelper = new BookerHelper();
        var bookingIds = bookerHelper.getBookingIds();
    }

    @Test
    void getBooking() throws IOException {
        BookerHelper bookerHelper = new BookerHelper();
        var bookingIds = bookerHelper.getBookingIds();
        String id = bookingIds[2].bookingId().toString();
        var userBooking = bookerHelper.getBooking(id);
    }

    @Test
    @DisplayName("Проверка создания нового бронирования")
    @Description(value = "Созданием новое бронирование, получаем индефикатор бронирования, получаем по индефикатору " +
            "бронирование и сравниваем его с тем, которое создавали ")
    void createBooking() throws JsonProcessingException {
        BookerHelper bookerHelper = new BookerHelper();
        var userBookingdates = new Bookingdates(LocalDate.parse("1996-06-19"),
                LocalDate.parse("2024-06-19"));
        var expectedUserBooking = new UserBooking("Ivan", "Yavkin",112,
                true,userBookingdates,"Thug Life");
        CreateBooking createBooking = bookerHelper.createBooking(expectedUserBooking);
        UserBooking actualUserBooking = bookerHelper.getBooking(createBooking.bookingId().toString());
        assertion.assertEqualsUserBooking(expectedUserBooking, actualUserBooking);
    }

    @Test
    @DisplayName("Проверка обновления существуещего бронирования")
    @Description(value = "Созданием новое бронирование, получаем индефикатор бронирования, обновляем по индефикатору" +
            "бронирование и сравниваем бронирование по индефикатору с бронированием, на которое хотели обновить")
    void updateBooking() throws JsonProcessingException {
        BookerHelper bookerHelper = new BookerHelper();
        var createUserBookingdates = new Bookingdates(LocalDate.parse("1997-06-19"),
                LocalDate.parse("2024-06-19"));
        var createUserBooking = new UserBooking("Ivan", "Yavkin",112,
                true,createUserBookingdates,"Thug Life");
        CreateBooking createBooking = bookerHelper.createBooking(createUserBooking);
        String id = createBooking.bookingId().toString();
        var expectedUserBookingdates = new Bookingdates(LocalDate.parse("1996-03-31"),
                LocalDate.parse("2024-08-24"));
        var expectedUserBooking = new UserBooking("Vano", "Yavnyi",911,
                true,expectedUserBookingdates,"Big City Life");
        bookerHelper.updateBooking(expectedUserBooking,id);
        UserBooking actualUserBooking = bookerHelper.getBooking(id);
        assertion.assertEqualsUserBooking(expectedUserBooking, actualUserBooking);
    }

    @Test
    @DisplayName("Проверка частичного обновления существуещего бронирования")
    @Description(value = "Созданием новое бронирование, получаем индефикатор бронирования, обновляем по индефикатору" +
            "частично поля бронирование и сравниваем поля бронирования по индефикатору с полями бронирования," +
            " на которое хотели обновить")
    void partialUpdateBooking() throws JsonProcessingException{
        BookerHelper bookerHelper = new BookerHelper();
        var partiakUserBooking = new UserBooking("Vano", "Yavnyi",null,
                null,null,null);
        var createUserBookingdates = new Bookingdates(LocalDate.parse("1997-06-19"),
                LocalDate.parse("2024-06-19"));
        var createUserBooking = new UserBooking("Ivan", "Yavkin",112,
                true,createUserBookingdates,"Thug Life");
        var expectedUserBooking = new UserBooking(partiakUserBooking.firstName(),partiakUserBooking.lastName(),
                createUserBooking.totalPrice(),createUserBooking.depositPaid(),createUserBooking.bookingDates(),
                createUserBooking.additionalNeeds());
        CreateBooking createBooking = bookerHelper.createBooking(createUserBooking);
        String id = createBooking.bookingId().toString();
        bookerHelper.partialUpdateBooking(partiakUserBooking,id);
        UserBooking actualUserBooking = bookerHelper.getBooking(id);
        assertion.assertEqualsUserBooking(expectedUserBooking, actualUserBooking);
    }

    @Test
    @DisplayName("Проверка удаления бронирования")
    @Description(value = "Созданием новое бронирование, получаем индефикатор бронирования, удаляем бронирование " +
            "под индефикатором, ожидаем выброса ошибки 'Ошибка подключения: java.io.IOException: " +
            "Запрос к серверу не был успешен: 404 Not Found' при попытке получения бронирования по индефикатору. ")
    void deleteBooking() throws IOException {
        BookerHelper bookerHelper = new BookerHelper();
        var createUserBookingdates = new Bookingdates(LocalDate.parse("1997-06-19"),
                LocalDate.parse("2024-06-19"));
        var createUserBooking = new UserBooking("Ivan", "Yavkin",112,
                true,createUserBookingdates,"Thug Life");
        CreateBooking createBooking = bookerHelper.createBooking(createUserBooking);
        var id = createBooking.bookingId().toString();
        bookerHelper.deleteBooking(id);
        assertion.assertThrowsDepositWithdraw(RuntimeException.class, () -> bookerHelper.getBooking(id),
                "Ожидался выброс ошибки 'Ошибка подключения: java.io.IOException: Запрос к серверу " +
                        "не был успешен: 404 Not Found'");
    }

    @Test
    @DisplayName("Проверка работоспособности API")
    @Description(value = "Проверяем работоспособность API")
    void healthCheck() {
        BookerHelper bookerHelper = new BookerHelper();
        var actual = bookerHelper.healthCheck();
        assertion.assertEqualsString("Created",actual,"API не запущен");
    }
}