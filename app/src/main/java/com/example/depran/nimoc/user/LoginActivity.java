package com.example.depran.nimoc.user;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;

/**
 * Created by Eidith on 22/04/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private Button btnSignUp;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        changeContent(SignInFragment.newInstance(this));

        btnSignIn = (Button) findViewById(R.id.btnLoginMenu);
        btnSignUp = (Button) findViewById(R.id.btnSignUpMenu);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent(SignUpFragment.newInstance(LoginActivity.this));
                btnSignIn.setBackgroundResource(R.color.btnLogin);
                btnSignIn.setTextColor(getResources().getColor(R.color.black));

                btnSignUp.setBackgroundResource(R.color.login);
                btnSignUp.setTextColor(getResources().getColor(R.color.white));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent(SignInFragment.newInstance(LoginActivity.this));
                btnSignUp.setBackgroundResource(R.color.btnLogin);
                btnSignUp.setTextColor(getResources().getColor(R.color.black));

                btnSignIn.setBackgroundResource(R.color.login);
                btnSignIn.setTextColor(getResources().getColor(R.color.white));
            }
        });


    }


    public void changeContent(Fragment fragment){
        getFragmentManager().beginTransaction()
                .replace(R.id.contentLayout, fragment)
                .commitAllowingStateLoss();
    }

    //    EditText us, pass;

//    //berfungsi untuk login ke database
//    private class LoginAsyncTask extends AsyncTask<String, String, String> {
//
//        ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(SignInFragment.this);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String respon = "";
//            try {
//                String url = Constrants.URL_LOGIN;
//                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>() ;
//                postParameters.add(new BasicNameValuePair("action", "login"));
//                postParameters.add(new BasicNameValuePair("username", params[0]));
//                postParameters.add(new BasicNameValuePair("password", params[1]));
//
//                respon = CustomHttpClient.executeHttpPost(url,postParameters);
//
//            } catch (Exception e) {
//                respon = e.toString();
//            }
//            return respon;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            progressDialog.dismiss();
//            Log.e("Respon"," -> "+result) ;
//            try {
//                JSONObject object = new JSONObject(result) ;
//                if(object.getString("status").equalsIgnoreCase("success")){
//
//                    String idUser = object.getJSONObject("data").getString("id_u") ;
//                    String username = object.getJSONObject("data").getString("username") ;
//                    String password = object.getJSONObject("data").getString("password") ;
////                    String name = object.getJSONObject("data").getString("name") ;
////                    String noTelp = object.getJSONObject("data").getString("no_telp") ;
////                    String photo = object.getJSONObject("data").getString("photo") ;
//
//                    //pengisian data username ke Session...
//                    Session.createLoginSession(SignInFragment.this,idUser, username, password);
//
//                    //Toast.makeText(Login2Activity.this, "Pindah ke BerandaActivity",Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(SignInFragment.this, BerandaActivity.class));
//                    SignInFragment.this.finish();
//
//                } else {
//                    Toast.makeText(SignInFragment.this, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                Log.e("masuk","-> "+e.getMessage()) ;
//            }
//        }
//    }
}
