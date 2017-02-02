package com.example.alex.myphotos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private void hacerFoto(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Lanzo intent de camara
        //startActivity(intent);
        startActivityForResult(intent, 60);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 60){
            switch (resultCode)
            {
                case RESULT_OK://La foto se ha tomado bien

                    Bitmap bitmap = null;
                    if(data != null) //La foto viene en el data
                    {
                        Log.e("FOTO", "EL USUARIO HIZO LA FOTO Y VIENE EN EL DATA");
                        bitmap = (Bitmap) data.getExtras().get("data");
                    }

                    ImageView imageView = (ImageView) findViewById(R.id.imagen);
                    imageView.setImageBitmap(bitmap);
                    break;
                case RESULT_CANCELED://Se ha cancelado
                    Log.e("CANCEL", "El usuario canceló la foto");
                    break;
                default://Se ha producido un error
                    Log.e("ERROR", "Algo ha pasado");
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private void pedirPermisos(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            //Alert dialog para avisar de que se va a usar
            //el permiso due denegado en algun momento
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 5);

    }

    private boolean permisoConcedido(int[] result){

        if(result.length > 0 && result [0] == PackageManager.PERMISSION_GRANTED)//Si el array es mayor que 0 y la posicion 0 me dice que
        // ha sido garantizado me devolvera true, si no false
            return true;
        return false;

    }
    private void salir(){
        Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();

        finishAffinity();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("REQUEST", "Request code = " + requestCode);
        if(permisoConcedido(grantResults))
            //El usuario me ha dado el OK
            hacerFoto();

        else
            //El usuario no acepta
            salir();


    }

    private boolean tienePermisoCamara(){
        boolean bdev = false;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            bdev = true;
        return bdev;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(tienePermisoCamara())
            hacerFoto();
        else
            pedirPermisos();
    }
}
