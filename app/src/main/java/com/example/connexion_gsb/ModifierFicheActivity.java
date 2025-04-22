package com.example.connexion_gsb;

import android.content.Intent;
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

public class ModifierFicheActivity extends AppCompatActivity {

    EditText editTextMois, editTextMontant;
    Button buttonValider;
    RequestQueue requestQueue;
    int ficheId; // récupéré via l'intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_fiche);

        editTextMois = findViewById(R.id.editTextMoisModif);
        editTextMontant = findViewById(R.id.editTextMontantModif);
        buttonValider = findViewById(R.id.buttonValiderModif);

        requestQueue = Volley.newRequestQueue(this);

        // Récupère les infos envoyées
        Intent intent = getIntent();
        ficheId = intent.getIntExtra("fiche_id", -1);
        String mois = intent.getStringExtra("mois");
        String montant = intent.getStringExtra("montant");

        // Affiche les valeurs actuelles
        editTextMois.setText(mois);
        editTextMontant.setText(montant);

        buttonValider.setOnClickListener(v -> {
            String nouveauMois = editTextMois.getText().toString().trim();
            String nouveauMontant = editTextMontant.getText().toString().trim();

            if (nouveauMois.isEmpty() || nouveauMontant.isEmpty()) {
                Toast.makeText(this, "Champs requis", Toast.LENGTH_SHORT).show();
                return;
            }

            envoyerModification(ficheId, nouveauMois, nouveauMontant);
        });
    }

    private void envoyerModification(int ficheId, String mois, String montant) {
        String url = "http://10.0.2.2/applimobile/modifierFiche.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Fiche modifiée", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, VisiteurActivity.class));
                    finish();
                },
                error -> Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(ficheId));
                params.put("mois", mois);
                params.put("montant_total", montant);
                return params;
            }
        };

        requestQueue.add(request);
    }
}
