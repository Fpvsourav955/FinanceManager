package com.sourav.financemanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    TextView signuplogin;
    public  static  final  int TIMER=2000;
    TextView signuptext;
    RelativeLayout signuplayout;
    private LoadingDialog loadingDialog;
    LottieAnimationView signup_animation;
    private EditText editTextName, editTextEmail, editTextPassword, editTextRePassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_sign_up);

        signuplogin = findViewById(R.id.signuplogin);
        signuplayout = findViewById(R.id.sign_up);
        signuptext = findViewById(R.id.sign_up_text);
        loadingDialog = new LoadingDialog(SignUp.this);
        signup_animation = findViewById(R.id.signup_animation);
        signuplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.signup_name);
        editTextEmail = findViewById(R.id.signup_email);
        editTextPassword = findViewById(R.id.signup_password);
        editTextRePassword = findViewById(R.id.signup_repassword);


        signuplayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                registerUser();
            }

        });


        getWindow().getDecorView().post(() -> {
            WindowInsetsControllerCompat controller =
                    new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            controller.setAppearanceLightStatusBars(true);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        int nightModeFlags =
                getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            controller.setAppearanceLightStatusBars(false);
            controller.setAppearanceLightNavigationBars(false);
        } else {
            controller.setAppearanceLightStatusBars(true);
            controller.setAppearanceLightNavigationBars(true);
        }
    }
        private void registerUser() {
            signup_animation.setVisibility(View.VISIBLE);
            signup_animation.playAnimation();
            signuptext.setVisibility(View.GONE);
            loadingDialog.startLoadingDiloag();

            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String rePassword = editTextRePassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                editTextName.setError("Name is required!");
                loadingDialog.dismissDialog();
                resetButton();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Email is required!");
                loadingDialog.dismissDialog();
                resetButton();
                return;
            }

            if (!email.toLowerCase().endsWith("@gmail.com")) {
                editTextEmail.setError("Only @gmail.com email allowed");
                Toast.makeText(this, "Please use your college email (example@gmail.com)", Toast.LENGTH_LONG).show();
                loadingDialog.dismissDialog();
                resetButton();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is required!");
                loadingDialog.dismissDialog();
                resetButton();
                return;
            }

            if (password.length() < 6) {
                editTextPassword.setError("Password must be at least 6 characters!");
                loadingDialog.dismissDialog();
                resetButton();
                return;
            }

            if (!password.equals(rePassword)) {
                editTextRePassword.setError("Passwords do not match!");
                loadingDialog.dismissDialog();
                resetButton();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        loadingDialog.dismissDialog();
                        resetButton();

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(profileTask -> {
                                            if (profileTask.isSuccessful()) {
                                                mAuth.signOut();
                                                startActivity(new Intent(SignUp.this, Login.class));
                                                sendLoginNotification();
                                                finish();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(SignUp.this, "Registration Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 101) {

            }
        }

        private void resetButton() {
            signup_animation.pauseAnimation();
            signup_animation.setVisibility(View.GONE);
            signuptext.setVisibility(View.VISIBLE);
        }
        private void sendLoginNotification() {
            String channelId = "SIGNUP_CHANNEL";
            String channelName = "Signup Notifications";


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                );

                notificationManager.createNotificationChannel(channel);
            }


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo1)
                    .setContentTitle("Welcome to Finance Manager")
                    .setContentText("Your account is ready. Start tracking your income and expenses today.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
            notificationManager.notify(1, builder.build());
        }
        }