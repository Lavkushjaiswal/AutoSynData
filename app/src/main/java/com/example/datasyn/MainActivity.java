package com.example.datasyn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

     EditText input_name,input_email, input_jonProfile;
     Spinner spinner_data;
     ImageView user_profile;
     Button submit_data;
     String[] country = {"Select Country ","India "," China" ,"US","UK"};
     String imageLink="";
     String currentCountrySelect="";
    SharedPreferences sharedPref;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input_name=findViewById(R.id.input_name);
        input_email=findViewById(R.id.input_email);
        input_jonProfile =findViewById(R.id.input_address);

        spinner_data=findViewById(R.id.spinner_data);
        user_profile=findViewById(R.id.user_profile);

        submit_data=findViewById(R.id.submit_data);

        spinner_data.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, country);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_data.setAdapter(arrayAdapter);

        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(MainActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(false)
                        .enableDebuggingMode(true)
                        .build();

            }
        });

        submit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

         db = new DatabaseHandler(this);

        sharedPref = getSharedPreferences("OfflineData", Context.MODE_PRIVATE);

        if (haveNetwork()){
            retriveSharePrefrecData();
            Toast.makeText(MainActivity.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!haveNetwork()) {
            Toast.makeText(MainActivity.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean haveNetwork(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentCountrySelect= country[position];
        if (position!=0) {
            Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (haveNetwork()){
            retriveSharePrefrecData();
            Toast.makeText(MainActivity.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!haveNetwork()) {
            Toast.makeText(MainActivity.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveNetwork()){
            retriveSharePrefrecData();
            Toast.makeText(MainActivity.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!haveNetwork()) {
            Toast.makeText(MainActivity.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private  void resetDataAfterSubmit(){
        input_name.setText("");
        input_email.setText("");
        input_jonProfile.setText("");
        currentCountrySelect="";
        imageLink="";
    }


    private void validateData(){
        String name=input_name.getText().toString().trim();
        String email=input_email.getText().toString().trim();
        String role=input_jonProfile.getText().toString().trim();
        String select_country=currentCountrySelect;
        String imageDataLink=imageLink;

        if(name.isEmpty()){
            input_name.setError("Enter the Your Name");
        }
        if(email.isEmpty()){
            input_email.setError("Enter the Your Name");
        }
        if(role.isEmpty()){
            input_jonProfile.setError("Enter the Your Name");
        }
        if(imageDataLink.isEmpty()){
//                    input_jonProfile.setError("Enter the Your Name");
                }

        if(select_country.isEmpty()){
            Toast.makeText(this, "Please Slect Country", Toast.LENGTH_SHORT).show();
        }

        if(name.isEmpty()||email.isEmpty()||role.isEmpty()||select_country.isEmpty()|| imageDataLink.isEmpty()){
            Toast.makeText(this, "Please Select all Field", Toast.LENGTH_SHORT).show();
        }else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name",name);
            editor.putString("email",email);
            editor.putString("role",role);
            editor.putString("location",select_country);
            editor.putString("imageLink",imageDataLink);
            editor.apply();
            Toast.makeText(this, "Name:-"+name+"\n email :-"+email +" \n role :- "+role +"\n ImageLink :- "+imageDataLink, Toast.LENGTH_SHORT).show();
            resetDataAfterSubmit();
            Log.d("Insert: ", "Inserting ..");

            }
    }



    private void retriveSharePrefrecData() {

        String name = sharedPref.getString("name", "");
        String email = sharedPref.getString("email", "");
        String role = sharedPref.getString("role", "");
        String select_country = sharedPref.getString("location", "");
        String imageLink = sharedPref.getString("imageLink", "");
        if (!name.isEmpty()) {
            db.addContact(new FormDataModal(name, email, role, select_country, imageLink));
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            File file= new File(mPaths.get(0));
            if(file.exists()) {
               imageLink= String.valueOf(file);
               Log.d("imageLink","ds"+imageLink);
                Uri uri = Uri.fromFile(file);
                user_profile.setImageURI(uri);
            }


        }
    }

}