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

public class FraisHorsForfaitAdapter extends RecyclerView.Adapter<FraisHorsForfaitAdapter.FraisViewHolder> {

    private final List<DetailFrais> fraisList;
    private final Context context;

    public FraisHorsForfaitAdapter(List<DetailFrais> fraisList, Context context) {
        this.fraisList = fraisList;
        this.context = context;
    }

    @NonNull
    @Override
    public FraisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_frais_hors_forfait, parent, false);
        return new FraisViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FraisViewHolder holder, int position) {
        DetailFrais frais = fraisList.get(position);

        holder.categorie.setText("Catégorie : " + frais.categorie);
        holder.montant.setText("Montant : " + frais.montant + " €");
        holder.date.setText("Date : " + frais.date);

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
            // À remplacer par une redirection vers un écran de modification
        });
    }

    @Override
    public int getItemCount() {
        return fraisList.size();
    }

    public static class FraisViewHolder extends RecyclerView.ViewHolder {
        TextView categorie, montant, date;
        Button btnModifier, btnSupprimer;

        public FraisViewHolder(@NonNull View itemView) {
            super(itemView);
            categorie = itemView.findViewById(R.id.textCategorieHF);
            montant = itemView.findViewById(R.id.textMontantHF);
            date = itemView.findViewById(R.id.textDateHF);
            btnModifier = itemView.findViewById(R.id.btnModifierHF);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimerHF);
        }
    }
}
