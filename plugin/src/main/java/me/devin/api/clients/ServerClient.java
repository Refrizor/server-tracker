package me.devin.api.clients;

import com.google.inject.Inject;
import me.devin.PlayerSessionTracker;
import me.devin.ServerTrackerPlugin;
import me.devin.api.RetrofitFutureAdapter;
import me.devin.api.interfaces.ServersApi;
import me.devin.api.models.ServerRegisterRequest;
import me.devin.api.models.ServerResponse;
import me.devin.api.models.ServerUpdateRequest;
import retrofit2.Call;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ServerClient {
    private final ServersApi serversApi;
    private final PlayerSessionTracker playerSessionTracker;

    @Inject
    public ServerClient(ServersApi serversApi, PlayerSessionTracker playerSessionTracker) {
        this.serversApi = serversApi;
        this.playerSessionTracker = playerSessionTracker;
    }

    public CompletableFuture<ServerResponse> getServer(UUID uuid){
        return RetrofitFutureAdapter.callAsyncWithApiResponse(serversApi.getServer(uuid), ServerResponse.class, "server");
    }

    public void register(ServerRegisterRequest request) {
        RetrofitFutureAdapter.callAsync(serversApi.registerServer(request));
    }

    public void update() {
        ServerUpdateRequest request = new ServerUpdateRequest();
        List<ServerUpdateRequest.PlayerSession> players = playerSessionTracker.getActiveSessions();

        request.setPlayerList(players);
        request.setPlayerCount(players.size());
        request.setLastHeartbeat(System.currentTimeMillis() / 1000L);

        Call<Void> call = serversApi.updateServer(ServerTrackerPlugin.getInstance().getServerId(), request);
        RetrofitFutureAdapter.callAsync(call);
    }
}