
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
import android.widget.EditText;
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
    public static SignInFragment newInstance(LoginActivity activity){
        loginActivity = activity;
        return new SignInFragment();
    }
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View view = LayoutInflater.from(loginActivity).inflate(R.layout.fragment_sign_in, container,  false);
        return view;
    }
}
