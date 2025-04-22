package com.example.connexion_gsb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AjoutFraisForfaitaireActivity extends AppCompatActivity {

    EditText editCategorie, editMontant;
    Button btnValider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_frais_forfaitaires);

        editCategorie = findViewById(R.id.editCategorie);
        editMontant = findViewById(R.id.editMontant);
        btnValider = findViewById(R.id.btnValiderFrais);

        int ficheId = getIntent().getIntExtra("fiche_id", -1);

        btnValider.setOnClickListener(v -> {
            String cat = editCategorie.getText().toString().trim();
            String montant = editMontant.getText().toString().trim();

            if (cat.isEmpty() || montant.isEmpty()) {
                Toast.makeText(this, "Champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://10.0.2.2/applimobile/ajoutFraisForfaitaire.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Toast.makeText(this, "Frais ajouté", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("fiche_id", String.valueOf(ficheId));
                    params.put("categorie", cat);
                    params.put("montant", montant);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);
        });
    }
}
