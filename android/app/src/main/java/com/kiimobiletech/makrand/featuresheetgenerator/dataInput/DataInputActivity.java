package com.kiimobiletech.makrand.featuresheetgenerator.dataInput;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.kiimobiletech.makrand.featuresheetgenerator.Constants;
import com.kiimobiletech.makrand.featuresheetgenerator.Generator;
import com.kiimobiletech.makrand.featuresheetgenerator.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataInputActivity extends AppCompatActivity implements AgentInfoFragment.OnNextListener, PropertyInfoFragment.OnNextListener, ImagePickerFragment.OnImagePickListener{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_PLACE_HOLDER_STRING = "place_holder_string";
    private static final String TAG = "DATAINPUT";

    private TabLayout tabLayout;
    Bitmap imageBitmap;

    FloatingActionButton fab;
    DataContainer dataContainer = DataContainer.getInstance();
    Generator generator = Generator.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        assert mViewPager != null;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    generator.saveDocument();
                } catch (Generator.TemplateException e) {
                    e.printStackTrace();
                }

                Snackbar.make(view, "Will generate document in future", Snackbar.LENGTH_LONG)
                        .setAction("Dismiss", new View.OnClickListener() {
                            //TODO: Check if this actually works. If not then go for alternate implementation.
                            //currently assuming that an empty click will automatically dismiss the snackbar
                            //alternate makes final object from the snackbar and then calls the dismiss() method on it
                            //http://stackoverflow.com/questions/30729312/how-to-dismiss-a-snackbar-using-its-own-action-button
                            @Override
                            public void onClick(View v) {}
                        })
                        .show();
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

        Log.d(TAG, "Triggered");
        Log.d("requestCode", String.valueOf(requestCode));

        switch(requestCode) {
            case Constants.SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Log.d(TAG, "Gallery image selected");
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //TODO: save imgBitmap into dataContainer
                    dataContainer.tempImage = BitmapFactory.decodeStream(imageStream);
//                    ImageView img = (ImageView) findViewById(R.id.imageView);
//                    if (img != null) {
//                        img.setImageBitmap(imgBitmap);
//                    }
                }
                break;
            case Constants.REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "Camera Image selected");
                    Bundle extras = data.getExtras();

                    //TODO: save imgBitmap into dataContainer
                    dataContainer.tempImage = (Bitmap) extras.get("data");
//                    mImageView.setImageBitmap(imageBitmap);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String placeHolderString) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PLACE_HOLDER_STRING, placeHolderString);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_data_input, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getArguments().getString(ARG_PLACE_HOLDER_STRING));
            return rootView;
        }
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

            Log.d("TAB_POSITION", Integer.toString(position));

//            Fragment fragment = null;
            switch (position) {
                case 0: //agent info
                    return AgentInfoFragment.newInstance();
                case 1: //property info
                    return PropertyInfoFragment.newInstance();
//                    return PlaceholderFragment.newInstance("Property Info Fields come here");
                case 2: //Images
                    ImagePickerFragment imgFrag;
                    if(imageBitmap != null)
                        imgFrag = ImagePickerFragment.newInstance(imageBitmap);
                    else
                        imgFrag = ImagePickerFragment.newInstance();
                    return imgFrag;
//                    return PlaceholderFragment.newInstance("Image Picker will be displayed here");
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
