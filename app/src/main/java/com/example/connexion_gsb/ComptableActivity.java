package com.example.connexion_gsb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComptableActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button buttonToutesFiches, buttonFichesAttente;
    ArrayList<FicheFrais> ficheList;
    FicheComptableAdapter adapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptable);

        recyclerView = findViewById(R.id.recyclerViewComptable);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ficheList = new ArrayList<>();
        adapter = new FicheComptableAdapter(ficheList, this);
        recyclerView.setAdapter(adapter);

        buttonToutesFiches = findViewById(R.id.buttonToutesFiches);
        buttonFichesAttente = findViewById(R.id.buttonFichesAttente);

        requestQueue = Volley.newRequestQueue(this);

        buttonToutesFiches.setOnClickListener(v -> chargerFiches("toutes"));
        buttonFichesAttente.setOnClickListener(v -> chargerFiches("attente"));

        chargerFiches("toutes");
    }

    private void chargerFiches(String filtre) {
        String url = "http://10.0.2.2/applimobile/getFichesComptable.php?filtre=" + filtre;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    ficheList.clear();
                    try {
                        if (response.getInt("status") == 200) {
                            JSONArray fiches = response.getJSONArray("fiches");

                            for (int i = 0; i < fiches.length(); i++) {
                                JSONObject fiche = fiches.getJSONObject(i);
                                int id = fiche.getInt("id");
                                String date = fiche.getString("date");
                                float montant = (float) fiche.getDouble("montant");
                                String statut = fiche.getString("statut");
                                String email = fiche.getString("email");

                                ficheList.add(new FicheFrais(id, date, montant, statut, email));
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Aucune fiche trouvée", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show());

        requestQueue.add(request);
    }
}
