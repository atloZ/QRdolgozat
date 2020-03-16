package hu.zolta.qrdolgozat;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnScan, btnKiir;
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan();
            }
        });

        btnKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiir(tvText.getText().toString());
            }
        });

    }

    private void qrScan(){
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("QR Code Scanning by dolgozat");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);       //!!
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Kiléptünk a scannelésből", Toast.LENGTH_SHORT).show();
            }else
            {
                tvText.setText("QR Code eredmény: \n" + result.getContents());

                Uri uri = Uri.parse(result.getContents());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void kiir(String text){
        String allapot;
        File file;
        String szovegesAdat;
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        String formatedDate = dateFormat.format(date);

        szovegesAdat = text + "," + formatedDate + "\n";

        allapot = Environment.getExternalStorageState();
        if (allapot.equals(Environment.MEDIA_MOUNTED))
        {
            file = new File(Environment.getExternalStorageDirectory(), "scannedCodes.csv");
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true),1024);
                bufferedWriter.append(szovegesAdat);
                bufferedWriter.close();
            }catch (IOException e)
            {

            }
        }
    }

    private void init(){
        btnScan.findViewById(R.id.btnScan);
        btnKiir.findViewById(R.id.btnKiir);
        tvText.findViewById(R.id.textViewActi1);
    }
}
