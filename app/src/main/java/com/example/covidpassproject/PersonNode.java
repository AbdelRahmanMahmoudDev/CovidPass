package com.example.covidpassproject;

// This representa the node that Firebase interacts with

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PersonNode {
    private static DatabaseReference m_database_reference;
    private static FirebaseAuth m_firebase_auth;

    public PersonNode() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://covidtest-15516-default-rtdb.firebaseio.com/");
        m_database_reference = database.getReference(Person.class.getSimpleName());
        m_firebase_auth = FirebaseAuth.getInstance();
    }

    // Adds nodes to a database
    public Task<Void> add(Person person) {
        // push() automatically generates a UUID
        Task<Void> result = null;
        if(person != null) {
            result = m_database_reference.push().setValue(person);
        }
        else {
            // TODO: Log insertion error
        }
        return result;
    }

    // Updates a node in a database
    // This can be used to update the VaccinationStatus property
    public Task<Void> update(String key, HashMap<String, Object> property_data) {
        Task<Void> result = m_database_reference.child(key).updateChildren(property_data);
        return result;
    }

    public Task<AuthResult> AuthenticateEmailAndPassword(String email, String password) {
        Task<AuthResult> result = m_firebase_auth.signInWithEmailAndPassword(email, password);
        return result;
    }

    public FirebaseAuth GetFirebaseAuth() {
        FirebaseAuth result = m_firebase_auth;
        return result;
    }
}
