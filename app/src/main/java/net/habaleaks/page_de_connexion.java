package net.habaleaks;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class page_de_connexion extends AppCompatActivity {
    Button btn_connexion, btn_parameters;
    TextView phone_number;
   static int testTemporairePourConnexion = 0;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchtesttechnicien;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_de_connexion);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        phone_number = (TextView) findViewById(R.id.TVnumconnexionpagedeconnexion);
        btn_connexion = (Button)findViewById(R.id.btnconnexion);
        switchtesttechnicien = (Switch) findViewById(R.id.switchtesttechnicien);


        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("HardwareIds") String phoneNumber = telephonyManager.getLine1Number();

            phone_number.setText(phoneNumber);
            return;
        } else {
            requestPermission();
        }

        switchtesttechnicien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // on below line we are checking
                // if switch is checked or not.
                if (isChecked) {
                    testTemporairePourConnexion =1;
                } else {
                    testTemporairePourConnexion =0;
                }
            }
        });


        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testTemporairePourConnexion == 1) {
                    Intent intentConnexiontechnicien = new Intent(page_de_connexion.this, page_techniciens.class);
                    intentConnexiontechnicien.putExtra("testTemporairePourConnexionTechnicien", testTemporairePourConnexion);
                    startActivity(intentConnexiontechnicien);
                }
                if (testTemporairePourConnexion ==0 ){
                    Intent intentConnexionAgtEntretient = new Intent(page_de_connexion.this, signaler_incidents.class);
                    intentConnexionAgtEntretient.putExtra("testTemporairePourConnexionAgtEntretient", testTemporairePourConnexion);
                    startActivity(intentConnexionAgtEntretient);
                }
            }
        });
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String phoneNumber = telephonyManager.getLine1Number();
                phone_number.setText(phoneNumber);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }


}