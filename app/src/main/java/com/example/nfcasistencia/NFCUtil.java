package com.example.nfcasistencia;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;

public class NFCUtil {

    private final Context context;

    public NFCUtil(Context context) {
        this.context = context;
    }

    public static PendingIntent createPendingIntent(Context context, int requestCode) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void enableForegroundDispatchSystem(MainActivity activity, NfcAdapter nfcAdapter) {
        int requestCode = 0; // Define the requestCode variable
        PendingIntent pendingIntent = createPendingIntent(context, requestCode);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilters, null);
        }
    }


    public void disableForegroundDispatchSystem(NfcAdapter nfcAdapter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            nfcAdapter.disableForegroundDispatch((MainActivity) context);
        }
    }
    public boolean isAttendanceTag(Intent intent) {
        // Siempre devolver true para cualquier etiqueta NFC
        return true;
    }
//    public boolean isAttendanceTag(Intent intent) {
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {

//            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            if (tag != null) {
//                // Aquí puedes verificar si la etiqueta NFC es válida para asistencia, por ejemplo, comprobando su ID o contenido
//                // Si la etiqueta es válida, devuelve true
//                return true;
//            }
//        }
//        // Si la etiqueta no es válida o no se encuentra, devuelve false
//        return false;
//    }
}


