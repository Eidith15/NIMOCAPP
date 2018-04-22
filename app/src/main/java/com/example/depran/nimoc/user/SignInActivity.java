
package com.example.depran.nimoc.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.R;
import com.example.depran.nimoc.utils.Session;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {
    EditText us, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_in);

        us = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = us.getText().toString().trim();
                String pswd = pass.getText().toString().trim();
//                if (user.equalsIgnoreCase("username") && pswd.equalsIgnoreCase("password")) {
//                    Session.createSignInSession(SignInActivity.this, user);
//                    startActivity(new Intent(SignInActivity.this, BerandaActivity.class));
//                    finish();
//                } else {
//                    Toast.makeText(SignInActivity.this, "user invalid", Toast.LENGTH_SHORT).show();
//                }
                new LoginAsyncTask().execute(user, pswd) ;
            }
        });

        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


    //berfungsi untuk login ke database
    private class LoginAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignInActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respon = "";
            try {
                String url = Constrants.URL_LOGIN;
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>() ;
                postParameters.add(new BasicNameValuePair("action", "login"));
                postParameters.add(new BasicNameValuePair("username", params[0]));
                postParameters.add(new BasicNameValuePair("password", params[1]));

                respon = CustomHttpClient.executeHttpPost(url,postParameters);

            } catch (Exception e) {
                respon = e.toString();
            }
            return respon;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.e("Respon"," -> "+result) ;
            try {
                JSONObject object = new JSONObject(result) ;
                if(object.getString("status").equalsIgnoreCase("success")){

                    String idUser = object.getJSONObject("data").getString("id_u") ;
                    String username = object.getJSONObject("data").getString("username") ;
                    String password = object.getJSONObject("data").getString("password") ;
//                    String name = object.getJSONObject("data").getString("name") ;
//                    String noTelp = object.getJSONObject("data").getString("no_telp") ;
//                    String photo = object.getJSONObject("data").getString("photo") ;

                    //pengisian data username ke Session...
                    Session.createLoginSession(SignInActivity.this,idUser, username, password);

                    //Toast.makeText(Login2Activity.this, "Pindah ke BerandaActivity",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignInActivity.this, BerandaActivity.class));
                    SignInActivity.this.finish();

                } else {
                    Toast.makeText(SignInActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("masuk","-> "+e.getMessage()) ;
            }
        }
    }
}
