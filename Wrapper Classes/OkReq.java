package jain.shreyans.cleanokhttp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shreyans on 11-Aug-18.
 */

public class OkReq {

    private ResponseCallback responseCallback;
    private Context context;
    private final int FAILURE =1;
    private final int SUCCESS=2;
    private static final String RESPONSE = "response";
    private Handler handler;
    private OkHttpClient client;


    public OkReq(final ResponseCallback responseCallback, final Context context){
        this.responseCallback = responseCallback;
        this.context=context;
        if (handler==null) {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case FAILURE:
                            if (context!=null) {
                                Toast.makeText(context, "Some Error Occurred, Please try again later", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case SUCCESS:
                            responseCallback.onSuccess(msg.getData().getString(RESPONSE),(int) msg.obj);
                            break;
                        default:
                            break;
                    }
                }
            };
        }
    }


    public void makeRequest(String url, final int requestCode){
        if (client==null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage(FAILURE);
                handler.sendMessageDelayed(msg, 0L);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = handler.obtainMessage(SUCCESS);
                msg.obj=requestCode;
                Bundle b = new Bundle();
                b.putString(RESPONSE,response.body().string());
                msg.setData(b);

                handler.sendMessageDelayed(msg, 0L);

            }
        });
    }

    public void makeRequest(Map<String,String> postParams, String url, final int requestCode ){
        if (client==null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String,String> entry : postParams.entrySet()) {

            formBody.add(entry.getKey(),entry.getValue());
        }
        RequestBody requestBody= formBody.build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage(FAILURE);
                handler.sendMessageDelayed(msg, 0L);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message msg = handler.obtainMessage(SUCCESS);
                msg.what=SUCCESS;
                msg.obj=requestCode;
                Bundle b = new Bundle();
                b.putString(RESPONSE,response.body().string());
                msg.setData(b);
                handler.sendMessageDelayed(msg, 0L);

            }
        });

    }



}
