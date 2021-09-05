package com.example.prototipo.classes.utilitario;

import androidx.annotation.NonNull;

import com.example.prototipo.classes.objetos.Parque;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UtilitarioBanco {

    public static ArrayList download() {
        ArrayList<Parque> parques = new ArrayList<Parque>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("parques").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dn : snapshot.getChildren()) {
                    Parque p = (Parque) dn.getValue(Parque.class);
                    parques.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return parques;
    }
}
