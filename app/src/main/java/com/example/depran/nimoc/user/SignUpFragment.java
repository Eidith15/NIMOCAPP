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
import android.widget.Toast;

import com.example.depran.nimoc.R;
import com.example.depran.nimoc.buku.BerandaActivity;
import com.example.depran.nimoc.utils.Constrants;
import com.example.depran.nimoc.utils.CustomHttpClient;
import com.example.depran.nimoc.utils.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Eidith on 21/04/2018.
 */

public class SignUpFragment extends Fragment {

    public static LoginActivity loginActivity;
    private Button btnGoSignUp;
    private EditText username;
    private EditText password;
    private EditText rePassword;

    public static SignUpFragment newInstance(LoginActivity activity) {
        loginActivity = activity;
        return new SignUpFragment();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(loginActivity).inflate(R.layout.fragment_sign_up, container, false);
        username = (EditText) view.findViewById(R.id.txtSignUpUsername);
        password = (EditText) view.findViewById(R.id.txtSignUpPassword);
        rePassword = (EditText) view.findViewById(R.id.txtRePassword);
        btnGoSignUp = (Button) view.findViewById(R.id.btnSignUp);
        btnGoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().trim();
                String pswd = password.getText().toString().trim();
                String rePswd = rePassword.getText().toString().trim();
                if (pswd.equals(rePswd)){
                    new SignUpAyncTask().execute(user, pswd);
                }else{
                    rePassword.setError("password tidak sama");
                }
            }
        });
        return view;
    }

    //berfungsi untuk login ke database
    private class SignUpAyncTask extends AsyncTask<String, String, String> {

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
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("action", "register"));
                postParameters.add(new BasicNameValuePair("username", params[0]));
                postParameters.add(new BasicNameValuePair("password", params[1]));

                respon = CustomHttpClient.executeHttpPost(url, postParameters);

            } catch (Exception e) {
                respon = e.toString();
            }
            return respon;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.e("Respon", " -> " + result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getString("status").equalsIgnoreCase("success")) {

                    Toast.makeText(getActivity(), "Akun anda telah berhasil di daftarkan", Toast.LENGTH_LONG).show();

                } else {
//                    Toast.makeText(getActivity(), "Username atau Password anda salah", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                Log.e("daftar", "-> " + e.getMessage());
            }
        }
    }
}
