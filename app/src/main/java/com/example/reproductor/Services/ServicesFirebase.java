package com.example.reproductor.Services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ServicesFirebase {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public ServicesFirebase(){
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference();
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }
    public ServicesFirebase(String val  ){
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference(val);
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    public void setFirebaseFirestore(FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public void setStorageReference(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public void setFirebaseDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
    public Object getImage(String carpeta){
        StorageReference ref = storageReference.child(carpeta+" Images/"+carpeta+".jpg");
        return ref;
    }
    public Object getImage(String carpeta, String nombre){
        StorageReference ref = storageReference.child(carpeta+" Images/"+nombre+".jpg");
        return ref;
    }
}
