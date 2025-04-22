package com.example.connexion_gsb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AjoutFicheActivity extends AppCompatActivity {

    EditText editTextMois, editTextMontant;
    Button buttonValider;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_fiche);

        editTextMois = findViewById(R.id.editTextMois);
        editTextMontant = findViewById(R.id.editTextMontant);
        buttonValider = findViewById(R.id.buttonValider);

        requestQueue = Volley.newRequestQueue(this);

        buttonValider.setOnClickListener(v -> {
            String mois = editTextMois.getText().toString().trim();
            String montant = editTextMontant.getText().toString().trim();

            if (mois.isEmpty() || montant.isEmpty()) {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
            String userId = prefs.getString("id", null);

            if (userId != null) {
                envoyerFiche(userId, mois, montant);
            } else {
                Toast.makeText(this, "Erreur utilisateur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void envoyerFiche(String userId, String mois, String montant) {
        String url = "http://10.0.2.2/applimobile/ajoutFiche.php";  // À créer côté PHP

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Fiche ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, VisiteurActivity.class));
                    finish();
                },
                error -> Toast.makeText(this, "Erreur serveur", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("mois", mois);
                params.put("montant_total", montant);
                return params;
            }
        };

        requestQueue.add(request);
    }
}
