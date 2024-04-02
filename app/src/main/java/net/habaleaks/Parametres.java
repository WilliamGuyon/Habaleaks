package net.habaleaks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Parametres extends  AppCompatActivity{
    EditText num, ip_server;
    Button btn_save;
    String phoneNumber, ipServer;
    SharedPreferences sp;
    public final String SHARED_PREFS = "sharedPrefs";
    public final String PHONE_NUMBER_TEXT = "phoneNumberText";
    public final String IP_ADDRESS_TEXT = "ipAddressText";

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametres);

        num = findViewById(R.id.num);
        ip_server = findViewById(R.id.ipserver);
        btn_save = findViewById(R.id.buttonsave);




        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();

                editor.putString(PHONE_NUMBER_TEXT, num.getText().toString());
                editor.putString(IP_ADDRESS_TEXT, ip_server.getText().toString());
                editor.apply();
                Toast.makeText(Parametres.this,"Parametres sauvegardés", Toast.LENGTH_LONG).show();
            }
        });

    }

}