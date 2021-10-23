package mm.pndaza.palitawnissaya.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mm.pndaza.palitawnissaya.R;
import mm.pndaza.palitawnissaya.utils.SharePref;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "pali_nsy.db";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        SharePref sharePref = SharePref.getInstance(this);
        if (sharePref.getNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        File file = new File(getFilesDir() + "/databases/" + DATABASE_NAME );

        if (!file.exists()) {
//            new CopyDB().execute(new File[]{file});
            new CopyDB().execute(file);
        }
         else {
            Log.d("onCreate","database exist");
            startMainActivity();
        }

    }


    private void startMainActivity(){

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            SplashScreenActivity.this.startActivity(intent);
            SplashScreenActivity.this.finish();
        },500);

    }


    public class CopyDB extends AsyncTask<File, Double, Void> {


        protected Void doInBackground(File... files) {

            File file = files[0];

            // check databases folder is exist and if not, make folder.
            if (!file.getParentFile().exists()){
                final boolean result = file.getParentFile().mkdirs();
                Log.d("folder creation result", String.valueOf(result));
            }

            try {
                InputStream input = SplashScreenActivity.this.getAssets().open("databases/" + DATABASE_NAME);
                OutputStream output = new FileOutputStream(file);

                int bufferSize;
                final int size = input.available();
                long alreadyCopy = 0;

                byte[] buffer = new byte[1024];
                while ((bufferSize = input.read(buffer) ) > 0) {
                    alreadyCopy += bufferSize;
                    output.write(buffer);
                    publishProgress(1.0d * alreadyCopy / size );
                }
                input.close();
                output.close();

                Log.i("db copy", "success");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            startMainActivity();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
    }

}
