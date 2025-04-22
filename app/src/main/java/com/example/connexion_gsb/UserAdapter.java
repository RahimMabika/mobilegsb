package com.example.connexion_gsb;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;
    private final Context context;

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.email.setText("Email : " + user.email);
        holder.role.setText("Rôle : " + user.role);

        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Modifier utilisateur");

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(40, 20, 40, 10);

            final EditText inputEmail = new EditText(context);
            inputEmail.setHint("Nouvel email");
            inputEmail.setText(user.email);
            layout.addView(inputEmail);

            final EditText inputPassword = new EditText(context);
            inputPassword.setHint("Nouveau mot de passe");
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(inputPassword);

            builder.setView(layout);

            builder.setPositiveButton("Valider", (dialog, which) -> {
                String newEmail = inputEmail.getText().toString().trim();
                String newPass = inputPassword.getText().toString().trim();
                modifierUtilisateur(user.id, newEmail, newPass);
            });

            builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }

    private void modifierUtilisateur(int userId, String email, String password) {
        String url = "http://10.0.2.2/applimobile/modifierUtilisateur.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(context, "Utilisateur modifié", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Erreur modification", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(userId));
                params.put("email", email);
                if (!password.isEmpty()) {
                    params.put("password", password);
                }
                return params;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView email, role;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.textEmailUser);
            role = itemView.findViewById(R.id.textRoleUser);
        }
    }
}
