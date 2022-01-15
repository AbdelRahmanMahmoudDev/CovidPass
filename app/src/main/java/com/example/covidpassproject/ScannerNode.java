package com.example.covidpassproject;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScannerNode {
    private static DatabaseReference m_database_reference;
    private static FirebaseAuth m_firebase_auth;

    public ScannerNode() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://covidtest-15516-default-rtdb.firebaseio.com/");
        m_database_reference = database.getReference(Scanner.class.getSimpleName());
        m_firebase_auth = FirebaseAuth.getInstance();
    }

    // Adds nodes to a database
    public Task<Void> add(Scanner scanner, String uid) {
        // push() automatically generates a UUID
        Task<Void> result = null;
        if(scanner != null) {
            result = m_database_reference.child(uid).setValue(scanner);
        }
        return result;
    }

    public FirebaseAuth GetFirebaseAuth() { return m_firebase_auth; }

    public DatabaseReference GetDatabaseReference() {
        return m_database_reference;
    }
}
