package com.tranhulovu.doop.applicationui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationcontrol.Authenticator;

public class AuthenticatorFragment extends ManagerFragment implements View.OnClickListener {
    private NavController navController;
    private Authenticator mAuthenticator;

    private TextInputEditText mSignInUsernameView;
    private TextInputEditText mSignInPasswordView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.authenticatorfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuthenticator = MainActivity.getAuthenticator();
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.authentication_signInButton).setOnClickListener(this);
        view.findViewById(R.id.authentication_signUpButton).setOnClickListener(this);
        mSignInUsernameView = (TextInputEditText) view.findViewById(R.id.authentication_username);
        mSignInPasswordView = (TextInputEditText) view.findViewById(R.id.authentication_password);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.authentication_signInButton)
        {
            String email = mSignInUsernameView.getText().toString();
            String password = mSignInPasswordView.getText().toString();;
            mAuthenticator.requestSignIn(email, password);
            if (mAuthenticator.getSignInState() == Authenticator.SignInState.SIGNED_IN) {
                navController.navigate(R.id.action_authenticatorFragment_to_mainFragment);
            }
        } else if (view.getId() == R.id.authentication_signUpButton)
        {
            navController.navigate(R.id.action_authenticatorFragment_to_signupFragment);
        }
    }
}
