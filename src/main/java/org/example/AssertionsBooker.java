package org.example;

import io.qameta.allure.Step;
import org.example.api.UserBooking;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsBooker {
    @Step("Сравниваем ожидаемое бронирование - {expected} с фактическим - {actual}")
    public void assertEqualsUserBooking(UserBooking expected, UserBooking actual){
        assertEquals( expected, actual, "errorMessage");
    }
    public void assertNotEqualsString(String expected, String actual, String errorMessage){
        assertNotEquals( expected, actual, errorMessage);
    }
    public void assertEqualsString(String expected, String actual, String errorMessage) {
        assertEquals(expected, actual, errorMessage);
    }
    @Step("Ожидаем выброса ошибки ")
    <T extends Exception> void assertThrowsDepositWithdraw(Class<T> exceptionClass, Executable executable, String message){
        assertThrows(exceptionClass,  executable, message);
    }
}
