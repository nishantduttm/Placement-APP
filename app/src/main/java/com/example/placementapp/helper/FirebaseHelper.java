package com.example.placementapp.helper;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public  abstract class FirebaseHelper {

    public static FirebaseDatabase firebaseDatabase;

    public static DatabaseReference getFirebaseReference(String path)
    {
        DatabaseReference databaseReference = null;
        if(path!=null && !path.isEmpty())
        {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference(path);
        }
        return databaseReference;
    }
}
