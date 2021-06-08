package com.example.reproductor.Services;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ServicesFirebase {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public ServicesFirebase(){
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference();
        this.storageReference = FirebaseStorage.getInstance().getReference();
    }
    public ServicesFirebase(String val  ){
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference(val);
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
