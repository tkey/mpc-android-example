package com.example.tkey_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tkey_android.databinding.ActivityMainBinding;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;
import com.web3auth.tkey.Version;
import com.web3auth.tkey.RuntimeError;

import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class MainActivity extends AppCompatActivity {

    //    To be used for saving/reading data from shared prefs
    public final String ENCRYPTION_KEY_ALIAS = "ENCRYPTION_KEY_ALIAS";

    private AppBarConfiguration appBarConfiguration;

    public ThresholdKey appKey;
    public StorageLayer tkeyStorage;
    public ServiceProvider tkeyProvider;

    public String postboxKey;

    public KeyChainInterface keyChainInterface;

    public SharedPreferences sharedpreferences;

    private static final String PREF_FILE_NAME = "app_shared_preferences";

    public void resetState() {
        this.appKey = null;
        this.tkeyProvider = null;
        this.tkeyStorage = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.loadLibrary("tkey-native");
        String libversion;
        try {
            libversion = Version.current();
        } catch (RuntimeError e) {
            throw new RuntimeException(e);
        }

        sharedpreferences = getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);

        try {
            String encryptionBase64String = sharedpreferences.getString(ENCRYPTION_KEY_ALIAS, null);
            byte[] encryption = null;
            if(encryptionBase64String != null) {
                encryption = Base64.decode(encryptionBase64String, Base64.DEFAULT);
            }
            keyChainInterface = new KeyChainInterface(encryption);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException  e) {
            throw new RuntimeException(e);
        }



        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Native library version")
                .setMessage(libversion)
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> {
                    // Whatever...
                }).show();

        com.example.tkey_android.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}