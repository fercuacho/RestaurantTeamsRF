Objective of the app.

Manifesto Manager is a tool that will help you create schedules for your work group in a quick and easy way. Although it was designed mainly for restaurants, it can really be used for any work group that requires making a schedule with flexible editing options.

Logo description and meaning

The Manifesto Manager logo is an M for the words "Manifesto" and "Manager". Likewise, the M resembles the silhouette of a house, and represents the spirit of camaraderie that is experienced in work groups.

Device type, operating system version and supported orientations.

For MM, it was decided that the best mobile platform would be a smartphone and not a tablet, laptop or computer because the smartphone is practical and is essential at work. It supports Android 6 Marshmellow versions and later, allowing us to reach 98.3% coverage of Android mobile devices. Because no feature requires horizontal orientation, the only orientation supported is vertical.

Credentials to test the app (not necessary)

It is possible to create a new account to try the functionality of the app. However I have provided the next credentials, in case you want to see an example of an optimal usage.

Email: fernando@jose.com
Password: 1234567

Project dependencies (packages and/or frameworks used)

 //Para Navigation Component
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    def room_version = "2.5.2"

    //Para retrofit y Gson
    implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-gson:$retrofitVersion"

    //Adicional para el interceptor
    implementation "com.squareup.okhttp3:logging-interceptor:4.10.0"

    //Glide y Picasso
    implementation "com.github.bumptech.glide:glide:$glideVersion"
    implementation "com.squareup.picasso:picasso:$picassoVersion"

    //Para las corrutinas
    implementation  'androidx.lifecycle:lifecycle-runtime-ktx:2.6.1'

    //Imágenes con bordes redondeados
    implementation 'com.makeramen:roundedimageview:2.3.0'

    //Shared preferences encriptadas
    implementation 'androidx.security:security-crypto:1.1.0-alpha06'

    //Google firebase
    implementation 'com.google.firebase:firebase-auth:21.1.0'

    //Google Maps
    implementation 'com.google.android.gms:play-services-maps:18.2.0'

    //Material Desing
    implementation 'com.google.android.material:material:1.10.0'

    //Room
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    //Para el calendario
    implementation 'com.applandeo:material-calendar-view:1.9.0-rc04'
    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.0.4'
    implementation 'com.kizitonwose.calendar:view:2.4.0'

    //Calendario-observer
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2"

    //timepicker
    implementation 'com.github.kshitijskumar:Ticker:1.0.0'

    //NumberPicker
    implementation 'com.github.kshitijskumar:Ticker:1.0.0'

    //ColorPicker
    implementation 'com.github.Dhaval2404:ColorPicker:2.3'  
