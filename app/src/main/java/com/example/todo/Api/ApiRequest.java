package com.example.todo.Api;

import com.example.todo.Model.Todo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiRequest {
    @GET("Todolists")
    Call<List<Todo>> getRetrieveDataTodo();

    @POST("Todolists")
    Call<Todo> insertDataTodo(
            @Body Todo todoList
    );
    @PUT("Todolists/{id}")
    Call<Response<String >> updataDataTodo(
            @Path("id") int id,
            @Body Todo todoList
    );
    @DELETE("Todolists/{id}")
    Call<Response<String >> deleteDataTodo(
            @Path("id") int id
    );
}
