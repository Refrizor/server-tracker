package me.devin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.devin.PlayerSessionTracker;
import me.devin.RetrofitFactory;
import me.devin.api.clients.ServerClient;
import me.devin.api.interfaces.ServersApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServersModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ServerClient.class).asEagerSingleton();
        bind(PlayerSessionTracker.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public ServersApi provideServersData(@Named("BaseApiUrl") String baseUrl, OkHttpClient client) {
        Retrofit retrofit = RetrofitFactory.createRetrofit(baseUrl, client);
        return retrofit.create(ServersApi.class);
    }
}