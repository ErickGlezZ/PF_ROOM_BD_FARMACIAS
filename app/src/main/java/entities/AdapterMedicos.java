package entities;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pf_room_bd_farmacias.R;

import java.util.List;


public class AdapterMedicos extends RecyclerView.Adapter<AdapterMedicos.ViewHolder> {

    private List<Medico> lista;

    public AdapterMedicos(List<Medico> lista) {
        this.lista = lista;
    }

    public void actualizarLista(List<Medico> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medico, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {

        Medico m = lista.get(pos);

        h.txtSSN.setText("SSN: " + m.getSSN());
        h.txtNombreCompleto.setText(m.getNombre() + " " + m.getApePaterno() + " " + m.getApeMaterno());
        h.txtEspecialidad.setText("Especialidad: " + m.getEspecialidad());
        h.txtExperiencia.setText("Experiencia: " + m.getAñosExperiencia() + " años");

        h.iconMedico.setImageResource(R.drawable.profile);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSSN, txtNombreCompleto, txtEspecialidad, txtExperiencia;
        ImageView iconMedico;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSSN = itemView.findViewById(R.id.txtSSN);
            txtNombreCompleto = itemView.findViewById(R.id.txtNombreCompleto);
            txtEspecialidad = itemView.findViewById(R.id.txtEspecialidadItem);
            txtExperiencia = itemView.findViewById(R.id.txtExperiencia);

            iconMedico = itemView.findViewById(R.id.iconMedico);

            card = (CardView) itemView;
        }
    }

    public void eliminarPorSSN(String ssn) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getSSN().equals(ssn)) {
                lista.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

}

