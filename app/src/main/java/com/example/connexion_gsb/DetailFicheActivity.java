package com.example.connexion_gsb;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailFicheActivity extends AppCompatActivity {

    TextView textTitre;
    Button boutonRetour, boutonAjoutForfait, boutonAjoutHorsForfait;

    RecyclerView recyclerViewForfait, recyclerViewHorsForfait;
    ArrayList<DetailFrais> forfaitList = new ArrayList<>();
    ArrayList<DetailFrais> horsForfaitList = new ArrayList<>();
    FraisForfaitaireAdapter forfaitAdapter;
    FraisHorsForfaitAdapter horsForfaitAdapter;

    int ficheId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fiche);

        textTitre = findViewById(R.id.textFicheTitre);
        boutonRetour = findViewById(R.id.boutonRetour);
        boutonAjoutForfait = findViewById(R.id.btnAjoutForfait);
        boutonAjoutHorsForfait = findViewById(R.id.btnAjoutHorsForfait);

        recyclerViewForfait = findViewById(R.id.recyclerViewForfaitaires);
        recyclerViewHorsForfait = findViewById(R.id.recyclerViewHorsForfait);

        recyclerViewForfait.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHorsForfait.setLayoutManager(new LinearLayoutManager(this));

        forfaitAdapter = new FraisForfaitaireAdapter(forfaitList, this);
        horsForfaitAdapter = new FraisHorsForfaitAdapter(horsForfaitList, this);

        recyclerViewForfait.setAdapter(forfaitAdapter);
        recyclerViewHorsForfait.setAdapter(horsForfaitAdapter);

        ficheId = getIntent().getIntExtra("fiche_id", -1);

        if (ficheId != -1) {
            textTitre.setText("Détails de la fiche n°" + ficheId);
            loadDetails(ficheId);
        } else {
            Toast.makeText(this, "Fiche introuvable", Toast.LENGTH_SHORT).show();
        }

        boutonRetour.setOnClickListener(v -> finish());

        boutonAjoutForfait.setOnClickListener(v -> {
            Intent intent = new Intent(this, AjoutFraisForfaitaireActivity.class);
            intent.putExtra("fiche_id", ficheId);
            startActivity(intent);
        });

        boutonAjoutHorsForfait.setOnClickListener(v -> {
            Intent intent = new Intent(this, AjoutFraisHorsForfaitActivity.class);
            intent.putExtra("fiche_id", ficheId);
            startActivity(intent);
        });
    }

    private void loadDetails(int ficheId) {
        String url = "http://10.0.2.2/applimobile/getDetailsFiche.php?fiche_id=" + ficheId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getInt("status") == 200) {
                            forfaitList.clear();
                            horsForfaitList.clear();

                            JSONArray details = response.getJSONArray("frais");

                            for (int i = 0; i < details.length(); i++) {
                                JSONObject f = details.getJSONObject(i);
                                int id = f.getInt("id");
                                String cat = f.getString("categorie");
                                double montant = f.getDouble("montant");
                                String date = f.optString("date", null);
                                String type = f.getString("type");

                                DetailFrais frais = new DetailFrais(id, cat, montant, date);

                                if (type.equalsIgnoreCase("autres")) {
                                    horsForfaitList.add(frais);
                                } else {
                                    forfaitList.add(frais);
                                }
                            }

                            forfaitAdapter.notifyDataSetChanged();
                            horsForfaitAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Aucun détail trouvé", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deconnexion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            getSharedPreferences("session", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
