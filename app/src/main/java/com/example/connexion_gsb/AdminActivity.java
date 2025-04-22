package com.example.connexion_gsb;

import android.os.Bundle;
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

public class AdminActivity extends AppCompatActivity {

    TextView textStats;
    RecyclerView recyclerViewUsers;
    ArrayList<User> userList;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        textStats = findViewById(R.id.textStats);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);
        recyclerViewUsers.setAdapter(userAdapter);

        chargerStatsEtUtilisateurs();
    }

    private void chargerStatsEtUtilisateurs() {
        String url = "http://10.0.2.2/applimobile/getStatsAndUsers.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getInt("status") == 200) {
                            //  Statistiques
                            int total = response.getInt("total");
                            int attente = response.getInt("attente");
                            int valides = response.getInt("validees");
                            int refuses = response.getInt("refusees");

                            String stats = "Total fiches : " + total +
                                    "\nEn attente : " + attente +
                                    "\nValidées : " + valides +
                                    "\nRefusées : " + refuses;

                            textStats.setText(stats);

                            //  Liste utilisateurs
                            JSONArray users = response.getJSONArray("users");
                            userList.clear();

                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                int id = user.getInt("id");
                                String email = user.getString("email");
                                String role = user.getString("role");

                                userList.add(new User(id, email, role));
                            }

                            userAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(request);
    }
}
