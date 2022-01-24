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
import com.tranhulovu.doop.MainActivity;
import com.tranhulovu.doop.R;
import com.tranhulovu.doop.applicationcontrol.Authenticator;

public class SignupFragment extends ManagerFragment implements View.OnClickListener {
    private NavController navController;
    private Authenticator mAuthenticator;

    private TextInputEditText mSignUpUsernameView;
    private TextInputEditText mSignUpEmailView;
    private TextInputEditText mSignUpPasswordView;
    private TextInputEditText mSignUpPasswordConfirmView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signupfragment, container, false);
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
        if (view.getId() == R.id.authentication_signUpButton) {
            String username = mSignUpUsernameView.getText().toString();
            String email = mSignUpEmailView.getText().toString();
            String password = mSignUpPasswordView.getText().toString();
            String password_confirm = mSignUpPasswordConfirmView.getText().toString();

            if (username.equals("")) {
                Toast.makeText(MainActivity.getInstance(), "Invalid username", Toast.LENGTH_SHORT).show();
            }
            else if (email.equals("") || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                Toast.makeText(MainActivity.getInstance(), "Invalid email address", Toast.LENGTH_SHORT).show();
                mSignUpEmailView.setText("");
            }
            else if (password.equals("") || !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$")) {
                Toast.makeText(MainActivity.getInstance(), "Password must contain at least 6 characters, one alphabet character and one number", Toast.LENGTH_SHORT).show();
                mSignUpPasswordView.setText("");
                mSignUpPasswordConfirmView.setText("");
            }
            else if (!password_confirm.equals(password)) {
                Toast.makeText(MainActivity.getInstance(), "Password do not match", Toast.LENGTH_SHORT).show();
                mSignUpPasswordConfirmView.setText("");
            }
            else {
                mAuthenticator = MainActivity.getAuthenticator();
                mAuthenticator.requestSignUp(email, password);
                if (mAuthenticator.getSignInState() == Authenticator.SignInState.SIGNED_UP) {
                    navController.popBackStack();
                }
            }
        }
    }
}
