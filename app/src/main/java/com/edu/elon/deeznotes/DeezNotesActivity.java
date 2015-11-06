package com.edu.elon.deeznotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DeezNotesActivity extends Activity {

    private TextView resultText;
    private Notes notes;
    Context context;
    GameLoopView glv;
    public final static int STRING_DIALOG = 1;

    //private GameLoopView boots = new GameLoopView(context); //AttributeSet attrs);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deez_notes);

        glv = (GameLoopView) findViewById(R.id.view);
        notes = new Notes();
        glv.setNotes(notes.noteListGetter());
        //view.setNotes(notes);
        context = getApplicationContext();

    }

    public void onClick(View view) {
        //showInputDialog();
        notes.notesArray.add(new Note(context));

    }

    public void onResume(){
       super.onResume();
        if (glv.startActivity) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String returnTitle = prefs.getString("returntitle", null);
            String returnText = prefs.getString("returntext", null);
            glv.setText(returnText);
            glv.setTitle(returnTitle);
        }
    }

    public void onActivityResult(){
            Intent intent = getIntent();
            glv.setText(intent.getStringExtra("returntext"));
            glv.setTitle(intent.getStringExtra("returntitle"));

    }

    /*public void giveStrings() {
        if (glv.startActivity) {
            intent = new Intent(context, NoteViewActivity.class);
            intent.putExtra("text", glv.getText());
            intent.putExtra("title", glv.getTitle());
            startActivityForResult(intent, STRING_DIALOG);
        }
        glv.startActivity = false;
    }*/

    /*public void inputChecker() {
                Intent intent = new Intent(this, NoteViewActivity.class);
                intent.putExtra("strings", glv.getStrings());
                startActivity(intent);
    }*/


}
