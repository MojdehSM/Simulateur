package com.intactile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class SimulatorService extends Service {

	static long currentIdentifier = new Random().nextLong();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationListener mlocListener = new LocationListener() {

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub

			}

			
			@Override
			public void onLocationChanged(Location loc) {

				String Text = "My current location is: " + "Latitud = "
						+ loc.getLatitude() + " Longitud = "
						+ loc.getLongitude();

				Log.e("HERE ", Text);
				
				WriteInFile(loc);
			}
		};

		if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					2000, 2, mlocListener);
		} else {
			mlocManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 2000, 2, mlocListener);
		}

		return START_STICKY;
	}

	void WriteInFile(Location loc) {
		String str = currentIdentifier +":"+loc.getTime() + ":" + loc.getLatitude() + ":" 
		+ loc.getLongitude() +":" +loc.getBearing()+":"+loc.getSpeed()+":"+loc.getAccuracy()+
				"\n";
		
		try {
			FileOutputStream fOut = openFileOutput("location.csv",
					Context.MODE_APPEND);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			osw.write(str);
			osw.flush();
			osw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
