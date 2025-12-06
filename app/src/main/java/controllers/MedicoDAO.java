package controllers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Medico;

@Dao
public interface MedicoDAO {

    //-------------- ALTAS ----------------
    @Insert
    void agregarMedico(Medico medico);

    //-------------- BAJAS ----------------
    @Delete
    void eliminarMedico(Medico medico);

    @Query("DELETE FROM medico WHERE SSN = :ssn")
    void eliminarMedicoPorSSN(String ssn);

    //-------------- BUSCAR ----------------
    @Query("SELECT * FROM medico WHERE SSN = :ssn")
    Medico buscarMedicoPorSSN(String ssn);

    //-------------- CAMBIOS ----------------
    @Update
    void actualizarMedico(Medico medico);

    @Query("UPDATE medico SET Nombre = :nombre WHERE SSN = :ssn")
    void actualizarNombrePorSSN(String nombre, String ssn);

    //-------------- CONSULTAS ----------------
    @Query("SELECT * FROM medico")
    List<Medico> obtenerMedicos();

    @Query("SELECT * FROM medico WHERE SSN  LIKE  :ssn")
    List<Medico> buscarPorSSN(String ssn);
}

