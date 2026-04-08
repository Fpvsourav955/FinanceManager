package com.sourav.financemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private TextView name, email;
    private LinearLayout btnLogout;

    private FirebaseAuth mAuth;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.Profile_image);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.useremail);
        btnLogout = view.findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            String userName = user.getDisplayName();
            if (!TextUtils.isEmpty(userName)) {
                name.setText(userName);
            } else {
                name.setText("User");
            }

            String userEmail = user.getEmail();
            if (!TextUtils.isEmpty(userEmail)) {
                email.setText(userEmail);
            } else {
                email.setText("No Email");
            }

            if (user.getPhotoUrl() != null) {

                Glide.with(requireContext())
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.ic_profile)
                        .into(profileImage);
            } else {

                profileImage.setImageResource(R.drawable.ic_profile);
            }

        }
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();

            Intent intent = new Intent(requireContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });
        return view;
    }
}