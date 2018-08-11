package jain.shreyans.cleanokhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ResponseCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkReq request = new OkReq(this,this);
        request.makeRequest("https://jsonplaceholder.typicode.com/todos/1",1);
    }

    @Override
    public void onSuccess(String response, int requestCode) {
        Toast.makeText(this,response,Toast.LENGTH_LONG).show();
    }
}
