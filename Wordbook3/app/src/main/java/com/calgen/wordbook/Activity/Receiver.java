package com.calgen.wordbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.calgen.wordbook.R;

/**
 * Created by Gurupad on 01-Aug-15.
 */
public class Receiver extends Activity {
    public EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the intent that started this activity
        setContentView(R.layout.new_word);
        et=(EditText)findViewById(R.id.etNewWord);
        Toast.makeText(this,"Started",Toast.LENGTH_SHORT);
        Intent intent = getIntent();
        //Get the action of the intent
        String action = intent.getAction();
        //Get the type of intent
        String type = intent.getType();
        Toast.makeText(this, "Created", Toast.LENGTH_LONG);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type))
                handleSentText(intent);
            else
                Toast.makeText(this, "Incorrect format", Toast.LENGTH_LONG);

        }
        else
            Toast.makeText(this,"Incorrect",Toast.LENGTH_SHORT);
    }



    private void handleSentText(Intent intent) {
        Toast.makeText(getApplicationContext(), "Word" + intent.getStringExtra(Intent.EXTRA_TEXT) + "added", Toast.LENGTH_SHORT);
        et.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
    }
}
