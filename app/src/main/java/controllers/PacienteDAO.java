package controllers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Paciente;

@Dao
public interface PacienteDAO {

    //-------------- ALTAS ----------------
    @Insert
    void agregarPaciente(Paciente paciente);

    //-------------- BAJAS ----------------
    @Delete
    void eliminarPaciente(Paciente paciente);

    @Query("DELETE FROM pacientes WHERE SSN = :ssn")
    void eliminarPacientePorSSN(String ssn);

    //-------------- BUSCAR ----------------
    @Query("SELECT * FROM pacientes WHERE SSN = :ssn")
    Paciente buscarPacientePorSSN(String ssn);

    //-------------- CAMBIOS ----------------
    @Update
    void actualizarPaciente(Paciente paciente);

    @Query("UPDATE pacientes SET Nombre = :nombre WHERE SSN = :ssn")
    void actualizarNombrePorSSN(String nombre, String ssn);

    //-------------- CONSULTAS ----------------
    @Query("SELECT * FROM pacientes")
    List<Paciente> obtenerPacientes();

    @Query("SELECT * FROM pacientes WHERE SSN LIKE :ssn")
    List<Paciente> buscarPorSSN(String ssn);

    //-------------- CONSULTAS ESPECIALES --------------
    @Query("SELECT * FROM pacientes WHERE SSN_Medico_Cabecera = :ssnMedico")
    List<Paciente> obtenerPacientesDeMedico(String ssnMedico);
}
