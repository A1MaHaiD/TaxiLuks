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


public class PassengerSingInActivity extends AppCompatActivity {

    private static final String TAG = "PassengerSingInActivity";

    private TextInputLayout passengerTextInputEmail;
    private TextInputLayout passengerTextInputName;
    private TextInputLayout passengerTextInputPassword;
    private TextInputLayout passengerTextInputConfirmPassword;

    private Button goButton;
    private TextView toggleLoginSingUpPassengerTextView;

    private boolean isLoginModeActive;


    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_sing_in);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(PassengerSingInActivity.this,
                    PassengerMapsActivity.class));
        }

        passengerTextInputEmail = findViewById(R.id.passengerTextInputEmail);
        passengerTextInputName = findViewById(R.id.passengerTextInputName);
        passengerTextInputPassword = findViewById(R.id.passengerTextInputPassword);
        passengerTextInputConfirmPassword = findViewById(R.id.passengerTextInputConfirmPassword);


        goButton = findViewById(R.id.goButton);
        toggleLoginSingUpPassengerTextView = findViewById(R.id.togglePassengerLoginSingUpTextView);
    }

    private boolean validateName() {
        String nameInput = passengerTextInputName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            passengerTextInputName.setError("Please input your name");
            return true;
        } else if (nameInput.length() > 15) {
            passengerTextInputName.setError("Please enter a name shortly ");
            return true;
        } else {
            passengerTextInputName.setError("");
            return false;
        }
    }

    private boolean validateEmail() {
        String emailInput = passengerTextInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            passengerTextInputEmail.setError("Please input your email");
            return true;
        } else {
            passengerTextInputEmail.setError("");
            return false;
        }
    }


    private boolean validatePassword() {
        String passwordInput = passengerTextInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            passengerTextInputPassword.setError("Please input your password");
            return true;
        } else if (passwordInput.length() < 7) {
            passengerTextInputPassword.setError("Password length have to be more than 6");
            return true;
        } else {
            passengerTextInputPassword.setError("");
            return false;
        }
    }

    private boolean validateConfirmPassword() {
        String passwordInput = passengerTextInputPassword.getEditText().getText().toString().trim();
        String confirmPasswordInput = passengerTextInputConfirmPassword.getEditText().getText().toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            passengerTextInputPassword.setError("Password have to match");
            return false;
        } else {
            passengerTextInputPassword.setError("");
            return true;
        }
    }

    @SuppressLint("SetTextI18n")
    public void togglePassengerLoginSingUp(View view) {
        if (isLoginModeActive) {
            isLoginModeActive = false;
            goButton.setText("Sing Up ");
            toggleLoginSingUpPassengerTextView.setText("Or, log in");
            passengerTextInputConfirmPassword.setVisibility(View.VISIBLE);
            passengerTextInputName.setVisibility(View.VISIBLE);
        } else {
            isLoginModeActive = true;
            goButton.setText("Log In");
            toggleLoginSingUpPassengerTextView.setText("Or, sign in");
            passengerTextInputConfirmPassword.setVisibility(View.INVISIBLE);
            passengerTextInputName.setVisibility(View.INVISIBLE);
        }
    }


    public void passengerCreateLogIn(View view) {
        if (validateEmail() | validatePassword()) {
            return;
        }
        if (isLoginModeActive) {
            auth.signInWithEmailAndPassword(
                    passengerTextInputEmail.getEditText().getText().toString().trim(),
                    passengerTextInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                startActivity(new Intent(PassengerSingInActivity.this,
                                        PassengerMapsActivity.class));
                                //      updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(PassengerSingInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                                // ...
                            }
                            // ...
                        }
                    });
        } else {
            if (validateEmail() | validateName() | validatePassword() | !validateConfirmPassword()) {
                return;
            }
            auth.createUserWithEmailAndPassword(
                    passengerTextInputEmail.getEditText().getText().toString().trim(),
                    passengerTextInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmailP:success");
                                FirebaseUser user = auth.getCurrentUser();
                                startActivity(new Intent(PassengerSingInActivity.this,
                                        PassengerMapsActivity.class));
                                //  updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmailP:failure", task.getException());
                                Toast.makeText(PassengerSingInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                        }
                    });
        }

    }
}