package com.comp7082.lifeaware;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

public class Patient implements Parcelable {
    private String id;
    private String name;
    private String age;
    private String caregiverPhone;
    private String caregiverId;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference myRef;

    public Patient(String id) {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef = database.getReference(id);
        CountDownLatch done = new CountDownLatch(4);
        myRef.child("id").addListenerForSingleValueEvent(new ValueEventListener()  {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 String id = dataSnapshot.getValue(String.class);
                 setId(id);
                 done.countDown();
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
     }) ;
        myRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                setName(name);
                done.countDown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("age").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String age = dataSnapshot.getValue(String.class);
                setAge(age);
                done.countDown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("caregiver").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String caregiverId = dataSnapshot.getValue(String.class);
                setCaregiverId(caregiverId);
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

        setCaregiverPhone();
    }

    protected Patient(Parcel in) {
        id = in.readString();
        name = in.readString();
        age = in.readString();
        caregiverPhone = in.readString();
        user = in.readParcelable(FirebaseUser.class.getClassLoader());
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    private void setUp() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef = database.getReference(user.getUid());
        CountDownLatch done = new CountDownLatch(4);
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
        myRef.child("age").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String age = dataSnapshot.getValue(String.class);
                setAge(age);
                done.countDown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.child("caregiver").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String caregiverId = dataSnapshot.getValue(String.class);
                setCaregiverId(caregiverId);
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

        setCaregiverPhone();
    }

    public Patient() {
        setUp();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCaregiverId(String id) {
        this.caregiverId = id;
    }
    public void setCaregiverPhone(String phone) {
        this.caregiverPhone = phone;
    }

    public String getCaregiverPhone() {
        return caregiverPhone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCaregiverPhone()
    {
        DatabaseReference caregiverRef;

        caregiverRef = database.getReference(caregiverId);
        CountDownLatch done = new CountDownLatch(1);
        caregiverRef.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String caregiverPhone = dataSnapshot.getValue(String.class);
                setCaregiverPhone(caregiverPhone);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(age);
        parcel.writeString(caregiverPhone);
        parcel.writeParcelable(user, i);
    }
}
