package entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "pacientes",
        foreignKeys = @ForeignKey(
                entity = Medico.class,
                parentColumns = "SSN",
                childColumns = "SSN_Medico_Cabecera",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        indices = {@Index("SSN_Medico_Cabecera")}
)
public class Paciente {

    @PrimaryKey
    @NonNull
    public String SSN;

    @NonNull
    @ColumnInfo(name = "Nombre")
    public String nombre;

    @NonNull
    @ColumnInfo(name = "Ape_Paterno")
    public String apePaterno;

    @NonNull
    @ColumnInfo(name = "Ape_Materno")
    public String apeMaterno;

    @ColumnInfo(name = "Edad")
    public int edad;

    @NonNull
    @ColumnInfo(name = "SSN_Medico_Cabecera")
    public String ssnMedicoCabecera;


    public Paciente(@NonNull String SSN,
                    @NonNull String nombre,
                    @NonNull String apePaterno,
                    @NonNull String apeMaterno,
                    int edad,
                    @NonNull String ssnMedicoCabecera) {

        this.SSN = SSN;
        this.nombre = nombre;
        this.apePaterno = apePaterno;
        this.apeMaterno = apeMaterno;
        this.edad = edad;
        this.ssnMedicoCabecera = ssnMedicoCabecera;
    }


    // ---------- GETTERS ----------
    @NonNull
    public String getSSN() {
        return SSN;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    @NonNull
    public String getApePaterno() {
        return apePaterno;
    }

    @NonNull
    public String getApeMaterno() {
        return apeMaterno;
    }

    public int getEdad() {
        return edad;
    }

    @NonNull
    public String getSsnMedicoCabecera() {
        return ssnMedicoCabecera;
    }


    // ---------- SETTERS ----------
    public void setSSN(@NonNull String SSN) {
        this.SSN = SSN;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public void setApePaterno(@NonNull String apePaterno) {
        this.apePaterno = apePaterno;
    }

    public void setApeMaterno(@NonNull String apeMaterno) {
        this.apeMaterno = apeMaterno;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setSsnMedicoCabecera(@NonNull String ssnMedicoCabecera) {
        this.ssnMedicoCabecera = ssnMedicoCabecera;
    }


    @Override
    public String toString() {
        return "Paciente:" + '\n' +
                "SSN = '" + SSN + '\'' + '\n' +
                "Nombre = '" + nombre + " " + apePaterno + " " + apeMaterno + '\'' + '\n' +
                "Edad = " + edad + '\n' +
                "Médico de Cabecera = " + ssnMedicoCabecera;
    }
}
