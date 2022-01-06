package com.example.covidpassproject;

// This representa the node that Firebase interacts with

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonNode {
    private DatabaseReference m_database_reference;

    public PersonNode() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        m_database_reference = database.getReference(Person.class.getSimpleName());
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
}
