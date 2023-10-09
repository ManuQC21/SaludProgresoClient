package com.upao.activity.entity.service;

import java.util.Date;
import java.util.List;


public class Paciente {

    private int ID_Paciente;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Date fechanacimiento;
    private String tipoDoc;
    private String numDoc;
    private String genero;
    private String distrito;
    private String direccion;
    private String telefono;
    private String alergias;
    private String informacionadicional;
    private List<CitasMedicas> citasMedicas;
    private List<Medicamentos> medicamentos;
    private List<RegistroSalud> registrosSalud;
    private List<ControlMedico> controlMedico;
    private Medico medicoAsignado;
    private DocumentoAlmacenado foto;

    public int getID_Paciente() {
        return ID_Paciente;
    }

    public void setID_Paciente(int ID_Paciente) {
        this.ID_Paciente = ID_Paciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getInformacionadicional() {
        return informacionadicional;
    }

    public void setInformacionadicional(String informacionadicional) {
        this.informacionadicional = informacionadicional;
    }

    public DocumentoAlmacenado getFoto() {
        return foto;
    }

    public void setFoto(DocumentoAlmacenado foto) {
        this.foto = foto;
    }

    public List<CitasMedicas> getCitasMedicas() {
        return citasMedicas;
    }

    public void setCitasMedicas(List<CitasMedicas> citasMedicas) {
        this.citasMedicas = citasMedicas;
    }

    public List<Medicamentos> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public List<RegistroSalud> getRegistrosSalud() {
        return registrosSalud;
    }

    public void setRegistrosSalud(List<RegistroSalud> registrosSalud) {
        this.registrosSalud = registrosSalud;
    }

    public List<ControlMedico> getControlMedico() {
        return controlMedico;
    }

    public void setControlMedico(List<ControlMedico> controlMedico) {
        this.controlMedico = controlMedico;
    }

    public Medico getMedicoAsignado() {
        return medicoAsignado;
    }

    public void setMedicoAsignado(Medico medicoAsignado) {
        this.medicoAsignado = medicoAsignado;
    }

}
