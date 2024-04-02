package net.habaleaks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class page_techniciens extends AppCompatActivity {
    Button btnsignalerIncidentTechnicien, btnListeIncidents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.technicien);

        btnsignalerIncidentTechnicien = (Button)findViewById(R.id.btn_signalerTechnicien);
        btnListeIncidents = (Button)findViewById(R.id.btn_liste_incidents);

        Intent intentConnexion = getIntent();
        if (intentConnexion.hasExtra("testTemporairePourConnexionTechnicien"))
        { // vérifie qu'une valeur est associée à la clé “testTemporairePourConnexion”
            // on récupère la valeur associée à la clé le 0 est une valeur par default
            int testTemporairePourConnexion = intentConnexion.getIntExtra("testTemporairePourConnexionTechnicien", 0);
        }

        btnsignalerIncidentTechnicien.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Intent intentSignalerIncident = new Intent(page_techniciens.this, signaler_incidents.class);
                    startActivity(intentSignalerIncident);
            }
        });

        btnListeIncidents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Intent intentListeIncidents = new Intent(page_techniciens.this, liste_incidents.class);
                    startActivity(intentListeIncidents);
            }
        });
    }
}
