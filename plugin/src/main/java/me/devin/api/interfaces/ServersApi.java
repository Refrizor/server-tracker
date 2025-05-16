package me.devin.api.interfaces;

import me.devin.api.ResponseWrapper;
import me.devin.api.models.ServerRegisterRequest;
import me.devin.api.models.ServerResponse;
import me.devin.api.models.ServerUpdateRequest;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.UUID;

public interface ServersApi {

    @POST("/servers")
    Call<Void> registerServer(@Body ServerRegisterRequest request);

    @GET("/servers/{uuid}")
    Call<ResponseWrapper<ServerResponse>> getServer(@Path("uuid") UUID serverId);

    @GET("/servers")
    Call<ResponseWrapper<ServerResponse>> getAllServers();

    @PATCH("/servers/{uuid}")
    Call<Void> updateServer(
            @Path("uuid") UUID serverId,
            @Body ServerUpdateRequest request
    );
}