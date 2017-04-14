package com.kenportal.users;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;

import com.kenportal.users.utils.IcoMoonModule;

/**
 * Created on 12/9/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //iconic font
        Iconify.with(new FontAwesomeModule())
                .with(new MaterialCommunityModule())
                .with(new MaterialModule())
                .with(new IcoMoonModule());
    }
}
