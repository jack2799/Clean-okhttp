This project includes wrapper classes to cleanly & easily use OKHTTP.
### Steps to Use

- Download  [OkReq](https://github.com/jack2799/Clean-okhttp/blob/master/Wrapper%20Classes/OkReq.java).
- Download  [ResponseCallback](https://github.com/jack2799/Clean-okhttp/blob/master/Wrapper%20Classes/ResponseCallback.java).
- Modify OkReq as per your needs.
- In activity or fragment implement ResponseCallBack interface.
- Make a call to server like this.

         OkReq request = new OkReq(this,this);
         request.makeRequest("https://jsonplaceholder.typicode.com/todos/1", GET_FIRST_DATA);
    
- Get response in onSuccess method like this.
           
        @Override
        public void onSuccess(String response, int requestCode) {
        if (requestCode==GET_FIRST_DATA) {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        }
        }
  
