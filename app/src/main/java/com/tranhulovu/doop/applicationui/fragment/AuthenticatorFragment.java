package com.tranhulovu.doop.applicationui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tranhulovu.doop.R;

public class AuthenticatorFragment extends ManagerFragment implements View.OnClickListener {
    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authenticatorfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.authentication_signInButton).setOnClickListener(this);
        view.findViewById(R.id.authentication_signUpButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.authentication_signInButton:
                navController.navigate(R.id.action_authenticatorFragment_to_mainFragment);
                break;
            case R.id.authentication_signUpButton:
                navController.navigate(R.id.action_authenticatorFragment_to_signupFragment);
                break;
        }
    }
}
