package tnc.emergency;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView selimage;
    private int PICK_IMAGE_REQUEST = 1;
    private StorageReference mstorage;
    public static final int GALLERY_INTENT = 2 ;
    TextView name , age , dob, bGroup;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mstorage = FirebaseStorage.getInstance().getReference();

        selimage=(CircleImageView)findViewById(R.id.selimage);
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        dob = (TextView) findViewById(R.id.dob);
        bGroup = (TextView) findViewById(R.id.bGroup);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    if (userSnapshot.getValue() != null) {
                        User user = userSnapshot.getValue(User.class);
                        Toast.makeText(ProfileActivity.this, user.getName(), Toast.LENGTH_SHORT).show();
                        user.setName(userSnapshot.child("Danyal Saif").child("name").getValue().toString());
                        name.setText(user.getName());
                        user.setAge(userSnapshot.child("Danyal Saif").child("age").getValue().toString());
                        age.setText(user.getAge());
                        user.setDob(userSnapshot.child("Danyal Saif").child("dob").getValue().toString());
                        dob.setText(user.getDob());
                        user.setbGroup(userSnapshot.child("Danyal Saif").child("bGroup").getValue().toString());
                        bGroup.setText(user.getbGroup());


                    }
                }

            }


//        selimage.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//// Show only images, no videos or anything else
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                // Always show the chooser (if there are multiple options available)
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//
//
//
//
//            }
//        });
@Override
public void onCancelled(DatabaseError databaseError) {
    // Getting Post failed, log a message
    Toast.makeText(ProfileActivity.this, String.valueOf(databaseError.toException()), Toast.LENGTH_SHORT).show();
    // ...
}
        };
        mDatabase.addValueEventListener(postListener);


        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

    }







//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri uri = data.getData();
//
//
//            StorageReference filepath = mstorage.child("Photos").child(uri.getLastPathSegment());
//            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(ProfileActivity.this, "Image Uploaded Successfully",Toast.LENGTH_LONG).show();
//
//                }
//            });
//
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//                CircleImageView imageView = (CircleImageView) findViewById(R.id.selimage);
//                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()== android.R.id.home);
        Intent intent = new Intent(ProfileActivity.this , mainuiactivoty.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
