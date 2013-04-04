package com.nmolina.learning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	}
	
	public void goToEcuaciones(View view){
		Intent i = new Intent(this, EcuacionExactaActivity.class );
        startActivity(i);
	}
	
	public void goToAbout(View view){
		Intent i = new Intent(this, About.class );
        startActivity(i);
	}
}
