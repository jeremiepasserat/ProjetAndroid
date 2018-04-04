 package com.example.chrx.projetandroid;

        import android.Manifest;
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.location.LocationManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;

        import com.google.android.gms.common.api.GoogleApiClient;
      //  import com.google.android.gms.location.LocationListener;
      //  import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

        import android.os.Build.VERSION;
        import android.os.StrictMode;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;


 public class Navigation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker oldmarker;
    private String arretnotifie = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mapFragment.getMapAsync(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1
            );

        }







        Button button = findViewById(R.id.accueil);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Navigation.this, Connexion.class);
                startActivity(intent);

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public static ArrayList<String> getCoordonnees() {
        String arret_url = "http://ptutequipe2g1.alwaysdata.net/coord_arret.php";
        ArrayList<String> resultats = new ArrayList<>();


        try {
            URL url = new URL(arret_url);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("POST");
            huc.setDoInput(true);
            huc.setDoOutput(true);
            InputStream inputStream = huc.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultats.add(line);
            }

            bufferedReader.close();
            inputStream.close();
            huc.disconnect();


        } catch (MalformedURLException e) {
            System.out.println("Malformed URL Exception");
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return resultats;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        final ArrayList<String> latitudes = getLatitude(getCoordonnees());
        final ArrayList<String> longitudes = getLongitude(getCoordonnees());
        final ArrayList<String> nomArrets = getNomArret(getCoordonnees());



        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(43.514591, 5.451379);
        mMap.addMarker(new MarkerOptions().position(sydney).title("IUT Aix").snippet("Là où les rêves commencent").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

        // Ajoute les coordonnées des arrêts à la carte
        for (int i = 0; i < latitudes.size(); ++i) {
            LatLng latlng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
            mMap.addMarker(new MarkerOptions().position(latlng).title(nomArrets.get(i)));
        }





    /*
        // demande à l'utilisateur les droits pour la géolocalisation.
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1
            );
        }*/

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // récupère la dernière position enrengistrée du GPS pour l'afficher sur la carte
        try {
            Location location = new Location(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            //oldmarker = mMap.addMarker(new MarkerOptions().position(position).title("Vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
            mMap.setMyLocationEnabled(true);
            getArretAMoinsdeDixMetres(position, latitudes, longitudes, nomArrets);
            /* if (getArretAMoinsdeDixMetres(position) != "")
            {
                createNotification("Tu as un nouveau message !!");
            }*/

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        }




        //mise à jour de la position de manière périodique
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 1, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                   // oldmarker.remove();
                   // oldmarker = mMap.addMarker(new MarkerOptions().position(position).title("Vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                    getArretAMoinsdeDixMetres(position, latitudes, longitudes, nomArrets);



                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }catch (SecurityException e){
            e.printStackTrace();
        }



    }
    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER)


    // A voir si la création d'une classe Arrêt (latitude, longitude, nom) peut s'imopser

    public ArrayList<String> getLatitude(ArrayList<String> coordonnees) {
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();

        //Traitement pour enlever les noms d'arrêt des Strings

        for (String s : coordonnees)
        {
            int i = 0;
            while (s.charAt(i) != '*') {
                ++i;
            }
            tmp.add(s.substring(i+2));
        }

        // Isolement des latitudes grâce à l'ArrayList Retraitée
        for (String s : tmp) {
            int i = 0;
            while (s.charAt(i) != ' ') {
                ++i;
            }
            latitudes.add(s.substring(0, i - 1));

        }


        return latitudes;
    }

    public ArrayList<String> getLongitude(ArrayList<String> coordonnees) {
        ArrayList<String> longitudes = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();


        //Traitement pour enlever les noms d'arrêt des Strings

        for (String s : coordonnees)
        {
            int i = 0;
            while (s.charAt(i) != '*') {
                ++i;
            }
            tmp.add(s.substring(i+2));
        }

        // Isolement des latitudes grâce à l'ArrayList Retraitée

        for (String s : tmp) {
            int i = 0;
            while (s.charAt(i) != ' ') {
                ++i;
            }
            longitudes.add(s.substring(i + 1));
        }
        return longitudes;
    }

    public ArrayList<String> getNomArret (ArrayList<String> coordonnees)
    {
        ArrayList<String> arrets = new ArrayList<>();

        for (String s : coordonnees) {
            int i = 0;
            while (s.charAt(i) != '*') {
                ++i;
            }
            arrets.add(s.substring(0, i - 1));
        }
        return arrets;
    }


     private void createNotification(String message){
         final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         final Intent intentNotification = new Intent(this, Connexion.class);
         final PendingIntent pendingIntent = PendingIntent.getActivity(this,1 , intentNotification,
                 PendingIntent.FLAG_ONE_SHOT);

         Notification.Builder builder = new Notification.Builder(this)
                 .setWhen(System.currentTimeMillis())
                 .setTicker("U just got a notif braw")
                 .setSmallIcon(R.mipmap.ic_launcher_round)
                 .setContentTitle(getResources().getString(R.string.notification_title))
                 .setContentText(message)
                 .setContentIntent(pendingIntent)
                 .setVibrate(new long[] {0,200,150,200});

         notificationManager.notify(1, builder.build());
     }
    public void getArretAMoinsdeDixMetres (LatLng position, ArrayList<String> latitudes, ArrayList<String> longitudes, ArrayList<String> nomArrets)
    {
        String arretsadistance = "";


        float [] results = new float[2];


        //Toast.makeText(Navigation.this,Double.toString(position.latitude),Toast.LENGTH_SHORT).show();
        for (int i = 0; i<latitudes.size(); ++i)
        {
            Location.distanceBetween(position.latitude, position.longitude, Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)), results );
            //Toast.makeText(Navigation.this, Float.toString(results[0]),Toast.LENGTH_SHORT).show();

            if (results[0] < 100 && !arretnotifie.equals(nomArrets.get(i)))
            {

                arretnotifie = nomArrets.get(i);
                createNotification(arretnotifie + " à moins de 100 mètres");
                //double lat_encours = Double.parseDouble(latitudes.get(i));
                //double long_encours = Double.parseDouble(longitudes.get(i));
                break;
            }
        }

        //Toast.makeText(Navigation.this, arretnotifie, Toast.LENGTH_SHORT).show();
    }


}
