package com.example.connexion_gsb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FicheAdapter extends RecyclerView.Adapter<FicheAdapter.FicheViewHolder> {

    private final List<FicheFrais> listeFiches;
    private final Context context;

    public FicheAdapter(List<FicheFrais> listeFiches, Context context) {
        this.listeFiches = listeFiches;
        this.context = context;
    }

    @NonNull
    @Override
    public FicheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fiche, parent, false);
        return new FicheViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FicheViewHolder holder, int position) {
        FicheFrais fiche = listeFiches.get(position);
        holder.date.setText("Date : " + fiche.date);
        holder.montant.setText("Montant : " + fiche.montant + " €");
        holder.statut.setText("Statut : " + fiche.statut);

        // Détails fiche
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFicheActivity.class);
            intent.putExtra("fiche_id", fiche.id);
            context.startActivity(intent);
        });

        // Supprimer fiche
        holder.supprimer.setOnClickListener(v -> {
            String url = "http://10.0.2.2/applimobile/supprimerFiche.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Toast.makeText(context, "Fiche supprimée", Toast.LENGTH_SHORT).show();
                        listeFiches.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listeFiches.size());
                    },
                    error -> Toast.makeText(context, "Erreur de suppression", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(fiche.id));
                    return params;
                }
            };

            Volley.newRequestQueue(context).add(request);
        });
    }

    @Override
    public int getItemCount() {
        return listeFiches.size();
    }

    public static class FicheViewHolder extends RecyclerView.ViewHolder {
        TextView date, montant, statut, supprimer;

        public FicheViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textMois); // renomme R.id.textMois en textDate dans XML si tu veux
            montant = itemView.findViewById(R.id.textMontant);
            statut = itemView.findViewById(R.id.textStatut);
            supprimer = itemView.findViewById(R.id.btnSupprimer);
        }
    }
}
