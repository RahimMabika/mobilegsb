package com.example.connexion_gsb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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

public class VisiteurActivity extends AppCompatActivity {

    TextView textViewWelcome;
    RecyclerView recyclerView;
    ArrayList<FicheFrais> ficheList;
    FicheAdapter ficheAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiteur);

        textViewWelcome = findViewById(R.id.textViewWelcome);
        recyclerView = findViewById(R.id.recyclerViewFiches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ficheList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String email = prefs.getString("email", "???");
        String userId = prefs.getString("id", null);
        Log.d("DEBUG_PREFS", "id = " + userId);  // Pour vérifier si l'id est bien lu

        textViewWelcome.setText("Bienvenue " + email);

        requestQueue = Volley.newRequestQueue(this);

        if (userId != null) {
            loadFiches(userId);
        } else {
            Toast.makeText(this, "Erreur : ID utilisateur manquant", Toast.LENGTH_LONG).show();
        }
    }

    private void loadFiches(String userId) {
        String url = "http://10.0.2.2/applimobile/getFiches.php?user_id=" + userId;
        Log.d("DEBUG_URL", "URL = " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getInt("status") == 200) {
                            JSONArray fichesArray = response.getJSONArray("fiches");

                            for (int i = 0; i < fichesArray.length(); i++) {
                                JSONObject fiche = fichesArray.getJSONObject(i);
                                int id = fiche.getInt("id");
                                String date = fiche.getString("date");
                                float montant = (float) fiche.getDouble("montant");
                                String statut = fiche.getString("statut");

                                ficheList.add(new FicheFrais(id, date, montant, statut));
                            }

                            ficheAdapter = new FicheAdapter(ficheList, VisiteurActivity.this);
                            recyclerView.setAdapter(ficheAdapter);

                        } else {
                            Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                        Log.e("JSON", e.toString());
                    }
                },
                error -> {
                    Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", error.toString());
                });

        requestQueue.add(request);
    }
}
