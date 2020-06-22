package com.example.mysql;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private Button emplBtn;
    private EditText ide;
    private EditText name;
    private EditText surname;
    private EditText middlename;
    private ToggleButton btnstate;
    private ToggleButton btnRfidIDstate;
    private EditText txtTagContent;

    private String idstring;
    private  long idlong;
    private String namestring;
    private String surnamestring;
    private String middlenamestring;
    private int btnint;
    private int btnRfidID;
    private String rfid_id;

    private getInfo Getinfo;
    private setInfo SetInfo;
    private setRfid SetRfid;
    private getInfoByRFID GetinfoByRFID;

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emplBtn = findViewById(R.id.button2);
        emplBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmplList();
            }
        });
        ide = (EditText) findViewById(R.id.editTextID);
        name = (EditText) findViewById(R.id.editTextName);
        name.setFocusableInTouchMode(false);
        name.setFocusable(false);
        surname = (EditText) findViewById(R.id.editTextSurname);
        surname.setFocusableInTouchMode(false);
        surname.setFocusable(false);
        middlename = (EditText) findViewById(R.id.editTextMiddlename);
        middlename.setFocusableInTouchMode(false);
        middlename.setFocusable(false);
        btnstate = (ToggleButton) findViewById(R.id.toggleButton);
        btnRfidIDstate = (ToggleButton) findViewById(R.id.button3);
        txtTagContent = (EditText) findViewById(R.id.txtTagContent);
        txtTagContent.setFocusable(false);
        txtTagContent.setFocusableInTouchMode(false);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC работает", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC выключен или нет такой возможности у телефона", Toast.LENGTH_LONG).show();

        }
    }
    private void openEmplList() {
        Intent intent = new Intent(this, ListEmployee.class);
        startActivity(intent);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        //  Toast.makeText(this, "NFC intent доступен", Toast.LENGTH_LONG).show();
        super.onNewIntent(intent);

        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id = tag.getId();
        String serialNumber = bytesToHex(id);
        txtTagContent.setText(serialNumber);
        if (!txtTagContent.equals("") && btnRfidIDstate.isChecked()) {
            txtTagContent.setText(serialNumber);
            SetRfid = new setRfid();
            if (btnRfidID == 1) {
                SetRfid.start(idstring, txtTagContent.getText().toString());
            }
            try {
                SetRfid.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0 ", ie.getMessage());
            }

        } else {
            rfid_id = txtTagContent.getText().toString();
            GetinfoByRFID = new getInfoByRFID();
            Toast.makeText(this,serialNumber, Toast.LENGTH_LONG).show();
            GetinfoByRFID.start(serialNumber);
            try {
                GetinfoByRFID.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0 ", ie.getMessage());
            }

            namestring = GetinfoByRFID.resname();
            surnamestring = GetinfoByRFID.ressurname();
            middlenamestring = GetinfoByRFID.resmiddlename();
            btnint = GetinfoByRFID.resbtnstate();
            idstring = GetinfoByRFID.getIde();

            name.setText(namestring);
            surname.setText(surnamestring);
            middlename.setText(middlenamestring);
            ide.setText(idstring);

            if (btnint == 1) {
                btnstate.setChecked(true);
            } else {
                btnstate.setChecked(false);
            }
        }

    }
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        super.onResume();
    }
    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }
    public void OnClick(View view) {
        if (btnRfidIDstate.isChecked()) {
            if (!txtTagContent.getText().toString().equals("")) {
                idstring = ide.getText().toString();
                rfid_id = txtTagContent.getText().toString();
                if (btnstate.isChecked()) {
                    btnint = 1;
                } else {
                    btnint = 0;
                }
                SetInfo = new setInfo();
                if (btnRfidID == 1) {
                    SetInfo.start(idstring, btnint, rfid_id);
                }
                try {
                    SetInfo.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0 ", ie.getMessage());
                }
            } else {
                Toast.makeText(this, "Сначала введите ID, а только потом включайте режим записи!", Toast.LENGTH_LONG).show();
            }
        } else {

            idstring = ide.getText().toString();
            Getinfo = new getInfo();
            Getinfo.start(idstring);
            try {
                Getinfo.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0 ", ie.getMessage());
            }

            namestring = Getinfo.resname();
            surnamestring = Getinfo.ressurname();
            middlenamestring = Getinfo.resmiddlename();
            btnint = Getinfo.resbtnstate();
            rfid_id = Getinfo.getRfid_id();

            name.setText(namestring);
            surname.setText(surnamestring);
            middlename.setText(middlenamestring);
            txtTagContent.setText(rfid_id);
            if (btnint == 1) {
                btnstate.setChecked(true);
            } else {
                btnstate.setChecked(false);
            }
        }
    }
    public void OnClickState(View view) {
        idstring = ide.getText().toString();
        rfid_id = txtTagContent.getText().toString();
        if (btnstate.isChecked()) {
            btnint = 1;
        } else {
            btnint = 0;
        }
        SetInfo = new setInfo();
       // SetRfid = new setRfid();
        SetInfo.start(idstring, btnint, txtTagContent.getText().toString());
        try {
            SetInfo.join();
        } catch (InterruptedException ie) {
            Log.e("pass 0 ", ie.getMessage());
        }
    }
    public void sendRGID_ID(View view) {
        rfid_id = txtTagContent.getText().toString();
        if (btnRfidIDstate.isChecked()) {
            btnRfidID = 1;
            txtTagContent.setText("");
        } else {
            btnRfidID = 0;
        }
    }
}
