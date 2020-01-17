package com.mycompany.myapp;

import android.app.*;
import android.widget.*;
import android.view.*;
import android.os.*;
import android.speech.tts.*;
import java.util.Locale;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import java.util.ArrayList;

public class MainActivity extends Activity 
{
	TextView firstName;
	TextView lastName;
	AlertDialog.Builder builder;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		builder = new AlertDialog.Builder(this);  
		final TextToSpeech talker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {public void onInit(int status) {}
		});
		final Button button = findViewById(R.id.button1);
		final Button button2 = findViewById(R.id.button2);
		firstName = findViewById(R.id.editTextName);
		lastName = findViewById(R.id.editTextLastName);
        button2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					askSpeechInput();
				}
			});
		button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					builder.setMessage(getResources().getString(R.string.hi) + firstName.getText().toString() + " " + lastName.getText());
					AlertDialog alert = builder.create();  
					//Setting the title manually  
					alert.setTitle("Special Message");
					talker.setLanguage(Locale.UK);
					talker.speak(firstName.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
					alert.show();  
				}
			});
    }
	
	// Showing google speech input dialog

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
						"Hi say something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
					if (resultCode == RESULT_OK && null != data) {

						ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
						firstName.setText(result.get(0));
					}
					break;
				}

        }
		
	}
}
