package com.example.connexion_gsb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FraisForfaitaireAdapter extends RecyclerView.Adapter<FraisForfaitaireAdapter.FraisViewHolder> {

    private final List<DetailFrais> fraisList;
    private final Context context;

    public FraisForfaitaireAdapter(List<DetailFrais> fraisList, Context context) {
        this.fraisList = fraisList;
        this.context = context;
    }

    @NonNull
    @Override
    public FraisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_frais_forfaitaire, parent, false);
        return new FraisViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FraisViewHolder holder, int position) {
        DetailFrais frais = fraisList.get(position);

        holder.categorie.setText("Catégorie : " + frais.categorie);
        holder.montant.setText("Montant : " + frais.montant + " €");

        holder.btnSupprimer.setOnClickListener(v -> {
            String url = "http://10.0.2.2/applimobile/supprimerDetail.php";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        fraisList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, fraisList.size());
                        Toast.makeText(context, "Frais supprimé", Toast.LENGTH_SHORT).show();
                    },
                    error -> Toast.makeText(context, "Erreur suppression", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(frais.id));
                    return params;
                }
            };
            Volley.newRequestQueue(context).add(request);
        });

        holder.btnModifier.setOnClickListener(v -> {
            Toast.makeText(context, "Modification à venir", Toast.LENGTH_SHORT).show();
            // Tu peux ici déclencher une nouvelle activité de modification
        });
    }

    @Override
    public int getItemCount() {
        return fraisList.size();
    }

    public static class FraisViewHolder extends RecyclerView.ViewHolder {
        TextView categorie, montant;
        Button btnModifier, btnSupprimer;

        public FraisViewHolder(@NonNull View itemView) {
            super(itemView);
            categorie = itemView.findViewById(R.id.textCategorie);
            montant = itemView.findViewById(R.id.textMontant);
            btnModifier = itemView.findViewById(R.id.btnModifier);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
        }
    }
}
