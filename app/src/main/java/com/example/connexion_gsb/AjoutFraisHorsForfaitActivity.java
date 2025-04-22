package com.example.connexion_gsb;

import android.annotation.SuppressLint;
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

public class AjoutFraisHorsForfaitActivity extends AppCompatActivity {

    EditText editCategorie, editMontant, editDate;
    Button btnValider;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_frais_hors_forfait);

        editCategorie = findViewById(R.id.editCategorieHors);
        editMontant = findViewById(R.id.editMontantHors);
        editDate = findViewById(R.id.editDateHors);
        btnValider = findViewById(R.id.btnValiderHors);

        int ficheId = getIntent().getIntExtra("fiche_id", -1);

        btnValider.setOnClickListener(v -> {
            String cat = editCategorie.getText().toString().trim();
            String montant = editMontant.getText().toString().trim();
            String date = editDate.getText().toString().trim();

            if (cat.isEmpty() || montant.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://10.0.2.2/applimobile/ajoutFraisHorsForfait.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Toast.makeText(this, "Frais hors forfait ajouté", Toast.LENGTH_SHORT).show();
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
                    params.put("date", date);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);
        });
    }
}
