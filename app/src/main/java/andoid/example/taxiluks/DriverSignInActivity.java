package andoid.example.taxiluks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DriverSignInActivity extends AppCompatActivity {

    private static final String TAG = "DriverSignInActivity";

    private TextInputLayout driverTextInputEmail;
    private TextInputLayout driverTextInputName;
    private TextInputLayout driverTextInputPassword;
    private TextInputLayout driverTextInputConfirmPassword;

    private Button loginSingUpButtonDriver;
    private TextView toggleLoginSingUpPassengerTextView;

    private boolean isLoginModeActive;

    private FirebaseAuth auth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_in);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(DriverSignInActivity.this,
                    DriverMapsActivity.class));
        }

        driverTextInputEmail = findViewById(R.id.driverTextInputEmail);
        driverTextInputName = findViewById(R.id.driverTextInputName);
        driverTextInputPassword = findViewById(R.id.driverTextInputPassword);
        driverTextInputConfirmPassword = findViewById(R.id.driverTextInputConfirmPassword);

        loginSingUpButtonDriver = findViewById(R.id.loginDriverSingUpButton);
        toggleLoginSingUpPassengerTextView = findViewById(R.id.toggleDriverLoginSingUpTextView);
    }

    private boolean validateEmail() {
        String emailInput = driverTextInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            driverTextInputEmail.setError("Please input your email");
            return true;
        } else {
            driverTextInputEmail.setError("");
            return false;
        }
    }


    private boolean validateName() {
        String nameInput = driverTextInputName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            driverTextInputName.setError("Please input your email");
            return true;
        } else if (nameInput.length() > 15) {
            driverTextInputName.setError("Name length have to be less than 15");
            return true;
        } else {
            driverTextInputName.setError("");
            return false;
        }
    }

    private boolean validatePassword() {
        String passwordInput = driverTextInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            driverTextInputPassword.setError("Please input your password");
            return true;
        } else if (passwordInput.length() < 7) {
            driverTextInputPassword.setError("Password length have to be more than 6");
            return true;
        } else {
            driverTextInputPassword.setError("");
            return false;
        }
    }

    private boolean validateConfirmPassword() {
        String passwordInput = driverTextInputPassword.getEditText().getText().toString().trim();
        String confirmPasswordInput = driverTextInputConfirmPassword.getEditText().getText().toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            driverTextInputPassword.setError("Password have to match");
            return false;
        } else {
            driverTextInputPassword.setError("");
            return true;
        }
    }

    public void loginSignUpDriver(View view) {
        if (validateEmail() | validatePassword()) {
            return;
        }
        if (isLoginModeActive) {
            auth.signInWithEmailAndPassword(
                    driverTextInputEmail.getEditText().getText().toString().trim(),
                    driverTextInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                startActivity(new Intent(DriverSignInActivity.this,
                                        DriverMapsActivity.class));
                                FirebaseUser user = auth.getCurrentUser();
                                //      updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(DriverSignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                                // ...
                            }
                            // ...
                        }
                    });
        } else {
            if (validateName() | validateEmail() | validatePassword() | !validateConfirmPassword()) {
                return;
            }
            auth.createUserWithEmailAndPassword(
                    driverTextInputEmail.getEditText().getText().toString().trim(),
                    driverTextInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                startActivity(new Intent(DriverSignInActivity.this,
                                        DriverMapsActivity.class));
                                FirebaseUser user = auth.getCurrentUser();
                                //  updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(DriverSignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                        }
                    });
        }

    }

    @SuppressLint("SetTextI18n")
    public void toggleDriverLoginSingUp(View view) {
        if (isLoginModeActive) {
            isLoginModeActive = false;
            loginSingUpButtonDriver.setText("Sing Up");
            toggleLoginSingUpPassengerTextView.setText("Or, log in");
            driverTextInputConfirmPassword.setVisibility(View.VISIBLE);
            driverTextInputName.setVisibility(View.VISIBLE);
        } else {
            isLoginModeActive = true;
            loginSingUpButtonDriver.setText("Log In");
            toggleLoginSingUpPassengerTextView.setText("Or, sign in");
            driverTextInputConfirmPassword.setVisibility(View.INVISIBLE);
            driverTextInputName.setVisibility(View.INVISIBLE);
        }
    }
}