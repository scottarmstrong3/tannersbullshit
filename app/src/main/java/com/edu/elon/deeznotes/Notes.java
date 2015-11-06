package com.edu.elon.deeznotes;

import java.util.ArrayList;

/**
 * Created by scottarmstrong on 10/29/15.
 */
public class Notes {

    ArrayList<Note> notesArray = new ArrayList<Note>();

    //public Notes() {

        //loadNotesFromCloud();

    //}

    public ArrayList<Note> noteListGetter() {
        return notesArray;
    }

    private void loadNotesFromCloud() {

    }
}
