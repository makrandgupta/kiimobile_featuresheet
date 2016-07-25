package com.kiimobiletech.makrand.featuresheetgenerator.dataInput;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kiimobiletech.makrand.featuresheetgenerator.Constants;
import com.kiimobiletech.makrand.featuresheetgenerator.DisplayActivity;
import com.kiimobiletech.makrand.featuresheetgenerator.Generator;
import com.kiimobiletech.makrand.featuresheetgenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataInputActivity extends AppCompatActivity implements AgentInfoFragment.OnNextListener, PropertyInfoFragment.OnNextListener, ImagePickerFragment.OnImagePickListener{

    private static final String TAG = "DATAINPUT";

    private TabLayout tabLayout;
    Bitmap imageBitmap;

    Generator generator;

    FloatingActionButton fab;
    public Snackbar generateMessageSnackbar;
    DataContainer dataContainer = DataContainer.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        * Instantiate Generator and parse templateName
        * */
        try {
            generator = Generator.getInstance(getApplicationContext());
            Log.d(TAG, "Generator loaded");
        } catch (Generator.TemplateException e) {
            e.printStackTrace();
        }
        generator.parseTemplate();

        /*
        * Activity base setup
        * */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);
        setTitle(dataContainer.getTemplate());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        * Fragment loader
        * */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        assert mViewPager != null;
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(mViewPager);

        /*
        * Floating action button setup
        * */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DisplayActivity.class);
                startActivity(intent);
                generateMessageSnackbar = Snackbar.make(view, "Generating document...", Snackbar.LENGTH_INDEFINITE);
                generateMessageSnackbar.show();
            }
        });
        if (fab != null) {
            fab.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_input, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.d(TAG, "Triggered");
//        Log.d("requestCode", String.valueOf(requestCode));

        /*
        * Based on the source, handle the image
        * */
        switch(requestCode) {
            case Constants.SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Log.d(TAG, "Gallery image selected");
                    Uri selectedImage = data.getData();
                    dataContainer.tempImageURI = selectedImage;
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    dataContainer.tempImageStream = imageStream;
                    dataContainer.tempImage = BitmapFactory.decodeStream(imageStream);
                }
                break;
            case Constants.REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "Camera Image selected");
                    /*
                    * Save low-res bitmap to dataContainer
                    * */
                    dataContainer.tempImage = (Bitmap) data.getExtras().get("data");

                    /*
                    * TODO: Save full-res image to external storage
                    * */
                }
                break;
        }
    }

    @Override
    public void onNext(Integer position) {
        Log.d("CURRENT_TAB:", position.toString());
        if(position < 3) {
            //noinspection ConstantConditions
            tabLayout.getTabAt(position + 1).select();
        }
        if(dataContainer.completed())
            fab.show();
    }

    @Override
    public void onImagePick() {


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Log.d("TAB_POSITION", Integer.toString(position));

            switch (position) {
                case 0: //agent info
                    return AgentInfoFragment.newInstance();
                case 1: //property info
                    return PropertyInfoFragment.newInstance();
                case 2: //Images
                    ImagePickerFragment imgFrag;
                    if(imageBitmap != null)
                        imgFrag = ImagePickerFragment.newInstance(imageBitmap);
                    else
                        imgFrag = ImagePickerFragment.newInstance();
                    return imgFrag;
                default:
                    return PlaceholderFragment.newInstance("Bazinga!");
            }
        }


        @Override
        public int getCount() {
            return Constants.NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Agent Info";
                case 1:
                    return "Property Info";
                case 2:
                    return "Select Photos";
            }
            return null;
        }
    }
}
