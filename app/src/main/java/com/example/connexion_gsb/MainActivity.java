package com.example.connexion_gsb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connexion...");
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(this);

        login.setOnClickListener(v -> {
            String userVar = email.getText().toString();
            String passVar = password.getText().toString();

            if (userVar.isEmpty()) {
                Toast.makeText(MainActivity.this, "Remplissez l'adresse mail", Toast.LENGTH_SHORT).show();
            } else if (passVar.isEmpty()) {
                Toast.makeText(MainActivity.this, "Remplissez le mot de passe", Toast.LENGTH_SHORT).show();
            } else {
                loginRequest(userVar, passVar);
            }
        });
    }

    private void loginRequest(String userVar, String passVar) {
        String monApi = "http://10.0.2.2/applimobile/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, monApi,
                response -> {
                    progressDialog.dismiss();

                    Log.d("REPONSE_API", response); // 🔍 Voir la réponse brute de l'API

                    try {
                        JSONObject json = new JSONObject(response);
                        int status = json.getInt("status");

                        if (status == 200) {
                            String id = json.optString("id", ""); // Si tu veux éviter l'erreur si l'id est absent
                            String role = json.getString("role");
                            String email = json.getString("email");

                            SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("id", id);
                            editor.putString("role", role);
                            editor.putString("email", email);
                            editor.apply();

                            switch (role) {
                                case "visiteur":
                                    startActivity(new Intent(MainActivity.this, VisiteurActivity.class));
                                    finish();
                                    break;
                                case "comptable":
                                    startActivity(new Intent(MainActivity.this, ComptableActivity.class));
                                    finish();
                                    break;
                                case "admin":
                                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                    finish();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Rôle inconnu", Toast.LENGTH_LONG).show();
                                    break;
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("REPONSE_API", "Erreur JSON : " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Erreur de traitement JSON", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Log.e("REPONSE_API", "Erreur réseau : " + error.toString());
                    Toast.makeText(MainActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userVar);
                params.put("password", passVar);
                return params;
            }
        };

        progressDialog.show();
        requestQueue.add(stringRequest);
    }
}
