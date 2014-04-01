package com.intactile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MaritimeActivity extends Activity implements OnClickListener {

	Button bt;
	Button bt2;
	Button bt3;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maritime);

		bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(this);

		bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(this);

		bt3 = (Button) findViewById(R.id.button3);
		bt3.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maritime, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == bt) {
			Intent serviceGeoSender = new Intent(this, SimulatorService.class);
			startService(serviceGeoSender);
		} else if (arg0 == bt3) {
			SimulatorService.currentIdentifier = new Random().nextLong();
			
			Toast.makeText(this, "New Identifier created "+SimulatorService.currentIdentifier, Toast.LENGTH_LONG).show();
			
		} else {
			try {
				FileInputStream fIn = openFileInput("location.csv");

				String line;
				StringBuffer sb = new StringBuffer();

				BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));

				while ((line = reader.readLine()) != null) {
					Log.e("TEST", line);
					sb.append(line + "\n");
				}

				fIn.close();

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[] { "mohamadi.mojde@yahoo.com" });
				email.putExtra(Intent.EXTRA_SUBJECT, "Maritime Simulation");
				email.putExtra(Intent.EXTRA_TEXT, sb.toString());
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Choisissez votre Client Email  :"));

				deleteFile("location.csv");
				FileOutputStream fOut2 = openFileOutput("location.csv",MODE_APPEND);
				fOut2.close();
				
				String  name = Calendar.getInstance().getTime().toString()+".csv";
				FileOutputStream fOut = openFileOutput(name,MODE_APPEND);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				osw.write(sb.toString());
				fOut.flush();
				fOut.close();
				
				for(File ctx : getFilesDir().listFiles()){
					Log.e("FILE", ctx.getName());
				}

				Toast.makeText(this, "Data Saved "+SimulatorService.currentIdentifier, Toast.LENGTH_LONG).show();		
				
			} catch (IOException ex) {
				Log.e("ERROR",ex.getMessage());
				ex.printStackTrace();
			}

		}
	}

}
