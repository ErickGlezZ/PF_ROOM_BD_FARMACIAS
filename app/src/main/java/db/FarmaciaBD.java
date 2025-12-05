package db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import controllers.MedicoDAO;
import entities.Medico;

@Database(entities = {Medico.class}, version = 1)
public abstract class FarmaciaBD extends RoomDatabase {

    private static FarmaciaBD INSTANCE;

    public abstract MedicoDAO medicoDAO();

    public static FarmaciaBD getAppDatabase(Context context){
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    FarmaciaBD.class,"BD_Farmacias").build();

            return INSTANCE;

    }

    public static void destroyInstance(){INSTANCE=null;}
}
