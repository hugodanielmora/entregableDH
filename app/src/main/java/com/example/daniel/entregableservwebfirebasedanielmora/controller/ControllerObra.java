package com.example.daniel.entregableservwebfirebasedanielmora.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.daniel.entregableservwebfirebasedanielmora.model.dao.DaoObraDeArte;
import com.example.daniel.entregableservwebfirebasedanielmora.model.pojo.ObraDeArte;
import com.example.daniel.entregableservwebfirebasedanielmora.utils.ResultListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ControllerObra {

    private Context context;
    //estos atributos solo se utilizan para el pedido con firebase
    private static final String PINTURAS = "paints";
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public ControllerObra(Context context) {
        this.context = context;
    }

    private boolean hayInternet() {
        return true;
    }

    public void obtenerObrasOnLine(final ResultListener<List<ObraDeArte>> resultListenerDeLaVista) {
        if (hayInternet()) {

            //opcion 1 hace el pedido a la api con retrofit: el json contiene datos incorrectos

//            DaoObraDeArte daoObraDeArte = new DaoObraDeArte();
//            daoObraDeArte.obtenerObrasDeArte(new ResultListener<List<ObraDeArte>>() {
//                @Override
//                public void finish(List<ObraDeArte> resultado) {
//                    resultListenerDeLaVista.finish(resultado);
//                }
//            });

            //opcion 2 para descargar el contenido correcto desde firebase

            database = FirebaseDatabase.getInstance();
            reference = database.getReference().child(PINTURAS);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        return;
                    }
                    List<ObraDeArte> artes = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ObraDeArte obraDeArte = snapshot.getValue(ObraDeArte.class);
                        artes.add(obraDeArte);
                        resultListenerDeLaVista.finish(artes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Fallo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
