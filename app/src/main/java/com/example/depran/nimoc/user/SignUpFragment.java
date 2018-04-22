package com.example.depran.nimoc.user;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.depran.nimoc.R;

/**
 * Created by Eidith on 21/04/2018.
 */

public class SignUpFragment extends Fragment{

    public static LoginActivity loginActivity;
    public static SignUpFragment newInstance(LoginActivity activity){
        loginActivity = activity;
        return new SignUpFragment();
    }
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View view = LayoutInflater.from(loginActivity).inflate(R.layout.fragment_sign_up, container,  false);
        return view;
    }
}
