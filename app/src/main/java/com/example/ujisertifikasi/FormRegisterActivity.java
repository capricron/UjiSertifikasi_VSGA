package com.example.ujisertifikasi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class FormRegisterActivity extends AppCompatActivity {

    private TextView locationText;

    private EditText namaInput;
    private EditText alamatInput;
    private String jenisKelamin;
    private String nama;
    private String alamat;
    private double latitude;
    private double longitude;

    private ImageView objectImage;
    private static final int PICK_IMAGE_REQUEST = 100;
    private Bitmap imageToStore;
    DatabaseHandler objectDatabaseHandler;
    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_register);

        namaInput = findViewById(R.id.input_nama);
        alamatInput = findViewById(R.id.input_alamat);
        RadioGroup radioGroupJenisKelamin = findViewById(R.id.radio_group_jenis_kelamin);
        locationText = findViewById(R.id.text_location);
        Button getLocationButton = findViewById(R.id.btn_lokasi);
        Button btnSubmit = findViewById(R.id.btn_submit);
        objectImage = findViewById(R.id.img_foto);
        Button btnUpload = findViewById(R.id.btn_upload_gambar);

        objectDatabaseHandler = new DatabaseHandler(this);

        radioGroupJenisKelamin.setOnCheckedChangeListener((group, id) -> {
            switch (id) {
                case R.id.radio_button_pria:
                    jenisKelamin = "pria";
                    break;
                case R.id.radio_button_wanita:
                    jenisKelamin = "wanita";
                    break;
            }
        });

        // inisialisasi fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationButton.setOnClickListener(v -> {
            // check perizinan
            if (ActivityCompat.checkSelfPermission(FormRegisterActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // ketika diizinkan
                getLocation();
            } else {
                // ketika tidak diizinkan
                ActivityCompat.requestPermissions(FormRegisterActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        404);
            }
        });

        btnUpload.setOnClickListener(v -> chooseImage());

        btnSubmit.setOnClickListener(v -> {
            nama =  namaInput.getText().toString();
            alamat = alamatInput.getText().toString();
            if(storeData()) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void chooseImage(){
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,PICK_IMAGE_REQUEST);
        }catch (Exception e){
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean storeData(){
        try {
            if(!nama.isEmpty() &&
                    !alamat.isEmpty() &&
                    !jenisKelamin.isEmpty() &&
                    latitude != 0 &&
                    longitude != 0 &&
                    objectImage.getDrawable() != null){
                objectDatabaseHandler.storeData(new ModelClass(nama, alamat,jenisKelamin, latitude, longitude, imageToStore));
                return true;
            }else{
                Toast.makeText(getApplication(), "Please Fill All Data", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (Exception e){
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK  && data != null && data .getData() != null){
                Uri imageFilePath = data.getData();
                imageToStore= MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                objectImage.setImageBitmap(imageToStore);

            }
        }catch (Exception e){
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            // inisialisasi location
            Location location = task.getResult();
            if (location != null) {
                //  inisialisasi list alamat
                try {
                    // inisialisasi geocoder
                    Geocoder geocoder = new Geocoder(FormRegisterActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                    latitude =  addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();
                    locationText.setText(Html.fromHtml("Latitude: " + latitude)+
                            "\nLongitude: " + longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}