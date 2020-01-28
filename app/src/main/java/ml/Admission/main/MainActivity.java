package ml.Admission.main;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ml.Admission.main.Interface.ItemClickListener;
import ml.Admission.main.Model.Student;
import ml.Admission.main.ViewHolder.StudentViewHolder;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout emptyField;

    FirebaseRecyclerAdapter<Student, StudentViewHolder> adapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_name);
        actionBar.setTitle(" ... "+"Admission2k19");

        recycler = findViewById(R.id.recyclerAdmission);
        emptyField = findViewById(R.id.emptyField);


        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("Please Wait...");
        mDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    recycler.setVisibility(View.VISIBLE);
                    emptyField.setVisibility(View.GONE);
                    loadAdmission();
                    mDialog.dismiss();
                }else {
                    recycler.setVisibility(View.GONE);
                    emptyField.setVisibility(View.VISIBLE);
                    mDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadAdmission() {

            recycler.setVisibility(View.VISIBLE);
            emptyField.setVisibility(View.GONE);

            FirebaseRecyclerOptions<Student> options = new FirebaseRecyclerOptions.Builder<Student>()
                    .setQuery(reference, Student.class)
                    .build();

            adapter = new FirebaseRecyclerAdapter<Student, StudentViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int i, @NonNull Student student) {
                    studentViewHolder.txtName.setText(student.getName());
                    studentViewHolder.txtCourse.setText(student.getCourse());
                    studentViewHolder.txtEmail.setText(student.getEmail());
                    studentViewHolder.txtPhone.setText(student.getPhone());
                    studentViewHolder.txtAddress.setText(student.getAddress());

                    studentViewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            //Todo
                        }
                    });
                }

                @NonNull
                @Override
                public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.card_layout, parent, false);
                    return new StudentViewHolder(itemView);
                }
            };
            adapter.startListening();
            recycler.setAdapter(adapter);


    }

    @Override
    protected void onStop(){
        super.onStop();
        if(adapter==null){
            recycler.setVisibility(View.GONE);
            emptyField.setVisibility(View.VISIBLE);
        }else {
            adapter.stopListening();
        }
    }
    @Override
    protected void onStart(){
        super.onStart();

        if(adapter==null){
            recycler.setVisibility(View.GONE);
            emptyField.setVisibility(View.VISIBLE);
        }else {
            adapter.startListening();
        }

    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals("Delete")){
            delete(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void delete(String key) {
        reference.child(key).removeValue();
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }


}

