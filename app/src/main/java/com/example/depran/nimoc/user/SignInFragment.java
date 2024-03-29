
package com.example.depran.nimoc.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.utils.Session;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignInFragment extends Fragment {

    public static LoginActivity loginActivity;
    private Button btnGoLogin;
    private EditText username;
    private EditText password;

    public static SignInFragment newInstance(LoginActivity activity) {
        loginActivity = activity;
        return new SignInFragment();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(loginActivity).inflate(R.layout.fragment_sign_in, container, false);
        btnGoLogin = (Button) view.findViewById(R.id.btnLogin);

        username = (EditText) view.findViewById(R.id.txtUsername);
        password = (EditText) view.findViewById(R.id.txtPassword);

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pswd = password.getText().toString().trim();
                if(user.isEmpty()){
                    username.setError("Username tidak boleh kosong");
                }else if(pswd.isEmpty()){
                    password.setError("Password tidak boleh kosong");
                }else if(user.length() < 5) {
                    username.setError("Username minimal 5 karakter");
                }else if(!cekKarakterUnik(user)){
                    username.setError("Symbol hanya boleh titik . atau garis bawah _ setelah huruf");
                }else if(pswd.length() < 5){
                    password.setError("Password minimal 5 karakter");
                }else{
                    new LoginAsyncTask().execute(user, pswd) ;
                }
            }
        });
        return view;
    }

    private boolean cekKarakterUnik(String s){
        return s.matches("^[a-z A-Z]+[[._][a-zA-Z_0-9]]*");
    }
        //berfungsi untuk login ke database
    private class LoginAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
                    String email = object.getJSONObject("data").getString("email") ;
                    String no_hp = object.getJSONObject("data").getString("no_hp") ;
                    String jenis_kelamin = object.getJSONObject("data").getString("jenis_kelamin") ;

                    //pengisian data idUser, username, password ke Session...
                    Session.createLoginSession(getActivity(),idUser, username, password, email, no_hp, jenis_kelamin);

                    //Toast.makeText(Login2Activity.this, "Pindah ke BerandaActivity",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), BerandaActivity.class));
                    getActivity().finish();

                } else {
                    Toast.makeText(getActivity(), "Username atau Password anda salah", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
//                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("masuk","-> "+e.getMessage()) ;
            }
        }
    }
}
