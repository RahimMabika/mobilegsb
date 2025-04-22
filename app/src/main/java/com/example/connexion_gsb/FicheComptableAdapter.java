package com.example.connexion_gsb;

import android.content.Context;
import android.content.Intent;
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

public class FicheComptableAdapter extends RecyclerView.Adapter<FicheComptableAdapter.FicheViewHolder> {

    private final List<FicheFrais> fiches;
    private final Context context;

    public FicheComptableAdapter(List<FicheFrais> fiches, Context context) {
        this.fiches = fiches;
        this.context = context;
    }

    @NonNull
    @Override
    public FicheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fiche_comptable, parent, false);
        return new FicheViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FicheViewHolder holder, int position) {
        FicheFrais fiche = fiches.get(position);
        holder.email.setText("Utilisateur : " + fiche.email);
        holder.date.setText("Date : " + fiche.date);
        holder.montant.setText("Montant : " + fiche.montant + " €");
        holder.statut.setText("Statut : " + fiche.statut);

        holder.voir.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFicheActivity.class);
            intent.putExtra("fiche_id", fiche.id);
            context.startActivity(intent);
        });

        holder.valider.setOnClickListener(v -> updateStatutFiche(fiche.id, "accepté", position));
        holder.refuser.setOnClickListener(v -> updateStatutFiche(fiche.id, "refusé", position));
    }

    private void updateStatutFiche(int ficheId, String nouveauStatut, int position) {
        String url = "http://10.0.2.2/applimobile/updateStatut.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Fiche " + nouveauStatut, Toast.LENGTH_SHORT).show();
                    fiches.get(position).statut = nouveauStatut;
                    notifyItemChanged(position);
                },
                error -> Toast.makeText(context, "Erreur mise à jour", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(ficheId));
                params.put("statut", nouveauStatut);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public int getItemCount() {
        return fiches.size();
    }

    public static class FicheViewHolder extends RecyclerView.ViewHolder {
        TextView email, date, montant, statut;
        Button voir, valider, refuser;

        public FicheViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.textEmail);
            date = itemView.findViewById(R.id.textMois); // ou textDate
            montant = itemView.findViewById(R.id.textMontant);
            statut = itemView.findViewById(R.id.textStatut);
            voir = itemView.findViewById(R.id.buttonVoir);
            valider = itemView.findViewById(R.id.buttonValider);
            refuser = itemView.findViewById(R.id.buttonRefuser);
        }
    }
}
