package org.example.httpclient;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import okhttp3.*;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

public class ClientBooker{


    OkHttpClient client = new OkHttpClient();
    @Step("Получаем ответ на GET - запрос" )
    public String httpGet(String url, int httpCode){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();
        Allure.step("Отправляем запрос - " + request);
        return response(request,httpCode);
    }

    @Step("Получаем ответ на POST - запрос" )
    public String httpPost(String url, String postBodyString) {
        var requestPost = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(requestBody(postBodyString))
                .build();
        Allure.step("Отправляем запрос - " + requestPost);
        return response(requestPost,HTTP_OK);
    }

    @Step("Получаем ответ на PUT - запрос" )
    public String httpPut(String url, String putBodyString, String token) {
        var requestPut = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cookie", "token="+token)
                .put(requestBody(putBodyString))
                .build();
        Allure.step("Отправляем запрос - " + requestPut);
        return response(requestPut,HTTP_OK);
    }

    @Step("Получаем ответ на PATCH - запрос" )
    public String httpPatch(String url, String putBodyString, String token) {
            var requestPatch = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Cookie", "token="+token)
                    .patch(requestBody(putBodyString))
                    .build();
            Allure.step("Отправляем запрос - " + requestPatch);
            return response(requestPatch,HTTP_OK);
    }

    @Step("Получаем ответ на DELETE - запрос" )
    public String httpDelete(String url, String token)  {
        var requestDelete = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cookie", "token="+token)
                .delete()
                .build();
        Allure.step("Отправляем запрос - " + requestDelete);
        return response(requestDelete,HTTP_CREATED);
    }
    public String response(Request request, int httpCode){
        try {
            Response response = this.client.newCall(request).execute();
            if (!(response.code() == httpCode)) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
            assert response.body() != null;
            Allure.step("Получение ответа на запрос - " + response);
            return response.body().string();
        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
        }
        return null;
    }

    public RequestBody requestBody(String bodyString){
        String jsonMimeType = "application/json; charset= utf-8";
        MediaType jsonContentType = MediaType.parse(jsonMimeType);
        return RequestBody.create(bodyString, jsonContentType);
    }
}
