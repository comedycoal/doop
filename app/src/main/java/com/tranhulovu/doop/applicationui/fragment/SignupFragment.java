package com.tranhulovu.doop.applicationui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;

public class SignupFragment extends ManagerFragment implements View.OnClickListener {
    private NavController navController;

    private TextInputEditText mSignUpUsernameView;
    private TextInputEditText mSignUpEmailView;
    private TextInputEditText mSignUpPasswordView;
    private TextInputEditText mSignUpPasswordConfirmView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signupfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.authentication_signUpButton).setOnClickListener(this);

        mSignUpUsernameView = (TextInputEditText) view.findViewById(R.id.authentication_signup_username);
        mSignUpEmailView = (TextInputEditText) view.findViewById(R.id.authentication_signup_email);
        mSignUpPasswordView = (TextInputEditText) view.findViewById(R.id.authentication_signup_password);
        mSignUpPasswordConfirmView = (TextInputEditText) view.findViewById(R.id.authentication_signup_confirmPassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.authentication_signUpButton:
            {
                String username = mSignUpUsernameView.getText().toString();
                String email = mSignUpEmailView.getText().toString();
                String password = mSignUpPasswordView.getText().toString();
                String password_confirm = mSignUpPasswordConfirmView.getText().toString();

                if (password != password_confirm)
                {
                    // Toast error
                    Toast.makeText(MainActivity.getInstance(), "Password does not match.", Toast.LENGTH_SHORT).show();

                    mSignUpPasswordView.setText("");
                    mSignUpPasswordConfirmView.setText("");;
                }
                else if (true)
                {
                    // Add handle cases blah blah
                    break; // Remove break please
                }
                else
                {
                    // Authenticate the user

                    navController.navigate(R.id.action_signupFragment_to_authenticatorFragment);
                }
                break;
            }
        }
    }
}
