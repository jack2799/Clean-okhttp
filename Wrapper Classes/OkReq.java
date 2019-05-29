package util.okhttp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shreyans on 11-Aug-18.
 */

public class OkReq {

    private ResponseCallback responseCallback;
    private Context context;
    private final int FAILURE = 1;
    private final int SUCCESS = 2;
    private static final String RESPONSE = "response";
    private Handler handler;
    private OkHttpClient client;
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final MediaType JSON = MediaType.get("application/json");
    private static final String STATUS_CODE = "status_code";


    public OkReq(final ResponseCallback responseCallback, final Context context) {
        this.responseCallback = responseCallback;
        this.context = context;
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case FAILURE:
                            if (context != null) {
                                Toast.makeText(context, "Some Error Occurred, Please try again later", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case SUCCESS:
                            int statusCode = msg.getData().getInt(STATUS_CODE);
                            String response = msg.getData().getString(RESPONSE);
                            switch (statusCode) {
                                case 200:
                                case 201:
                                    responseCallback.onSuccess(response, (int) msg.obj);
                                    break;
                                case 401:
                                case 400:
                                    extractErrorAndShow(response);
                                    break;
                                default:
                                    break;

                            }
                            break;
                        default:
                            break;
                    }
                }

                private void extractErrorAndShow(String response) {
                    String error = null;
                    JSONObject errorObj = null;
                    try {
                        errorObj = new JSONObject(response);
                        error = errorObj.getString("error");
                    } catch (JSONException e) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = errorObj.getJSONArray("errors");
                            errorObj = jsonArray.getJSONObject(0);
                            error = errorObj.getString("msg");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    ToastErrorMessage(error);
                }

                private void ToastErrorMessage(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            };
        }
    }


    public void makeRequest(String url, final int requestCode) {
        if (client == null) {
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

                msg.what = SUCCESS;
                msg.obj = requestCode;
                Bundle b = new Bundle();
                b.putString(RESPONSE, response.body().string());
                b.putInt(STATUS_CODE, response.code());
                msg.setData(b);
                handler.sendMessageDelayed(msg, 0L);

            }
        });
    }

    public void makeRequest(Map<String, String> postParams, String url, final int requestCode) {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, String> entry : postParams.entrySet()) {

            formBody.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = formBody.build();
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

                msg.what = SUCCESS;
                msg.obj = requestCode;
                Bundle b = new Bundle();
                b.putString(RESPONSE, response.body().string());
                b.putInt(STATUS_CODE, response.code());
                msg.setData(b);
                handler.sendMessageDelayed(msg, 0L);

            }
        });

    }

    public void makeRequest(String json, String url, final int requestCode) {

        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
        }
        String requestBodyjson = json;
        Charset charset = UTF_8;
        byte[] bytes = requestBodyjson.getBytes(charset);

        RequestBody requestBody = RequestBody.create(JSON, bytes);

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

                msg.what = SUCCESS;
                msg.obj = requestCode;
                Bundle b = new Bundle();
                b.putString(RESPONSE, response.body().string());
                b.putInt(STATUS_CODE, response.code());
                msg.setData(b);
                handler.sendMessageDelayed(msg, 0L);


            }
        });

    }


}
