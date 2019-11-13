package com.example.practice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class ExcelSignUp extends AppCompatActivity {
    Button open, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_sign_up);
        open = findViewById(R.id.button);
        back = findViewById(R.id.back);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, 1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExcelSignUp.this, signUpPage.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String path = data.getData().getPath();

            /*ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();*/
            try {
                //Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
                path = "/Documents/Book1.csv";
                Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
               // File csvfile = new File(Environment.getExternalStorageDirectory() + "/csvfile.csv");
                File csvfile = new File(path);
                InputStream inputStream = new FileInputStream(csvfile);
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                Toast.makeText(getApplicationContext(),""+rowsCount,Toast.LENGTH_SHORT).show();
               // FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
               // StringBuilder sb = new StringBuilder();

                // CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));

               // CSVReader reader = new CSVReader(new FileReader(path));
               // String[] nextLine;
              //  while ((nextLine = reader.readNext()) != null) {
                    // nextLine[] is an array of values from the line
                //    Toast.makeText(getApplicationContext(),nextLine[0] + nextLine[1] + "etc...",Toast.LENGTH_SHORT).show();
               // }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();

            }


        }
    }
}
