package kr.pe.paran.myeyes.network;

import android.content.Context;

import kr.pe.paran.myeyes.model.Properties;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class RetrofitService extends RetrofitAdapter {

    public static RetrofitAPI getProperties(Context context) {
        return (RetrofitAPI) retrofit(context, RetrofitAPI.class);
    }

    public interface RetrofitAPI {
        @GET(RetrofitUrl.PROPERTIES_URL)
        Call<Properties> get();
    }

    public static DownloadAPI getRetrofit(Context context) {
        return (DownloadAPI) retrofit(context, DownloadAPI.class);
    }

    public interface DownloadAPI {
        @Streaming
        @GET
        Call<ResponseBody> download(@Url String downloadUrl);
    }

}
