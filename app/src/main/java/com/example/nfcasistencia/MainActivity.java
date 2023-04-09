package com.example.nfcasistencia;

import androidx.annotation.NonNull;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private TextView attendanceCounter;
    private NfcAdapter nfcAdapter;
    private NFCUtil nfcUtil;
    private SharedPreferences preferences;
    private int attendanceCount = 0;

    private static final int PERMISSION_REQUEST_NFC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener la instancia de SharedPreferences
        preferences = getPreferences(MODE_PRIVATE);

        // Recuperar la cantidad de asistencias guardada, o usar cero como valor predeterminado
        attendanceCount = preferences.getInt("attendanceCount", 0);

        attendanceCounter = findViewById(R.id.attendance_count);
        checkNFCPermission();
        setupNFC();

        // Actualizar el contador con la cantidad de asistencias recuperada
        updateAttendanceCounter();
    }

    private void checkNFCPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.NFC}, PERMISSION_REQUEST_NFC);
            }
        }
    }

    private void setupNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC no está disponible en este dispositivo", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        nfcUtil = new NFCUtil(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcUtil.enableForegroundDispatchSystem(this, nfcAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcUtil.disableForegroundDispatchSystem(nfcAdapter);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (nfcUtil.isAttendanceTag(intent)) {
            attendanceCount++;
            updateAttendanceCounter();

            // Guardar la cantidad de asistencias actualizada en SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("attendanceCount", attendanceCount);
            editor.apply();
        }
    }

    private void updateAttendanceCounter() {
        attendanceCounter.setText(String.format("N° Asistencias: %d", attendanceCount));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_NFC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso NFC concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso NFC denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }



}




