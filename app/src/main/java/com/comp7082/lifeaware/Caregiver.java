package com.comp7082.lifeaware;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Caregiver implements Serializable {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private String id;
    private String name;
    private String phoneNumber;
    List<String> patientIds = new ArrayList<>();

    public Caregiver() {
        setUp();
    }
    public void setUp()
    {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        myRef = database.getReference(user.getUid());
        myRef.child("help").setValue("");
        CountDownLatch done = new CountDownLatch(3);
        myRef.child("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getValue(String.class);
                setId(id);
                done.countDown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   String name = dataSnapshot.getValue(String.class);
                   setName(name);
                   System.out.println(getName());
                   done.countDown();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        myRef.child("patientIds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String patientId = postSnapshot.getValue().toString();
                    patientIds.add(patientId);

                }
                done.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try {
            done.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getPatientIds()
    {
        return patientIds;
    }

    public void addPatientId(String patientId)
    {
        patientIds.add(patientId);
        myRef.child("patientIds").child(("" + patientId.length())).setValue(patientId);
    }

    public void setPatientIds(List<String> patientIds)
    {
        this.patientIds = patientIds;
    }


}
