package com.hamzashami.coronaproject.fragments.mainFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hamzashami.coronaproject.LoginActivity;
import com.hamzashami.coronaproject.MainActivity;
import com.hamzashami.coronaproject.R;
import com.hamzashami.coronaproject.model.User;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private StorageReference mStorageRef;

    private CircleImageView profile_image;
    private FloatingActionButton fab_editImage;
    private TextView tv_name, tv_username, tv_email;
    private Button btn_changePassword, btn_changeName, btn_logout;

    private String userId;
    private User currentUser;
    private FirebaseUser firebaseUser;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_image = view.findViewById(R.id.profile_image);
        tv_username = view.findViewById(R.id.tv_username);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);

        fab_editImage = view.findViewById(R.id.fab_editImage);
        btn_changePassword = view.findViewById(R.id.btn_changePassword);
        btn_changeName = view.findViewById(R.id.btn_changeName);
        btn_logout = view.findViewById(R.id.btn_logout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                if (currentUser == null) {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentUser.getImageUrl().equalsIgnoreCase(getString(R.string.no_image)) || TextUtils.isEmpty(currentUser.getImageUrl()) || currentUser.getImageUrl().equalsIgnoreCase("")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(currentUser.getImageUrl()).into(profile_image);
                }
                tv_name.setText("Name: " + currentUser.getName());
                tv_username.setText("Username: " + currentUser.getUsername());
                tv_email.setText("Email: " + currentUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        initClicks();
    }

    private void initClicks() {
        fab_editImage.setOnClickListener(v -> {
            pickImage();
        });

        btn_changePassword.setOnClickListener(v -> {
            displayChangePasswordDialog();
        });

        btn_changeName.setOnClickListener(v -> {
            displayChangeNameDialog();
        });

        btn_logout.setOnClickListener(v -> {
            displayLogoutDialog();
        });
    }

    private void displayLogoutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Logout");
        alert.setMessage("Are you sure you need to logout?");

        alert.setPositiveButton("Logout", (dialog, whichButton) -> {
            auth.signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            dialog.dismiss();
        });
        alert.show();
    }

    private void displayChangeNameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_name, null);
        EditText et_newName = view.findViewById(R.id.et_newName);
        alert.setTitle("Change Name: " + currentUser.getName());

        alert.setView(view);

        alert.setPositiveButton("Save", (dialog, whichButton) -> {
            String newNameString = et_newName.getText().toString();
            userRef.child(userId).child("name").setValue(newNameString).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    tv_name.setText("Name: " + newNameString);
                    Toast.makeText(getContext(), "Successfully Update Name", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error Name not update", Toast.LENGTH_SHORT).show();
                }
            });
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            dialog.dismiss();
        });
        alert.show();
    }

    private void displayChangePasswordDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null);

        EditText et_currentPassword = view.findViewById(R.id.et_currentPassword);
        EditText et_newPassword = view.findViewById(R.id.et_newPassword);
        EditText et_confirmNewPassword = view.findViewById(R.id.et_confirmNewPassword);

        alert.setTitle("Change Password");

        alert.setView(view);

        alert.setPositiveButton("Save", (dialog, whichButton) -> {

            checkAndUpdatePassword(
                    et_currentPassword.getText().toString(),
                    et_newPassword.getText().toString(),
                    et_confirmNewPassword.getText().toString()
            );
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            dialog.dismiss();
        });
        alert.show();
    }

    private void checkAndUpdatePassword(String currentPassword, String newPassword, String confirmNewPassword) {
        Log.d(TAG, "checkAndUpdatePassword: ");
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

        firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (newPassword.equals(currentPassword)) {
                    Toast.makeText(getContext(), "Current Password and new Password can't be equals", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.equals(confirmNewPassword)) {
                    firebaseUser.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "Password updated");
                            Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Error password not updated");
                            Toast.makeText(getContext(), "Error password not updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Passwords not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Current Password mismatch", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error auth failed");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            // Setting image on image view using Bitmap
            Bitmap myBitmap = BitmapFactory.decodeFile(filePath.getPath());
            profile_image.setImageBitmap(myBitmap);
            uploadImage();
        }
    }

    void pickImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image ..."), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString());

            ref.putFile(filePath).addOnSuccessListener(
                    taskSnapshot -> {
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            userRef.child(userId).child("imageUrl").setValue(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            });
        }
    }
}
