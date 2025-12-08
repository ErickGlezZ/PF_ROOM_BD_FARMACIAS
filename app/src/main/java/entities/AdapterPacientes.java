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

public class AdapterPacientes extends RecyclerView.Adapter<AdapterPacientes.ViewHolder> {

    private List<Paciente> lista;

    public AdapterPacientes(List<Paciente> lista) {
        this.lista = lista;
    }

    public void actualizarLista(List<Paciente> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paciente, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {

        Paciente p = lista.get(pos);

        h.txtSSN.setText("SSN: " + p.getSSN());
        h.txtNombreCompleto.setText(
                p.getNombre() + " " + p.getApePaterno() + " " + p.getApeMaterno()
        );
        h.txtEdad.setText("Edad: " + p.getEdad() + " años");
        h.txtMedicoCabecera.setText("Médico Cabecera (SSN): " + p.getSsnMedicoCabecera());

        h.iconPaciente.setImageResource(R.drawable.profile);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSSN, txtNombreCompleto, txtEdad, txtMedicoCabecera;
        ImageView iconPaciente;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSSN = itemView.findViewById(R.id.txtSSN_Paciente);
            txtNombreCompleto = itemView.findViewById(R.id.txtNombreCompleto_Paciente);
            txtEdad = itemView.findViewById(R.id.txtEdad_Paciente);
            txtMedicoCabecera = itemView.findViewById(R.id.txtMedicoCabecera_Paciente);

            iconPaciente = itemView.findViewById(R.id.iconPaciente);

            card = (CardView) itemView;
        }
    }

    // ELIMINAR POR SSN PARA LA VISTA DE BAJAS
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
