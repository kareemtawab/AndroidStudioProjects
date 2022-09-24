package com.kareem.miniproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EditNoteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DialogCustomNoteHeader.ExampleDialogListener{

    private TextView headerTV;
    private EditText editNote;
    String noteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_text, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        headerTV = (TextView) findViewById(R.id.selectednoteheader);
        editNote = findViewById(R.id.edittext);

        String compareValue = getIntent().getStringExtra("title");
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.spinner_text, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        if (compareValue != null) {
            int spinnerPosition = ad.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
        }
        editNote.setText(getIntent().getStringExtra("body"));

        Button saveBtn = findViewById(R.id.savenotebutton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notes n = new Notes();
                n.setNotetitle(noteTitle);
                n.setNotebody(editNote.getText().toString());
                addNewNote(n);
                Toast.makeText(EditNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(view.getContext(), MainActivity.class);
                startActivity(in);
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(adapterView.getContext(), "Selected: " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
        if (i == 7){
            openDialog();
        }
        else{
            noteTitle = adapterView.getItemAtPosition(i).toString();
            headerTV.setText("");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void applyTexts(String nh) {
        headerTV.setText("Custom Header: " + nh);
        noteTitle = nh;
    }

    public void openDialog() {
        DialogCustomNoteHeader exampleDialog = new DialogCustomNoteHeader();
        exampleDialog.show(getSupportFragmentManager(), "dialog_customnoteheader");
    }


    private void addNewNote(Notes n) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NoteDB db = NoteDB.getInstance(EditNoteActivity.this);
                db.getNoteDao().addNote(new Notes(n.getNotetitle(), n.getNotebody(), false));
            }
        }).start();
    }
}