package com.edu.elon.deeznotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NoteViewActivity extends Activity {

    private EditText titleText;
    private EditText bodyText;
    private String title;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        this.setContentView(R.layout.activity_note_view);
        Intent intent = getIntent();
        titleText = (EditText) findViewById(R.id.title_text);
        bodyText = (EditText) findViewById(R.id.note_text);
        titleText.setText(intent.getStringExtra("title"));
        bodyText.setText(intent.getStringExtra("text"));

    }

    public String returnTitle(){
        return title;
    }

    public String returnText(){
        return text;
    }

    public void updateNoteClick(View view){
        title = titleText.getText().toString();
        text = bodyText.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("returntitle", title);
        editor.putString("returntext", text);
        editor.apply();
        //System.out.println("noteIntent");
        finish();

    }


}
