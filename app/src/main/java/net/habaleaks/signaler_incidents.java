package net.habaleaks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class signaler_incidents extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    private Spinner spinner_lieux, spinner_defauts;
    private Button btn_envoyer;
    private ImageView btn_photo, img_ajout_photo;
    private String photoPath = null, encodedImage = "";
    private EditText commentaire;
    private static final int RETOUR_PRENRDE_PHOTO = 1, PERMISSION_REQUEST_INTERNET = 1, PERMISSION_REQUEST_LOCALISATION = 1;
    private int iddefaut, idlieu, idpersonnel, testPermissionInternet, testPermissionLocalisation;
    Switch switchLocalisation;
    private Double longitude = null;
    private Double latitude = null;

    private String fournisseurLoc;

    private Signalement signalementTest;


    LocationManager locationManager = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signaler_incident);

        btn_photo = (ImageView) findViewById(R.id.cadre_photo);
        spinner_lieux = (Spinner) findViewById(R.id.spinner_lieux);
        spinner_defauts = (Spinner) findViewById(R.id.spinner_categorie);
        img_ajout_photo = (ImageView) findViewById(R.id.img_ajt_photo);
        btn_envoyer = (Button) findViewById(R.id.btn_envoyer);
        commentaire = (EditText) findViewById(R.id.EditTextCommentaire);
        switchLocalisation = (Switch) findViewById(R.id.switch_localisation);

        createOnClickBtnPrendrePhoto();
        createOnClickBtnEnvoyer();

        Intent intentConnexion = getIntent();
        if (intentConnexion.hasExtra("testTemporairePourConnexionAgtEntretient")) { // vérifie qu'une valeur est associée à la clé “testTemporairePourConnexion”
            // on récupère la valeur associée à la clé le 0 est une valeur par default
            int testTemporairePourConnexion = intentConnexion.getIntExtra("testTemporairePourConnexionAgtEntretient", 0);
        }

        new ApiTaskLieu().execute();
        new ApiTaskDefaut().execute();


        spinner_defauts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iddefaut = (int) spinner_defauts.getSelectedItemId() + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_lieux.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iddefaut = (int) spinner_lieux.getSelectedItemId() + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        switchLocalisation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    checkAndRequestLocalisationPermission();

                    if (testPermissionLocalisation == 1) {
                        initialiserLocalisation();
                    }
                } else {
                    longitude = null;
                    latitude = null;
                }
            }
        });

    }

    //ApiTask pour créer récuperer la liste des lieux et l'inclure dans un spinner
    private class ApiTaskLieu extends AsyncTask<Void, Void, List<Lieu>> {

        @Override
        protected List<Lieu> doInBackground(Void... voids) {
            ApiClient apiClient = new ApiClient();
            try {
                return apiClient.GetAllLieux();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Lieu> lieux) {
            super.onPostExecute(lieux);

            if (lieux != null) {
                ArrayList<String> nomLieux = new ArrayList<String>();
                for (int i = 0; i < lieux.size(); i++) {
                    nomLieux.add(lieux.get(i).getlieu());
                }

                ArrayAdapter<String> arrayadapterLieux = new ArrayAdapter(signaler_incidents.this, android.R.layout.simple_list_item_1, nomLieux);
                arrayadapterLieux.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinner_lieux.setAdapter(arrayadapterLieux);
            } else {
                Toast.makeText(signaler_incidents.this, "Impossible de recuperer les lieux", Toast.LENGTH_LONG).show();
            }
        }
    }

    //ApiTask pour créer récuperer la liste des defauts et l'inclure dans un spinner
    private class ApiTaskDefaut extends AsyncTask<Void, Void, List<Defaut>> {

        @Override
        protected List<Defaut> doInBackground(Void... voids) {
            ApiClient apiClient = new ApiClient();
            try {
                return apiClient.GetAllDefauts();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Defaut> defaut) {
            super.onPostExecute(defaut);

            if (defaut != null) {
                ArrayList<String> nomDefauts = new ArrayList<String>();
                for (int i = 0; i < defaut.size(); i++) {
                    nomDefauts.add(defaut.get(i).getdescription());
                }

                ArrayAdapter<String> arrayadapterDefauts = new ArrayAdapter(signaler_incidents.this, android.R.layout.simple_list_item_1, nomDefauts);
                arrayadapterDefauts.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spinner_defauts.setAdapter(arrayadapterDefauts);
            } else {
                Toast.makeText(signaler_incidents.this, "Impossible de recuperer les defauts", Toast.LENGTH_LONG).show();
            }
        }
    }


    //ApiTask pour envoyer un signalement et vérifier si il a bien été envoyé
    private class ApiTaskEnvoyer extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            ApiClient apiClient = new ApiClient();
            try {
                return apiClient.AddSignalement(signalementTest);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                //adapter = new DefautAdapter(MainActivity.this, defauts);
                //listeLieux.setAdapter(adapter);
                Toast.makeText(signaler_incidents.this, "Ajout réussi", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(signaler_incidents.this, "Ajout nul", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void createOnClickBtnPrendrePhoto() {
        btn_photo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendreUnePhoto();
            }
        });
    }

    private void createOnClickBtnEnvoyer() {
        btn_envoyer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestInternetPermission();

                if (testPermissionInternet == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        signalementTest = new Signalement(1, 5, 8, 5, java.sql.Date.valueOf(String.valueOf(LocalDate.now())), 0, commentaire.getText().toString(), false, latitude.toString(), longitude.toString(), encodedImage);
                        new ApiTaskEnvoyer().execute();

                    }
                }
            }
        });
    }

    // Méthode où vous vérifiez et demandez la permission internet
    private void checkAndRequestInternetPermission() {
        if (ActivityCompat.checkSelfPermission(signaler_incidents.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            // Permission déjà accordée, effectuez votre action ici
            testPermissionInternet = 1;
        } else {
            // Demande de permission
            ActivityCompat.requestPermissions(signaler_incidents.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
        }
    }

    // Méthode où vous vérifiez et demandez la permission localisation
    private void checkAndRequestLocalisationPermission() {
        if (ActivityCompat.checkSelfPermission(signaler_incidents.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission déjà accordée, effectuez votre action ici
            testPermissionLocalisation = 1;
        } else {
            // Demande de permission
            ActivityCompat.requestPermissions(signaler_incidents.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCALISATION);
        }
    }

    // Méthode appelée lorsque l'utilisateur répond à la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_INTERNET) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission acces internet accordée
                testPermissionInternet = 1;
            } else {
                // Permission acces internet refusée
                testPermissionInternet = 0;
            }
        }
        if (requestCode == PERMISSION_REQUEST_LOCALISATION) {
            if (permissions.length == 1 && permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission localisation accordée
                testPermissionLocalisation = 1;
            } else {
                // Permission localisatoin refusée
                testPermissionLocalisation = 0;
                Toast.makeText(signaler_incidents.this, "Accés à la localisation refusé", Toast.LENGTH_LONG).show();
            }
        }
    }

    //méthode pour prendre une photo
    private void prendreUnePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo" + time, "jpg", photoDir);
                photoPath = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(signaler_incidents.this,
                        signaler_incidents.this.getApplicationContext().getPackageName() + ".provider"
                        , photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, RETOUR_PRENRDE_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //service de localisation
    public void initialiserLocalisation() {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteres = new Criteria();

            // la précision
            criteres.setAccuracy(Criteria.ACCURACY_FINE);

            // l'altitude
            criteres.setAltitudeRequired(true); 

            // la direction
            criteres.setBearingRequired(true);

            fournisseurLoc = locationManager.getBestProvider(criteres, true);
            Log.d("GPS", "fournisseur : " + fournisseurLoc);
        }

        if (fournisseurLoc != null) {
            // dernière position connue
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            Location localisation = locationManager.getLastKnownLocation(fournisseurLoc);
                Log.d("GPS", "localisation : " + localisation.toString());
                @SuppressLint("DefaultLocale") String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
                latitude = localisation.getLatitude();
                longitude = localisation.getLongitude();
                Log.d("GPS", "coordonnees : " + coordonnees);
        }
    }

    //conversion bitArray en format blob(base64)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RETOUR_PRENRDE_PHOTO && resultCode==RESULT_OK){
            Bitmap image = BitmapFactory.decodeFile(photoPath);
            btn_photo.setImageBitmap(image);
            img_ajout_photo.setVisibility(View.INVISIBLE);

            byte[] bytearray = getBytesFromBitmap(image);
            //String s = new String(bytearray, StandardCharsets.UTF_8);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encodedImage = Base64.getEncoder().encodeToString(bytearray);
                Log.d("ICI", encodedImage.toString());
            }
        }
    }

    //conversion d'un format JPEG==>array de bit
    public static byte[] getBytesFromBitmap(Bitmap bitmap){
        if (bitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            return stream.toByteArray();
        }
        return null;
    }

}
