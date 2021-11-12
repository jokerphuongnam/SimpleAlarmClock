package com.learntodroid.simplealarmclock.activities.userinfodialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.activities.LaunchActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;


public class UserInfoFragment extends DialogFragment {

    public UserInfoFragment() {

    }

    private FirebaseUser currentUser;
    private UserInfoViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();
        String displayName = currentUser.getDisplayName();
        Uri photoUrl = currentUser.getPhotoUrl();
        ((TextView) view.findViewById(R.id.userEmail)).setText(email);
        ((TextView) view.findViewById(R.id.username)).setText(displayName);
        if (photoUrl != null) {
            Picasso.get().load(photoUrl.toString()).transform(new CircleTransform()).into(((ImageView) view.findViewById(R.id.userAvartar)));
        }
        view.findViewById(R.id.signOutBtn).setOnClickListener(view1 -> {
            AuthUI.getInstance()
                    .signOut(requireActivity())
                    .addOnCompleteListener(task -> {
                        viewModel.onClearListener();
                        Intent intent = new Intent(requireActivity(), LaunchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
        });
    }
}