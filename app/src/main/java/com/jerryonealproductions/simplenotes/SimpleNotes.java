package com.jerryonealproductions.simplenotes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleNotes extends AppCompatActivity {

    TextView textPage;
    TextView textNotes;
    SimpleNotes_DB simpleNotes_db = new SimpleNotes_DB(this, null, null, 1);
    int page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        page = 4;
        textPage = (TextView) findViewById(R.id.txtPage);
        page = Integer.parseInt(simpleNotes_db.notes(0,1,""));
        textPage.setText(String.valueOf(page));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextPage(View view) {
        int lastPage = page;
        page = page + 1;
        // Set the new page
        textPage = (TextView) findViewById(R.id.txtPage);
        textPage.setText(String.valueOf(page));

        textNotes = (TextView) findViewById(R.id.txtNotes);
        String text = textNotes.getText().toString();
        String nextNotes = simpleNotes_db.notes(lastPage, page, text);

        textNotes.setText(nextNotes);
    }

    public void prevPage(View view) {
        if(page > 1) {
            int lastPage = page;
            page = page - 1;
            // Set the new page
            textPage = (TextView) findViewById(R.id.txtPage);
            textPage.setText(String.valueOf(page));
            textNotes = (TextView) findViewById(R.id.txtNotes);
            String text = textNotes.getText().toString();
            String nextNotes = simpleNotes_db.notes(lastPage, page, text);

            textNotes.setText(nextNotes);
        }
        else
            Toast.makeText(this, "There can not be less than one record.", Toast.LENGTH_SHORT).show();
    }
}
