package com.example.texteditor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.texteditor.R;
import com.example.texteditor.SettingsActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private final static String FILENAME = "sample.txt"; // имя файла
    private EditText mEditText;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.editText);
        toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(FILENAME); // заголовок
        toolbar.setTitleTextColor(Color.CYAN);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_open) {
            openFile(FILENAME);
            return true;
        }
        else if (item.getItemId() == R.id.action_save) {
            saveFile(FILENAME);
            return true;
        }
        else if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isOpenMode = prefs.getBoolean(getString(R.string.pref_openmode), false);
        if (isOpenMode) {
            openFile(FILENAME);
        }
        float fSize = Float.parseFloat(prefs.getString(getString(R.string.pref_size), "20"));
        mEditText.setTextSize(fSize);

        String regular = prefs.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;

        if (regular.contains("Полужирный")) {
            typeface += Typeface.BOLD;
        }

        if (regular.contains("Курсив")) {
            typeface += Typeface.ITALIC;
        }

        mEditText.setTypeface(mEditText.getTypeface(), typeface);

    }

    private void openFile(String fileName) {
        try {
            InputStream inputStream = openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                inputStream.close();
                mEditText.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void saveFile(String fileName) {
        try {
            OutputStream outputStream = openFileOutput(fileName, 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(mEditText.getText().toString());
            osw.close();
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
}