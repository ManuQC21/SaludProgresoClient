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
import com.google.android.material.textfield.TextInputLayout;
import com.upao.R;
import com.upao.entity.service.Foto;
import com.upao.entity.service.Paciente;
import com.upao.entity.service.Usuario;
import com.upao.viewmodel.FotoViewModel;
import com.upao.viewmodel.PacienteViewModel;
import com.upao.viewmodel.UsuarioViewModel;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private File f;
    String pattern = "^[a-zA-Z0-9.,!?\\s]*$";

    private PacienteViewModel pacienteViewModel;
    private UsuarioViewModel usuarioViewModel;
    private FotoViewModel documentoAlmacenadoViewModel;
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
        this.documentoAlmacenadoViewModel = vmp.get(FotoViewModel.class);
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
                        p.setFoto(new Foto());
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
        //NOMBRES
        if (nombres.isEmpty()) {
            txtInputNameUser.setError("Ingresar nombres");
            retorno = false;
        } else if (!nombres.matches(pattern)) {
            txtInputNameUser.setError("Esta usando caracteres no permitidos");
            retorno = false;
        } else {
            txtInputNameUser.setErrorEnabled(false);
        }
        //APELLIDO P
        if (apellidoPaterno.isEmpty()) {
            txtInputApellidoPaternoU.setError("Ingresar apellido paterno");
            retorno = false;
        } else if (!apellidoPaterno.matches(pattern)) {
            txtInputApellidoPaternoU.setError("Esta usando caracteres no permitidos");
            retorno = false;
        } else {
            txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        //APELLIDO M
        if (apellidoMaterno.isEmpty()) {
            txtInputApellidoMaternoU.setError("Ingresar apellido materno");
            retorno = false;
        } else if (!apellidoMaterno.matches(pattern)) {
            txtInputApellidoMaternoU.setError("Esta usando caracteres no permitidos");
            retorno = false;
        } else {
            txtInputApellidoMaternoU.setErrorEnabled(false);
        }
        //NACIMIENTO
        if (fechanacimiento.isEmpty()) {
            txtInputfechanacimiento.setError("Ingresar fecha de nacimiento");
            retorno = false;
        } else {
            txtInputfechanacimiento.setErrorEnabled(false);
        }
        //NUMERO DOC
        if (numDoc.isEmpty()) {
            txtInputNumeroDocU.setError("Ingresar número documento");
            retorno = false;
        } else {
            txtInputNumeroDocU.setErrorEnabled(false);
        }
        //NUMERO TELEFONO
        if (telefono.isEmpty()) {
            txtInputTelefonoU.setError("Ingresar número telefónico");
            retorno = false;
        } else {
            txtInputTelefonoU.setErrorEnabled(false);
        }
        //DIRECCION
        if (direccion.isEmpty()) {
            txtInputDireccionU.setError("Ingresar dirección de su casa");
            retorno = false;
        }else if (!alergias.matches(pattern)) {
            txtInputDireccionU.setError("Esta usando caracteres no permitidos");
            retorno = false;
        } else {
            txtInputDireccionU.setErrorEnabled(false);
        }
        //ALERGIAS
        if (alergias.isEmpty()) {
            txtInputAlergias.setError("Ingresar alergias en caso de tenerlas");
            retorno = false;
        } else if (!alergias.matches(pattern)) {
            txtInputAlergias.setError("Esta usando caracteres no permitidos");
            retorno = false;
        } else {
            txtInputAlergias.setErrorEnabled(false);
        }
        //INFO
        if (informacionadicional.isEmpty()) {
            txtInputinformacionadicional.setError("Ingresar información adicional importante del paciente");
            retorno = false;
        } else if (!informacionadicional.matches(pattern)) {
            txtInputinformacionadicional.setError("Esta usando caracteres no permitidos");
            retorno = false;
        } else {
            txtInputinformacionadicional.setErrorEnabled(false);
        }
        //CORREO

        if (correo.isEmpty()) {
            txtInputEmailUser.setError("Ingresar correo electrónico");
            retorno = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtInputEmailUser.setError("Correo electrónico no válido");
            retorno = false;
        } else {
            txtInputEmailUser.setErrorEnabled(false);
        }
        //CLAVE
        if (clave.isEmpty()) {
            txtInputPasswordUser.setError("Ingresar clave para el inicio de sesión");
            retorno = false;
        } else {
            txtInputPasswordUser.setErrorEnabled(false);
        }
        //TIPO DOC
        if (dropTipoDoc.isEmpty()) {
            txtInputTipoDoc.setError("Seleccionar Tipo Doc");
            retorno = false;
        } else {
            txtInputTipoDoc.setErrorEnabled(false);
        }
        //DEP
        if (dropDepartamento.isEmpty()) {
            txtInputDepartamento.setError("Seleccionar Departamento");
            retorno = false;
        } else {
            txtInputDepartamento.setErrorEnabled(false);
        }
        //PROVINCIA
        if (dropProvincia.isEmpty()) {
            txtInputProvincia.setError("Seleccionar Provincia");
            retorno = false;
        } else {
            txtInputProvincia.setErrorEnabled(false);
        }
        //DISTRITO
        if (dropDistrito.isEmpty()) {
            txtInputDistrito.setError("Seleccionar Distrito");
            retorno = false;
        } else {
            txtInputDistrito.setErrorEnabled(false);
        }
        //GENERO
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
        // AMAZONAS:
        HashMap<String, String[]> amazonasData = new HashMap<>();
        amazonasData.put("Condorcanqui", getResources().getStringArray(R.array.Condorcanqui));
        amazonasData.put("Bagua", getResources().getStringArray(R.array.Bagua));
        amazonasData.put("Bongara", getResources().getStringArray(R.array.Bongara));
        amazonasData.put("Utcubamba", getResources().getStringArray(R.array.Utcubamba));
        amazonasData.put("Luya", getResources().getStringArray(R.array.Luya));
        amazonasData.put("Rodriguez de Mendoza", getResources().getStringArray(R.array.RodriguezDeMendoza));
        amazonasData.put("Chachapoyas", getResources().getStringArray(R.array.Chachapoyas));

        // Ancash:
        HashMap<String, String[]> ancashData = new HashMap<>();
        amazonasData.put("Aija", getResources().getStringArray(R.array.Aija));
        amazonasData.put("Antonio Raymondi", getResources().getStringArray(R.array.AntonioRaymondi));
        amazonasData.put("Asunción", getResources().getStringArray(R.array.Asuncion));
        amazonasData.put("Bolognesi", getResources().getStringArray(R.array.Bolognesi));
        amazonasData.put("Carhuaz", getResources().getStringArray(R.array.Carhuaz));
        amazonasData.put("Casma", getResources().getStringArray(R.array.Casma));
        amazonasData.put("Corongo", getResources().getStringArray(R.array.Corongo));
        amazonasData.put("Huaraz", getResources().getStringArray(R.array.Huaraz));
        amazonasData.put("Huari", getResources().getStringArray(R.array.Huari));
        amazonasData.put("Huarmey", getResources().getStringArray(R.array.Huarmey));
        amazonasData.put("Mariscal Luzuriaga", getResources().getStringArray(R.array.MariscalLuzuriaga));
        amazonasData.put("Ocros", getResources().getStringArray(R.array.Ocros));
        amazonasData.put("Pallasca", getResources().getStringArray(R.array.Pallasca));
        amazonasData.put("Pomabamba", getResources().getStringArray(R.array.Pomabamba));
        amazonasData.put("Recuay", getResources().getStringArray(R.array.Recuay));
        amazonasData.put("Santa", getResources().getStringArray(R.array.Santa));
        amazonasData.put("Sihuas", getResources().getStringArray(R.array.Sihuas));
        amazonasData.put("Yungay", getResources().getStringArray(R.array.Yungay));
        // APURIMAC:
        HashMap<String, String[]> apurimacData = new HashMap<>();
        amazonasData.put("Abancay", getResources().getStringArray(R.array.Abancay));
        amazonasData.put("Andahuaylas", getResources().getStringArray(R.array.Andahuaylas));
        amazonasData.put("Antabamba", getResources().getStringArray(R.array.Antabamba));
        amazonasData.put("Aymaraes", getResources().getStringArray(R.array.Aymaraes));
        amazonasData.put("Chincheros", getResources().getStringArray(R.array.Chincheros));
        amazonasData.put("Cotabambas", getResources().getStringArray(R.array.Cotabambas));
        amazonasData.put("Grau", getResources().getStringArray(R.array.Grau));
        amazonasData.put("Huancarama", getResources().getStringArray(R.array.Huancarama));
        amazonasData.put("Tambobamba", getResources().getStringArray(R.array.Tambobamba));
        amazonasData.put("Coyllurqui", getResources().getStringArray(R.array.Coyllurqui));
        amazonasData.put("Haquira", getResources().getStringArray(R.array.Haquira));
        amazonasData.put("Lucanas", getResources().getStringArray(R.array.Lucanas));
        amazonasData.put("Saul Cantoral", getResources().getStringArray(R.array.SaulCantoral));
        amazonasData.put("San Pedro de Cachora", getResources().getStringArray(R.array.SanPedroDeCachora));
        //Arequipa:
        HashMap<String, String[]> arequipaData = new HashMap<>();
        arequipaData.put("Arequipa", getResources().getStringArray(R.array.Arequipa));
        arequipaData.put("Camaná", getResources().getStringArray(R.array.Camana));
        arequipaData.put("Caravelí", getResources().getStringArray(R.array.Caraveli));
        arequipaData.put("Castilla", getResources().getStringArray(R.array.Castilla));
        arequipaData.put("Caylloma", getResources().getStringArray(R.array.Caylloma));
        arequipaData.put("Condesuyos", getResources().getStringArray(R.array.Condesuyos));
        arequipaData.put("Islay", getResources().getStringArray(R.array.Islay));
        arequipaData.put("La Unión", getResources().getStringArray(R.array.LaUnion));
        arequipaData.put("Castilla", getResources().getStringArray(R.array.Castilla));
        arequipaData.put("Huarinavi", getResources().getStringArray(R.array.Huarinavi));
        arequipaData.put("Piscopi", getResources().getStringArray(R.array.Piscopi));
        //Ayacucho:
        HashMap<String, String[]> ayacuchoData = new HashMap<>();
        ayacuchoData.put("Huamanga", getResources().getStringArray(R.array.Huamanga));
        ayacuchoData.put("Cangallo", getResources().getStringArray(R.array.Cangallo));
        ayacuchoData.put("Huanta", getResources().getStringArray(R.array.Huanta));
        ayacuchoData.put("La Mar", getResources().getStringArray(R.array.LaMar));
        ayacuchoData.put("Lucanas", getResources().getStringArray(R.array.Lucanas));
        ayacuchoData.put("Parinacochas", getResources().getStringArray(R.array.Parinacochas));
        ayacuchoData.put("Paucar del Sara Sara", getResources().getStringArray(R.array.PaucarDelSaraSara));
        ayacuchoData.put("Sucre", getResources().getStringArray(R.array.Sucre));
        ayacuchoData.put("Victor Fajardo", getResources().getStringArray(R.array.VictorFajardo));
        ayacuchoData.put("Vilcas Huamán", getResources().getStringArray(R.array.VilcasHuaman));
        //CAJAMARCA:
        HashMap<String, String[]> cajamarcaData = new HashMap<>();
        cajamarcaData.put("Cajamarca", getResources().getStringArray(R.array.Cajamarca));
        cajamarcaData.put("Cajabamba", getResources().getStringArray(R.array.Cajabamba));
        cajamarcaData.put("Celendín", getResources().getStringArray(R.array.Celendin));
        cajamarcaData.put("Chota", getResources().getStringArray(R.array.Chota));
        cajamarcaData.put("Contumaza", getResources().getStringArray(R.array.Contumaza));
        cajamarcaData.put("Cutervo", getResources().getStringArray(R.array.Cutervo));
        cajamarcaData.put("Hualgayoc", getResources().getStringArray(R.array.Hualgayoc));
        cajamarcaData.put("Jaen", getResources().getStringArray(R.array.Jaen));
        cajamarcaData.put("San Ignacio", getResources().getStringArray(R.array.SanIgnacio));
        cajamarcaData.put("San Marcos", getResources().getStringArray(R.array.SanMarcos));
        cajamarcaData.put("San Miguel", getResources().getStringArray(R.array.SanMiguel));
        cajamarcaData.put("San Pablo", getResources().getStringArray(R.array.SanPablo));
        cajamarcaData.put("Santa Cruz", getResources().getStringArray(R.array.SantaCruz));
        //CALLAO:
        HashMap<String, String[]> callaoData = new HashMap<>();
        cajamarcaData.put("Callao", getResources().getStringArray(R.array.Callao));
        //CUSCO:
        HashMap<String, String[]> cuscoData = new HashMap<>();
        cuscoData.put("Acomayo", getResources().getStringArray(R.array.Acomayo));
        cuscoData.put("Anta", getResources().getStringArray(R.array.Anta));
        cuscoData.put("Calca", getResources().getStringArray(R.array.Calca));
        cuscoData.put("Canas", getResources().getStringArray(R.array.Canas));
        cuscoData.put("Canchis", getResources().getStringArray(R.array.Canchis));
        cuscoData.put("Chumbivilcas", getResources().getStringArray(R.array.Chumbivilcas));
        cuscoData.put("Cusco", getResources().getStringArray(R.array.Cusco));
        cuscoData.put("Espinar", getResources().getStringArray(R.array.Espinar));
        cuscoData.put("La Convención", getResources().getStringArray(R.array.LaConvencion));
        cuscoData.put("Paruro", getResources().getStringArray(R.array.Paruro));
        cuscoData.put("Paucartambo", getResources().getStringArray(R.array.Paucartambo));
        cuscoData.put("Quispicanchi", getResources().getStringArray(R.array.Quispicanchi));
        cuscoData.put("Urubamba", getResources().getStringArray(R.array.Urubamba));
        //HUANCAVELICA:
        HashMap<String, String[]> huancavelicaData = new HashMap<>();
        huancavelicaData.put("Huancavelica", getResources().getStringArray(R.array.Huancavelica));
        huancavelicaData.put("Acobamba", getResources().getStringArray(R.array.Acobamba));
        huancavelicaData.put("Angaraes", getResources().getStringArray(R.array.Angaraes));
        huancavelicaData.put("Castrovirreyna", getResources().getStringArray(R.array.Castrovirreyna));
        huancavelicaData.put("Churcampa", getResources().getStringArray(R.array.Churcampa));
        huancavelicaData.put("Huaytara", getResources().getStringArray(R.array.Huaytara));
        huancavelicaData.put("Tayacaja", getResources().getStringArray(R.array.Tayacaja));
        //HUANUCO:
        HashMap<String, String[]> huanucoData = new HashMap<>();
        huanucoData.put("Huanuco", getResources().getStringArray(R.array.Huanuco));
        huanucoData.put("Ambo", getResources().getStringArray(R.array.Ambo));
        huanucoData.put("Dos de Mayo", getResources().getStringArray(R.array.DosdeMayo));
        huanucoData.put("Huacaybamba", getResources().getStringArray(R.array.Huacaybamba));
        huanucoData.put("Huamalíes", getResources().getStringArray(R.array.Huamalies));
        huanucoData.put("Leoncio Prado", getResources().getStringArray(R.array.LeoncioPrado));
        huanucoData.put("Marañón", getResources().getStringArray(R.array.Marañon));
        huanucoData.put("Pachitea", getResources().getStringArray(R.array.Pachitea));
        huanucoData.put("Puerto Inca", getResources().getStringArray(R.array.PuertoInca));
        huanucoData.put("Lauricocha", getResources().getStringArray(R.array.Lauricocha));
        huanucoData.put("Yarowilca", getResources().getStringArray(R.array.Yarowilca));
        //ICA:
        HashMap<String, String[]> icaData = new HashMap<>();
        icaData.put("Ica", getResources().getStringArray(R.array.Ica));
        icaData.put("Chincha", getResources().getStringArray(R.array.Chincha));
        icaData.put("Nazca", getResources().getStringArray(R.array.Nazca));
        icaData.put("Palpa", getResources().getStringArray(R.array.Palpa));
        icaData.put("Pisco", getResources().getStringArray(R.array.Pisco));
        icaData.put("Subtanjalla", getResources().getStringArray(R.array.Subtanjalla));
        //JUNIN
        HashMap<String, String[]> juninData = new HashMap<>();
        juninData.put("Huancayo", getResources().getStringArray(R.array.Huancayo));
        juninData.put("Concepción", getResources().getStringArray(R.array.Concepcion));
        juninData.put("Chanchamayo", getResources().getStringArray(R.array.Chanchamayo));
        juninData.put("Jauja", getResources().getStringArray(R.array.Jauja));
        juninData.put("Junín", getResources().getStringArray(R.array.Junin));
        juninData.put("Satipo", getResources().getStringArray(R.array.Satipo));
        juninData.put("Tarma", getResources().getStringArray(R.array.Tarma));
        juninData.put("Yauli", getResources().getStringArray(R.array.Yauli));
        juninData.put("Chupaca", getResources().getStringArray(R.array.Chupaca));
        //LA LIBERTAD:
        HashMap<String, String[]> laLibertadData = new HashMap<>();
        laLibertadData.put("Trujillo", getResources().getStringArray(R.array.Trujillo));
        laLibertadData.put("Ascope", getResources().getStringArray(R.array.Ascope));
        laLibertadData.put("Bolívar", getResources().getStringArray(R.array.Bolivar));
        laLibertadData.put("Gran Chimú", getResources().getStringArray(R.array.GranChimu));
        laLibertadData.put("Julcán", getResources().getStringArray(R.array.Julcan));
        laLibertadData.put("Pacasmayo", getResources().getStringArray(R.array.Pacasmayo));
        laLibertadData.put("Pataz", getResources().getStringArray(R.array.Pataz));
        laLibertadData.put("Sánchez Carrión", getResources().getStringArray(R.array.SanchezCarrion));
        laLibertadData.put("Santiago de Chuco", getResources().getStringArray(R.array.SantiagodeChuco));
        laLibertadData.put("Virú", getResources().getStringArray(R.array.Viru));
        //LAMBAYEQUE:
        HashMap<String, String[]> lambayequeData = new HashMap<>();
        lambayequeData.put("Chiclayo", getResources().getStringArray(R.array.Motupe));
        lambayequeData.put("Ferreñafe", getResources().getStringArray(R.array.Ferrenafe));
        lambayequeData.put("Olmos", getResources().getStringArray(R.array.Olmos));
        lambayequeData.put("Lambayeque", getResources().getStringArray(R.array.lambayeque));
        //LIMA:
        HashMap<String, String[]> limaData = new HashMap<>();
        limaData.put("Lima", getResources().getStringArray(R.array.lima));
        limaData.put("Barranca", getResources().getStringArray(R.array.Barranca));
        limaData.put("Cajatambo", getResources().getStringArray(R.array.Cajatambo));
        limaData.put("Canta", getResources().getStringArray(R.array.Canta));
        limaData.put("Cañete", getResources().getStringArray(R.array.Cañete));
        limaData.put("Huaral", getResources().getStringArray(R.array.Huaral));
        limaData.put("Huarochirí", getResources().getStringArray(R.array.Huarochiri));
        limaData.put("Huaura", getResources().getStringArray(R.array.Huaura));
        limaData.put("Oyon", getResources().getStringArray(R.array.Oyon));
        limaData.put("Yauyos", getResources().getStringArray(R.array.Yauyos));
        //LORETO:
        HashMap<String, String[]> loretoData = new HashMap<>();
        loretoData.put("Alto Amazonas", getResources().getStringArray(R.array.AltoAmazonas));
        loretoData.put("Datem del Marañón", getResources().getStringArray(R.array.DatemdelMaranon));
        loretoData.put("Loreto", getResources().getStringArray(R.array.loreto));
        loretoData.put("Mariscal Ramón Castilla", getResources().getStringArray(R.array.MariscalRamonCastilla));
        loretoData.put("Maynas", getResources().getStringArray(R.array.Maynas));
        loretoData.put("Requena", getResources().getStringArray(R.array.Requena));
        loretoData.put("Ucayali", getResources().getStringArray(R.array.Ucayali));
        //MADRE DE DIOS:
        HashMap<String, String[]> madreDeDiosData = new HashMap<>();
        madreDeDiosData.put("Tambopata", getResources().getStringArray(R.array.Tambopata));
        madreDeDiosData.put("Manu", getResources().getStringArray(R.array.Manu));
        madreDeDiosData.put("Tahuamanu", getResources().getStringArray(R.array.Tahuamanu));
        //MOQUEGUA:
        HashMap<String, String[]> moqueguaData = new HashMap<>();
        moqueguaData.put("Mariscal Nieto", getResources().getStringArray(R.array.MariscalNieto));
        moqueguaData.put("Ilo", getResources().getStringArray(R.array.Ilo));
        moqueguaData.put("General Sánchez Cerro", getResources().getStringArray(R.array.GeneralSanchezCerro));
        //PASCO:
        HashMap<String, String[]> pascoData = new HashMap<>();
        pascoData.put("Pasco", getResources().getStringArray(R.array.Pasco));
        pascoData.put("Daniel Alcides Carrion", getResources().getStringArray(R.array.DanielAlcidesCarrion));
        pascoData.put("Oxapampa", getResources().getStringArray(R.array.Oxapampa));
        //PIURA:
        HashMap<String, String[]> piuraData = new HashMap<>();
        piuraData.put("Ayabaca", getResources().getStringArray(R.array.Ayabaca));
        piuraData.put("Huancabamba", getResources().getStringArray(R.array.Huancabamba));
        piuraData.put("Luciano Castillo Colonna", getResources().getStringArray(R.array.LucianoCastilloColonna));
        piuraData.put("Mercedes Cáceres", getResources().getStringArray(R.array.MercedesCaceres));
        piuraData.put("Sechura", getResources().getStringArray(R.array.Sechura));
        piuraData.put("Paita", getResources().getStringArray(R.array.Paita));
        piuraData.put("Piura", getResources().getStringArray(R.array.Piura));
        piuraData.put("Talara", getResources().getStringArray(R.array.Talara));
        //PUNO:
        HashMap<String, String[]> punoData = new HashMap<>();
        punoData.put("Azángaro", getResources().getStringArray(R.array.Azangaro));
        punoData.put("Carabaya", getResources().getStringArray(R.array.Carabaya));
        punoData.put("Chucuito", getResources().getStringArray(R.array.Chucuito));
        punoData.put("El Collao", getResources().getStringArray(R.array.ElCollao));
        punoData.put("Huancané", getResources().getStringArray(R.array.Huancane));
        punoData.put("Lampa", getResources().getStringArray(R.array.Lampa));
        punoData.put("Melgar", getResources().getStringArray(R.array.Melgar));
        punoData.put("Puno", getResources().getStringArray(R.array.Puno));
        punoData.put("San Antonio de Putina", getResources().getStringArray(R.array.SanAntoniodePutina));
        punoData.put("Sandia", getResources().getStringArray(R.array.Sandia));
        punoData.put("Yunguyo", getResources().getStringArray(R.array.Yunguyo));
        //SAN MARTIN:
        HashMap<String, String[]> sanMartinData = new HashMap<>();
        sanMartinData.put("Bellavista", getResources().getStringArray(R.array.Bellavista));
        sanMartinData.put("El Dorado", getResources().getStringArray(R.array.ElDorado));
        sanMartinData.put("Huallaga", getResources().getStringArray(R.array.Huallaga));
        sanMartinData.put("Lamas", getResources().getStringArray(R.array.Lamas));
        sanMartinData.put("Mariscal Caceres", getResources().getStringArray(R.array.MariscalCaceres));
        sanMartinData.put("Moyobamba", getResources().getStringArray(R.array.Moyobamba));
        sanMartinData.put("Picota", getResources().getStringArray(R.array.Picota));
        sanMartinData.put("Rioja", getResources().getStringArray(R.array.Rioja));
        sanMartinData.put("San Martín", getResources().getStringArray(R.array.SanMartin));
        sanMartinData.put("Tocache", getResources().getStringArray(R.array.Tocache));
        //TACNA:
        HashMap<String, String[]> tacnaData = new HashMap<>();
        tacnaData.put("Tacna", getResources().getStringArray(R.array.Tacna));
        tacnaData.put("Candarave", getResources().getStringArray(R.array.Candarave));
        tacnaData.put("Jorge Basadre", getResources().getStringArray(R.array.JorgeBasadre));
        tacnaData.put("Tarata", getResources().getStringArray(R.array.Tarata));
        //TUMBES:
        HashMap<String, String[]> tumbesData = new HashMap<>();
        tumbesData.put("Tumbes", getResources().getStringArray(R.array.Tumbes));
        tumbesData.put("Zarumilla", getResources().getStringArray(R.array.Zarumilla));
        tumbesData.put("Contralmirante Villar", getResources().getStringArray(R.array.ContralmiranteVillar));
        //UCAYALI:
        HashMap<String, String[]> ucayaliData = new HashMap<>();
        ucayaliData.put("Pucallpa", getResources().getStringArray(R.array.Pucallpa));
        ucayaliData.put("Coronel Portillo", getResources().getStringArray(R.array.CoronelPortillo));
        ucayaliData.put("Atalaya", getResources().getStringArray(R.array.Atalaya));
        ucayaliData.put("Padre Abad", getResources().getStringArray(R.array.PadreAbad));
        ucayaliData.put("Purus", getResources().getStringArray(R.array.Purus));


        // Añade más provincias y departamentos según tus necesidades

        data.put("Amazonas", amazonasData);
        data.put("Ancash", ancashData);
        data.put("Apurimac", apurimacData);
        data.put("Arequipa", arequipaData);
        data.put("Ayacucho", ayacuchoData);
        data.put("Cajamarca", cajamarcaData);
        data.put("Callao", callaoData);
        data.put("Cusco", cuscoData);
        data.put("Huancavelica", huancavelicaData);
        data.put("Huanuco", huanucoData);
        data.put("Ica", icaData);
        data.put("Junin", juninData);
        data.put("La Libertad", laLibertadData);
        data.put("Lambayeque", lambayequeData);
        data.put("Lima", limaData);
        data.put("Loreto", loretoData);
        data.put("Madre de Dios", madreDeDiosData);
        data.put("Moquegua", moqueguaData);
        data.put("Pasco", pascoData);
        data.put("Piura", piuraData);
        data.put("Puno", punoData);
        data.put("San Martin", sanMartinData);
        data.put("Tacna", tacnaData);
        data.put("Tumbes", tumbesData);
        data.put("Ucayali", ucayaliData);

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
