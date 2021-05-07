package com.learntodroid.simplealarmclock.activities.userinfodialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.activities.LaunchActivity;
import com.squareup.picasso.Picasso;


public class UserInfoFragment extends DialogFragment {


    public UserInfoFragment() {

    }


    FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String email = currentUser.getEmail();
        String displayName = currentUser.getDisplayName();
        Uri photoUrl = currentUser.getPhotoUrl();
        ((TextView) view.findViewById(R.id.userEmail)).setText(email);
        ((TextView) view.findViewById(R.id.username)).setText(displayName);
        Picasso.get().load(photoUrl.toString()).into( ((ImageView) view.findViewById(R.id.userAvartar)));
        view.findViewById(R.id.signOutBtn).setOnClickListener(view1 -> {
            AuthUI.getInstance()
                    .signOut(requireActivity())
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(requireActivity(), LaunchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });

        });


    }
}