package db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import controllers.MedicoDAO;
import controllers.PacienteDAO;
import entities.Medico;
import entities.Paciente;

@Database(
        entities = {Medico.class, Paciente.class},
        version = 2,
        exportSchema = false
)
public abstract class FarmaciaBD extends RoomDatabase {

    private static FarmaciaBD INSTANCE;

    public abstract MedicoDAO medicoDAO();
    public abstract PacienteDAO pacienteDAO();

    public static FarmaciaBD getAppDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FarmaciaBD.class,
                            "BD_Farmacias"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
