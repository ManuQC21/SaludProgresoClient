package com.upao.entity.service;


public class Paciente {

    private int id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechanacimiento;
    private String tipoDoc;
    private String numDoc;
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccion;
    private String genero;
    private String telefono;
    private String alergias;
    private String informacionadicional;
    private Foto foto;

    public Paciente() {
    }

    public Paciente(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int ID_Paciente) {
        this.id = ID_Paciente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
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

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public String getNombreCompletoPaciente(){
        return this.nombres != null && this.apellidoPaterno != null && this.apellidoMaterno != null ?
                this.nombres + " " + this.apellidoPaterno + " " + this.apellidoMaterno : "-----";
    }


}
