package com.upao.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.upao.R;
import com.upao.entity.service.DocumentoAlmacenado;
import com.upao.entity.service.Paciente;
import com.upao.entity.service.Usuario;
import com.upao.viewmodel.DocumentoAlmacenadoViewModel;
import com.upao.viewmodel.PacienteViewModel;
import com.upao.viewmodel.UsuarioViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private File f;
    private PacienteViewModel pacienteViewModel;
    private UsuarioViewModel usuarioViewModel;
    private DocumentoAlmacenadoViewModel documentoAlmacenadoViewModel;
    private Button btnSubirImagen, btnGuardarDatos;
    private CircleImageView imageUser;
    private AutoCompleteTextView dropdownTipoDoc, dropdownDepartamento, dropdownProvincia, dropdownDistrito, dropdownGeneros;
    private EditText edtfechanacimiento;
    private EditText edtNameUser, edtApellidoPaternoU, edtApellidoMaternoU, edtNumDocU, edtTelefonoU,
            edtDireccionU, edtAlergias, edtinformacionadicional, edtPasswordUser, edtEmailUser;
    private TextInputLayout txtInputNameUser, txtInputApellidoPaternoU, txtInputApellidoMaternoU,
            txtInputTipoDoc, txtInputNumeroDocU, txtInputDepartamento, txtInputProvincia,
            txtInputDistrito, txtInputTelefonoU, txtInputDireccionU, txtInputAlergias, txtInputinformacionadicional,
            txtInputGeneros ,txtInputEmailUser, txtInputPasswordUser ,txtInputfechanacimiento;
    ArrayAdapter<String> provinciasAdapter;
    ArrayAdapter<String> distritosAdapter;
    HashMap<String, HashMap<String, String[]>> data = new HashMap<>();
    private final static int LOCATION_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        this.init();
        this.initViewModel();
        this.spinners();
    }

    private void spinners() {
        //LISTA DE TIPOS DE DOCUMENTOS
        String[] tipoDoc = getResources().getStringArray(R.array.tipoDoc);
        ArrayAdapter arrayTipoDoc = new ArrayAdapter(this, R.layout.dropdown_item, tipoDoc);
        dropdownTipoDoc.setAdapter(arrayTipoDoc);

        //LISTA DE GENEROS
        String[] generos = getResources().getStringArray(R.array.generos);
        ArrayAdapter arrayGeneros = new ArrayAdapter(this, R.layout.dropdown_item, generos);
        dropdownGeneros.setAdapter(arrayGeneros);

        //ARRAY DISTRITOS - DEPARTAMENTOS - PROVINCIAS
        provinciasAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item);
        distritosAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item);
        loadDepartamentos();
        dropdownDepartamento.setAdapter(new ArrayAdapter<>(this, R.layout.dropdown_item, data.keySet().toArray(new String[0])));
        dropdownProvincia.setAdapter(provinciasAdapter);
        dropdownDistrito.setAdapter(distritosAdapter);
        dropdownDepartamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDepartamento = dropdownDepartamento.getText().toString();
                updateProvincias(selectedDepartamento);
            }
        });

        dropdownProvincia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvincia = dropdownProvincia.getText().toString();
                updateDistritos(selectedProvincia);
            }
        });
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        this.pacienteViewModel = vmp.get(PacienteViewModel.class);
        this.usuarioViewModel = vmp.get(UsuarioViewModel.class);
        this.documentoAlmacenadoViewModel = vmp.get(DocumentoAlmacenadoViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Gracias por conceder los permisos para " +
                            "leer el almacenamiento, estos permisos son necesarios para poder " +
                            "escoger tu foto de perfil", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No podemos realizar el registro si no nos concedes los permisos para leer el almacenamiento.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void init() {
        edtfechanacimiento = findViewById(R.id.edtfechaNacimiento);
        btnGuardarDatos = findViewById(R.id.btnGuardarDatos);
        btnSubirImagen = findViewById(R.id.btnSubirImagen);
        imageUser = findViewById(R.id.imageUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtApellidoPaternoU = findViewById(R.id.edtApellidoPaternoU);
        edtApellidoMaternoU = findViewById(R.id.edtApellidoMaternoU);
        edtNumDocU = findViewById(R.id.edtNumDocU);
        edtTelefonoU = findViewById(R.id.edtTelefonoU);
        edtDireccionU = findViewById(R.id.edtDireccionU);
        edtAlergias = findViewById(R.id.edtAlergias);
        edtinformacionadicional = findViewById(R.id.edtinformacionadicional);
        edtPasswordUser = findViewById(R.id.edtPasswordUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        edtfechanacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //AutoCompleteTextView
        dropdownTipoDoc = findViewById(R.id.dropdownTipoDoc);
        dropdownDepartamento = findViewById(R.id.dropdownDepartamento);
        dropdownProvincia = findViewById(R.id.dropdownProvincia);
        dropdownDistrito = findViewById(R.id.dropdownDistrito);
        dropdownGeneros = findViewById(R.id.dropdownGeneros);


        //TextInputLayout
        txtInputfechanacimiento = findViewById(R.id.txtInputfechanacimiento);
        txtInputNameUser = findViewById(R.id.txtInputNameUser);
        txtInputApellidoPaternoU = findViewById(R.id.txtInputApellidoPaternoU);
        txtInputApellidoMaternoU = findViewById(R.id.txtInputApellidoMaternoU);
        txtInputTipoDoc = findViewById(R.id.txtInputTipoDoc);
        txtInputNumeroDocU = findViewById(R.id.txtInputNumeroDocU);
        txtInputDepartamento = findViewById(R.id.txtInputDepartamento);
        txtInputProvincia = findViewById(R.id.txtInputProvincia);
        txtInputGeneros = findViewById(R.id.txtInputGeneros);
        txtInputDistrito = findViewById(R.id.txtInputDistrito);
        txtInputTelefonoU = findViewById(R.id.txtInputTelefonoU);
        txtInputDireccionU = findViewById(R.id.txtInputDireccionU);
        txtInputAlergias = findViewById(R.id.txtInputAlergias);
        txtInputinformacionadicional = findViewById(R.id.txtInputinformacionadicional);
        txtInputEmailUser = findViewById(R.id.txtInputEmailUser);
        txtInputPasswordUser = findViewById(R.id.txtInputPasswordUser);

        btnSubirImagen.setOnClickListener(v -> {
            this.cargarImagen();
        });
        btnGuardarDatos.setOnClickListener(v -> {
            this.guardarDatos();
        });
        edtNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNameUser.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtApellidoPaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoPaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtApellidoMaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoMaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtNumDocU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNumeroDocU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtTelefonoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTelefonoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDireccionU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDireccionU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtAlergias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputAlergias.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtinformacionadicional.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputinformacionadicional.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownTipoDoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTipoDoc.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownDepartamento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDepartamento.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownProvincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputProvincia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownDistrito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDistrito.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownGeneros.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputGeneros.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void guardarDatos() {
        Paciente p;
        if (validar()) {
            p = new Paciente();
            try {
                p.setNombres(edtNameUser.getText().toString());
                p.setApellidoPaterno(edtApellidoPaternoU.getText().toString());
                p.setApellidoMaterno(edtApellidoMaternoU.getText().toString());
                p.setTipoDoc(dropdownTipoDoc.getText().toString());
                p.setNumDoc(edtNumDocU.getText().toString());
                p.setDepartamento(dropdownDepartamento.getText().toString());
                p.setProvincia(dropdownProvincia.getText().toString());
                p.setDistrito(dropdownDistrito.getText().toString());
                p.setDireccion(edtDireccionU.getText().toString());
                p.setGenero(dropdownGeneros.getText().toString());
                p.setTelefono(edtTelefonoU.getText().toString());
                p.setAlergias(edtAlergias.getText().toString());
                p.setInformacionadicional(edtinformacionadicional.getText().toString());
                p.setFechanacimiento(edtfechanacimiento.getText().toString());
                p.setId(0);
                LocalDateTime ldt = LocalDateTime.now();
                RequestBody rb = RequestBody.create(f, MediaType.parse("multipart/form-data")), somedata;
                String filename = "" + ldt.getDayOfMonth() + (ldt.getMonthValue() + 1) +
                        ldt.getYear() + ldt.getHour()
                        + ldt.getMinute() + ldt.getSecond();
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", f.getName(), rb);
                somedata = RequestBody.create("profilePh" + filename, MediaType.parse("text/plain"));
                this.documentoAlmacenadoViewModel.save(part, somedata).observe(this, response -> {
                    if (response.getRpta() == 1) {
                        p.setFoto(new DocumentoAlmacenado());
                        p.getFoto().setId(response.getBody().getId());
                        this.pacienteViewModel.guardarPaciente(p).observe(this, cResponse -> {
                            if (cResponse.getRpta() == 1) {
                                Toast.makeText(this, response.getMessage() + ", ahora procederemos a registrar sus credenciales.", Toast.LENGTH_SHORT).show();
                                int idc = cResponse.getBody().getId();
                                Usuario u = new Usuario();
                                u.setEmail(edtEmailUser.getText().toString());
                                u.setClave(edtPasswordUser.getText().toString());
                                u.setVigencia(true);
                                u.setPaciente(new Paciente(idc));
                                this.usuarioViewModel.save(u).observe(this, uResponse -> {
                                    if (uResponse.getRpta() == 1) {
                                            toastRegistrado("Bienvenido nuevo usuario");
                                        this.finish();
                                    } else {
                                        toastIncorrecto("No se ha podido guardar los datos, intentelo de nuevo");
                                    }
                                });
                            }  else {
                                toastIncorrecto("Uy lo sentimos ya existe un usuario con ese DNI");
                            }
                        });
                    } else {
                        toastIncorrecto("Algo esta haciendo mal revise bien sus datos");
                    }
                });
            } catch (Exception e) {
                warningMessage("Se ha producido un error : " + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos del formulario");
        }
    }

    private void cargarImagen() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(Intent.createChooser(i, "Seleccione la Aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            final String realPath = getRealPathFromURI(uri);
            this.f = new File(realPath);
            this.imageUser.setImageURI(uri);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String result;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private boolean validar() {
        boolean retorno = true;
        String nombres, apellidoPaterno, apellidoMaterno, numDoc, telefono, direccion, correo, clave, alergias, informacionadicional,
                dropTipoDoc, dropDepartamento, dropProvincia, dropDistrito, dropGeneros, fechanacimiento;
        nombres = edtNameUser.getText().toString();
        apellidoPaterno = edtApellidoPaternoU.getText().toString();
        apellidoMaterno = edtApellidoMaternoU.getText().toString();
        numDoc = edtNumDocU.getText().toString();
        telefono = edtTelefonoU.getText().toString();
        direccion = edtDireccionU.getText().toString();
        alergias = edtAlergias.getText().toString();
        fechanacimiento = edtfechanacimiento.getText().toString();
        informacionadicional = edtinformacionadicional.getText().toString();
        correo = edtEmailUser.getText().toString();
        clave = edtPasswordUser.getText().toString();
        dropTipoDoc = dropdownTipoDoc.getText().toString();
        dropDepartamento = dropdownDepartamento.getText().toString();
        dropProvincia = dropdownProvincia.getText().toString();
        dropDistrito = dropdownDistrito.getText().toString();
        dropGeneros = dropdownGeneros.getText().toString();
        if (this.f == null) {
            errorMessage("debe selecionar una foto de perfil");
            retorno = false;
        }
        if (nombres.isEmpty()) {
            txtInputNameUser.setError("Ingresar nombres");
            retorno = false;
        } else {
            txtInputNameUser.setErrorEnabled(false);
        }
        if (apellidoPaterno.isEmpty()) {
            txtInputApellidoPaternoU.setError("Ingresar apellido paterno");
            retorno = false;
        } else {
            txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        if (apellidoMaterno.isEmpty()) {
            txtInputApellidoMaternoU.setError("Ingresar apellido materno");
            retorno = false;
        } else {
            txtInputApellidoMaternoU.setErrorEnabled(false);
        }
        if (fechanacimiento.isEmpty()) {
            txtInputfechanacimiento.setError("Ingresar fecha de nacimiento");
            retorno = false;
        } else {
            txtInputfechanacimiento.setErrorEnabled(false);
        }
        if (numDoc.isEmpty()) {
            txtInputNumeroDocU.setError("Ingresar número documento");
            retorno = false;
        } else {
            txtInputNumeroDocU.setErrorEnabled(false);
        }
        if (telefono.isEmpty()) {
            txtInputTelefonoU.setError("Ingresar número telefónico");
            retorno = false;
        } else {
            txtInputTelefonoU.setErrorEnabled(false);
        }
        if (direccion.isEmpty()) {
            txtInputDireccionU.setError("Ingresar dirección de su casa");
            retorno = false;
        } else {
            txtInputDireccionU.setErrorEnabled(false);
        }
        if (alergias.isEmpty()) {
            txtInputAlergias.setError("Ingresar alergias en caso de tenerlas");
            retorno = false;
        } else {
            txtInputAlergias.setErrorEnabled(false);
        }
        if (informacionadicional.isEmpty()) {
            txtInputinformacionadicional.setError("Ingresar información adicional importante del paciente");
            retorno = false;
        } else {
            txtInputinformacionadicional.setErrorEnabled(false);
        }
        if (correo.isEmpty()) {
            txtInputEmailUser.setError("Ingresar correo electrónico");
            retorno = false;
        } else {
            txtInputEmailUser.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            txtInputPasswordUser.setError("Ingresar clave para el inicio de sesión");
            retorno = false;
        } else {
            txtInputPasswordUser.setErrorEnabled(false);
        }
        if (dropTipoDoc.isEmpty()) {
            txtInputTipoDoc.setError("Seleccionar Tipo Doc");
            retorno = false;
        } else {
            txtInputTipoDoc.setErrorEnabled(false);
        }
        if (dropDepartamento.isEmpty()) {
            txtInputDepartamento.setError("Seleccionar Departamento");
            retorno = false;
        } else {
            txtInputDepartamento.setErrorEnabled(false);
        }
        if (dropProvincia.isEmpty()) {
            txtInputProvincia.setError("Seleccionar Provincia");
            retorno = false;
        } else {
            txtInputProvincia.setErrorEnabled(false);
        }
        if (dropDistrito.isEmpty()) {
            txtInputDistrito.setError("Seleccionar Distrito");
            retorno = false;
        } else {
            txtInputDistrito.setErrorEnabled(false);
        }
        if (dropGeneros.isEmpty()) {
            txtInputGeneros.setError("Seleccionar Genero");
            retorno = false;
        } else {
            txtInputGeneros.setErrorEnabled(false);
        }
        return retorno;
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }
    public void toastRegistrado(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_registrado, (ViewGroup) findViewById(R.id.ll_custom_toast_registrado));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeRegistrado);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema")
                .setContentText(message).setConfirmText("Ok").show();
    }
    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    private void loadDepartamentos() {
        // Carga tus datos de departamentos, provincias y distritos aquí
        // Ejemplo:
        HashMap<String, String[]> amazonasData = new HashMap<>();
        amazonasData.put("Condorcanqui", getResources().getStringArray(R.array.Condorcanqui));
        amazonasData.put("Bagua", getResources().getStringArray(R.array.Bagua));
        amazonasData.put("Bongara", getResources().getStringArray(R.array.Bongara));
        // Añade más provincias y departamentos según tus necesidades

        data.put("Amazonas", amazonasData);
        // Agrega más departamentos según tus necesidades
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + month + "/" + year;
                        edtfechanacimiento.setText(selectedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    // Actualiza el adaptador de provincias basado en el departamento seleccionado
    private void updateProvincias(String departamento) {
        HashMap<String, String[]> provinciasData = data.get(departamento);
        if (provinciasData != null) {
            provinciasAdapter.clear();
            provinciasAdapter.addAll(provinciasData.keySet());
        } else {
            provinciasAdapter.clear();
        }
        dropdownProvincia.setText("");
        distritosAdapter.clear();
        dropdownDistrito.setText("");
    }

    private void updateDistritos(String provincia) {
        String selectedDepartamento = dropdownDepartamento.getText().toString();
        HashMap<String, String[]> provinciasData = data.get(selectedDepartamento);
        String[] distritosData = provinciasData.get(provincia);
        if (distritosData != null) {
            distritosAdapter.clear();
            distritosAdapter.addAll(distritosData);
        } else {
            distritosAdapter.clear();
        }
        dropdownDistrito.setText("");
    }


}
