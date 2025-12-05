package entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Medico {

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

    @NonNull
    @ColumnInfo(name = "Especialidad")
    public String especialidad;

    @ColumnInfo(name = "Años_Experiencia")
    public int aniosExperiencia;


    // ---------- CONSTRUCTOR ----------
    public Medico(@NonNull String SSN,
                  @NonNull String nombre,
                  @NonNull String apePaterno,
                  @NonNull String apeMaterno,
                  @NonNull String especialidad,
                  int aniosExperiencia) {

        this.SSN = SSN;
        this.nombre = nombre;
        this.apePaterno = apePaterno;
        this.apeMaterno = apeMaterno;
        this.especialidad = especialidad;
        this.aniosExperiencia = aniosExperiencia;
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

    @NonNull
    public String getEspecialidad() {
        return especialidad;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
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

    public void setEspecialidad(@NonNull String especialidad) {
        this.especialidad = especialidad;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }



    @Override
    public String toString() {
        return "Medico:" + '\n' +
                "SSN = '" + SSN + '\'' + '\n' +
                "Nombre = '" + nombre + " " + apePaterno + " " + apeMaterno + '\'' + '\n' +
                "Especialidad = '" + especialidad + '\'' + '\n' +
                "Años de experiencia = " + aniosExperiencia;
    }
}

