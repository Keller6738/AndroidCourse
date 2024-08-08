package com.example.memory2024;

import static android.content.Intent.ACTION_PICK;
import static android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.memory2024.memory_game.MemoryGameActivity;
import com.example.memory2024.tic_tac_toe.XOGameActivity;

import java.util.Locale;

public class OpenActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPlay, btnContact1, btnContact2;
    private ImageButton btnBack;
    private EditText edName1, edName2;
    private TextView tvTitle;

    private boolean memoryGame;

    private TextToSpeech textToSpeech;

    private ActivityResultLauncher<Intent> contentLauncher;
    private int playerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPlay = findViewById(R.id.btmStart);
        btnPlay.setOnClickListener(this);

        btnBack = findViewById(R.id.imgBack);
        btnBack.setOnClickListener(this);

        btnContact1 = findViewById(R.id.btnContact1);
        btnContact1.setOnClickListener(this);

        btnContact2 = findViewById(R.id.btnContact2);
        btnContact2.setOnClickListener(this);

        edName1 = findViewById(R.id.etName1);
        edName2 = findViewById(R.id.etName2);

        tvTitle = findViewById(R.id.tvTitle);

        Intent intent = getIntent();
        Bundle extras;

        if (intent != null && intent.getExtras() != null) {
            extras = intent.getExtras();
            memoryGame = extras.getBoolean("game");
        }

        if (!memoryGame) {
            tvTitle.setText("Tic Tac Toe");
        }

        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i == TextToSpeech.SUCCESS) {
                int lang = textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });

        initContentP();
    }

    private void initContentP() {
        contentLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Cursor cursor;
                            Uri uri;

                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent intent = result.getData();
                                try {
                                    uri = intent.getData();

                                    cursor = getContentResolver().query(uri, null, null, null, null);
                                    cursor.moveToFirst();


                                    int phoneIndexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                                    String phoneName = cursor.getString(phoneIndexName);

                                    if (playerNum == 1) {
                                        edName1.setText(phoneName);
                                    } else {
                                        edName2.setText(phoneName);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        Intent contactPickerIntent;

        String name1 = edName1.getText().toString().trim();
        String name2 = edName2.getText().toString().trim();

        if (view.getId() == btnContact1.getId()) {
            this.playerNum = 1;
            contactPickerIntent = new Intent(ACTION_PICK);
            contactPickerIntent.setType(CONTENT_TYPE);
            contentLauncher.launch(contactPickerIntent);
        } else if (view.getId() == btnContact2.getId()) {
            this.playerNum = 2;
            contactPickerIntent = new Intent(ACTION_PICK);
            contactPickerIntent.setType(CONTENT_TYPE);

            contentLauncher.launch(contactPickerIntent);
        } else if (view.getId() == btnPlay.getId()) {
            if (name1.isEmpty() ||
                    name2.isEmpty() ||
                    name1.equals(name2)) {
                new AlertDialog.Builder(this)
                        .setTitle("ERROR!!")
                        .setMessage("User names are wrong!")
                        .setNeutralButton("ok", (dialogInterface, i) -> {
                        })
                        .setIcon(R.drawable.error)
                        .show();
            } else {
                textToSpeech.speak("Good Luck, " + name1 + "and" + name2,
                        TextToSpeech.QUEUE_FLUSH, null);

                intent = new Intent(
                        this,
                        memoryGame ?
                                MemoryGameActivity.class : XOGameActivity.class
                );
                intent.putExtra("NAME1", name1);
                intent.putExtra("NAME2", name2);
                startActivity(intent);
            }
        } else if (view.getId() == btnBack.getId()) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
