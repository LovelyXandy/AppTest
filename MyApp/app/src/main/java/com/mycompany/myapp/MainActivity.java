package com.mycompany.myapp;

import android.app.*;
import android.widget.*;
import android.view.*;
import android.os.*;
import android.speech.tts.*;
import java.util.Locale;

public class MainActivity extends Activity 
{
	AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		builder = new AlertDialog.Builder(this);  
		final TextToSpeech talker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {public void onInit(int status) {}
		});
		final Button button = findViewById(R.id.button1);
		final EditText firstName = findViewById(R.id.editTextName);
		final EditText lastName = findViewById(R.id.editTextLastName);
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
}
