package jain.shreyans.clean_okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import jain.shreyans.clean_okhttp.*;
import jain.shreyans.cleanokhttp.OkReq;
import jain.shreyans.cleanokhttp.ResponseCallback;

public class MainActivity extends AppCompatActivity implements ResponseCallback {

    private static final int GET_FIRST_DATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkReq request = new OkReq(this,this);
        request.makeRequest("https://jsonplaceholder.typicode.com/todos/1",GET_FIRST_DATA);
    }

    @Override
    public void onSuccess(String response, int requestCode) {
        if (requestCode==GET_FIRST_DATA) {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        }
    }
}
