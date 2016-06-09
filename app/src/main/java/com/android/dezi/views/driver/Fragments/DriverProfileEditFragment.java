package com.android.dezi.views.driver.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.Permissions.PermissionsAndroid;
import com.android.dezi.R;
import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.GetProfileResponse;
import com.android.dezi.beans.UpdateDriverProfileResponse;
import com.android.dezi.customClasses.BlurBuilder;
import com.android.dezi.imageHandling.FileUtils;
import com.android.dezi.imageHandling.ImageUtils;
import com.android.dezi.imageHandling.SampledImageCallback;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.MobileConnectivity;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.driver.Activities.DriverProfileActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by  on 13/5/16.
 */
public class DriverProfileEditFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, APIResponseInterface, SampledImageCallback {
    View rootView = null;
    static final int DATE_PICKER_ID_DOB = 1111;
    static final int DATE_PICKER_ID_ANNIVERSARY = 2222;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "0EAf4fOMlWzytr6XkxWzNALgO";
    private static final String TWITTER_SECRET = "qDdfgkXzcZaFTm7Dg7knuSaIWWMifg57VXJ7OCCh16WSS6qZN5";
    Context mContext;
    CircleImageView mProfilePic;
    //min date to datepicker
    int mDobDay = 1;
    int mDobMonth = 1;
    int mDobYear = 1970;
    int mAnniDay = 1;
    int mAnniMonth = 1;
    int mAnniYear = 1970;
    ProgressBar mProgress;
    ProgressDialog mProgressDialog;
    CallbackManager callbackManager;
    //Twitter Login Button
    TwitterLoginButton twitterLoginButton;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 3333;
    private int myear;
    private int mmonth;
    private int mday;
    private ImageView mProfileBG, mFacebookBtn, mGoogleBtn, mTwitterBtn;
    private EditText mEmail,mName,mLicense,mSsn;
    private DatePickerDialog dialog;
    private TextView mDOBDatePicker,mAnniversaryDatePicker,mEditPic;
    private Button mSaveData;
    private RelativeLayout mParent;
    private RadioGroup mGender, mTransmission, mNavigation;
    private RadioButton mMale, mFemale, mTransmissionAutomatic, mTransmissionManual, mNavigationGoogleMaps, mNavigationDeziAppMaps;
    // Camera relation varialbles
    private String selectedImagePath = "";
    private boolean isProfileChanged = false;
    //    private boolean isImageUpdated = false;
    /*
    Social Info Related Variables
     */
    private String facebookID = null;

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(getActivity());
    }

    //on create view
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));
        rootView = inflater.inflate(R.layout.fragment_driver_profile_edit, container, false);
        mContext = getActivity();
        ((BaseActivity)mContext).mTitle.setText("PROFILE");
        ((BaseActivity)mContext).mTitle.setTextColor(CommonMethods.getInstance().getColor(mContext, R.color.black));
        initView();
        if(MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()){
            mParent.setEnabled(false);
            getProfile();
        }
        else{
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), rootView);
        }
        return rootView;
    }

    private void initView() {
        callbackManager = CallbackManager.Factory.create(); //For Facebook
        //For Google Signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        mParent = (RelativeLayout)rootView.findViewById(R.id.parent);
        mProgress = (ProgressBar)rootView. findViewById(R.id.progress);
        mProfileBG = (ImageView)rootView. findViewById(R.id.driver_Profile_bg);
        mProfilePic = (CircleImageView)rootView. findViewById(R.id.driver_profile);
        mFacebookBtn = (ImageView)rootView. findViewById(R.id.driver_profile_facebook);
        mGoogleBtn = (ImageView)rootView. findViewById(R.id.driver_profile_google);
        mTwitterBtn = (ImageView)rootView. findViewById(R.id.driver_profile_twitter);
        //Initializing twitter login button
        twitterLoginButton = (TwitterLoginButton)rootView. findViewById(R.id.twitterLogin);


        mSaveData= (Button) rootView.findViewById(R.id.driver_profile_save);
        mAnniversaryDatePicker = (TextView)rootView. findViewById(R.id.driver_profile_anniversary);
        mEditPic = (TextView)rootView. findViewById(R.id.driver_profilePic_edit);
        mDOBDatePicker = (TextView)rootView. findViewById(R.id.driver_profile_dob);
        mEmail = (EditText)rootView. findViewById(R.id.driver_profile_email);
        mName = (EditText)rootView. findViewById(R.id.driver_profile_name);
        mSsn= (EditText)rootView. findViewById(R.id.driver_profile_ssn);
        mLicense= (EditText)rootView. findViewById(R.id.driver_profile_license_number);
        mGender = (RadioGroup)rootView. findViewById(R.id.driver_profile_gender);
        mTransmission = (RadioGroup)rootView. findViewById(R.id.driver_profile_transmission);
        mNavigation = (RadioGroup)rootView. findViewById(R.id.driver_profile_navigation);
        mMale = (RadioButton)rootView. findViewById(R.id.driver_pofile_male_gender);
        mFemale = (RadioButton)rootView. findViewById(R.id.driver_pofile_female_gender);
        mTransmissionAutomatic = (RadioButton)rootView. findViewById(R.id.driver_profile_transmission_automatic);
        mTransmissionManual = (RadioButton)rootView. findViewById(R.id.driver_profile_transmission_manual);
        mNavigationGoogleMaps = (RadioButton)rootView. findViewById(R.id.driver_profile_navigation_googlemaps);
        mNavigationDeziAppMaps = (RadioButton)rootView. findViewById(R.id.driver_profile_navigation_dezimaps);

        // Click Llisteners
        mDOBDatePicker.setOnClickListener(this);
        mAnniversaryDatePicker.setOnClickListener(this);
        mProfilePic.setOnClickListener(this);
        mFacebookBtn.setOnClickListener(this);
        mGoogleBtn.setOnClickListener(this);
        mTwitterBtn.setOnClickListener(this);
        mEditPic.setOnClickListener(this);
        mSaveData.setOnClickListener(this);

        //Adding callback to the button
        twitterLoginButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.driver_profile_save:
                DriverVerification();
                break;
            case R.id.driver_profile_dob:
                show(DATE_PICKER_ID_DOB);
                break;
            case R.id.driver_profile_anniversary:
                show(DATE_PICKER_ID_ANNIVERSARY);
                break;
            case R.id.driver_profile_facebook:
                getFacebookInfo();
                break;
            case R.id.driver_profile_google:
                getGoogleInfo();
                break;
            case R.id.driver_profile_twitter:
                getTwitterInfo();
                break;
            case R.id.driver_profilePic_edit:
                // First check for permission then execute rest of code
                boolean isExternalStorage = PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(getActivity());
                if (!isExternalStorage) {
                    PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(getActivity());
                }
                // Show option to update profile
                ImageUtils.getInstance().openImageChooser(mContext, getActivity());
                break;
        }
    }

    public Dialog show(int id) {
        switch (id) {
            case DATE_PICKER_ID_DOB:
                dobPicker();
                return dialog;
            case DATE_PICKER_ID_ANNIVERSARY:
                anniversaryPicker();
                return dialog;
        }
        return null;
    }

    public void dobPicker() {
        if (mDOBDatePicker != null && mDOBDatePicker.getText().length() > 0) {
            String initialDOB = mDOBDatePicker.getText().toString();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = df.parse(initialDOB);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                mDobYear = (cal.get(Calendar.YEAR));
                mDobMonth = (cal.get(Calendar.MONTH));
                mDobDay = cal.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                String month = month_date.format(userAge.getTime());
                if (minAdultAge.before(userAge)) {
                    CommonMethods.getInstance().displaySnackBar(mContext, "User age must be 18+", mParent);
                } else {
                    mDOBDatePicker.setText(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).append(" "));
                }
            }
        }, mDobYear, mDobMonth, mDobDay);
        pickerDialog.getDatePicker().setMaxDate(GregorianCalendar.getInstance().getTimeInMillis());
        if (Build.VERSION.SDK_INT < 21) {
            pickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        pickerDialog.show();
    }

    private void anniversaryPicker() {
        if (mAnniversaryDatePicker != null && mAnniversaryDatePicker.getText().length() > 0) {
            String initialDOB = mAnniversaryDatePicker.getText().toString();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = df.parse(initialDOB);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                mAnniYear = (cal.get(Calendar.YEAR));
                mAnniMonth = (cal.get(Calendar.MONTH));
                mAnniDay = cal.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                String month = month_date.format(userAge.getTime());
                mAnniversaryDatePicker.setText(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).append(" "));
            }
        }, mAnniYear, mAnniMonth, mAnniDay);
        pickerDialog.getDatePicker().setMaxDate(GregorianCalendar.getInstance().getTimeInMillis());
        if (Build.VERSION.SDK_INT < 21) {
            pickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        pickerDialog.show();
    }

    /*
    Let's get Social
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            /*
            For Image Handeling
             */
            if (requestCode == Crop.REQUEST_CROP) {
                ImageUtils.getInstance().resampleImage(getActivity(),ImageUtils.getInstance().getCroppedImagePath(getActivity()));
            } else if (requestCode == ImageUtils.IMAGE_CHOOSER_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null || data.getData() == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {
                    selectedImagePath = ImageUtils.getInstance().getImagePath(getActivity());
                } else {
                    selectedImagePath = FileUtils.getPath(getActivity(), data.getData());
                }

                if ((selectedImagePath != null) && !(selectedImagePath.equals(""))) {
                    // selectedImagePath is the path of selected or captured image.
                    // if you want cropping
//                    CommonMethods.getInstance().DisplayToast(mContext,selectedImagePath+"");
                    ImageUtils.getInstance().beginCrop(getActivity(),getActivity(), selectedImagePath, false);

                    // else if you want cropping as square
                    // ImageUtils.getInstance().beginCrop(this, this, selectedImagePath, true);

                    // else
                    // ImageUtils.getInstance().resampleImage(this, this, selectedImagePath);
                }
            }
            /*
            For Social Sharing
             */
            else if (requestCode == RC_SIGN_IN) {
                // Result from Google
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } else {
                // Result from Facebook
                callbackManager.onActivityResult(requestCode, resultCode, data);

                //Adding the login result back to the button
                // handle cancelled Twitter login (resets TwitterCore.*AuthHandler.AuthState)
                final TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
                if (twitterAuthClient.getRequestCode() == requestCode) {
                    twitterAuthClient.onActivityResult(requestCode, resultCode, data);
                }
//                twitterLoginButton.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            // Error Occured, or user cancelled
        }
    }

    private void getFacebookInfo() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_about_me", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                try {
                    Profile profile = Profile.getCurrentProfile();
                    CommonMethods.getInstance().e("", "FACEBOOK" + loginResult.getRecentlyGrantedPermissions());
                    CommonMethods.getInstance().e("", "FACEBOOK USER ID: " + loginResult.getAccessToken().getUserId());
                    String name = profile.getName();
                    String profilePic = profile.getProfilePictureUri(300, 300).toString();
                    CommonMethods.getInstance().e("", "USER NAME " + name);
                    CommonMethods.getInstance().e("", "USER PROFILEPIC " + profilePic);
                    /*
                    Update UI
                     */
                    updateHeader(profilePic, false);
                    mName.setText(name);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                // App code
                CommonMethods.getInstance().DisplayToast(mContext, "User cancelled the request");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                CommonMethods.getInstance().DisplayToast(mContext, "Error Occured while Facebook Login");
            }
        });
    }

    private void getGoogleInfo() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        CommonMethods.getInstance().d("GOOGLE SIGNIN", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            // Update UI
            try {
                mName.setText(acct.getDisplayName());
                updateHeader(acct.getPhotoUrl().toString(), false);
                // save profile on device for future use
//                saveImageonDevice(acct.getPhotoUrl().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void getTwitterInfo() {
        twitterLoginButton.callOnClick();
    }

    //The login function accepting the result object
    public void login(Result<TwitterSession> result) {

        //Creating a twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();

        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new com.twitter.sdk.android.core.Callback<User>() {
                    @Override
                    public void failure(TwitterException e) {
                        //If any error occurs handle it here
                        e.printStackTrace();
                    }

                    @Override
                    public void success(Result<User> userResult) {
                        //If it succeeds creating a User object from userResult.data
                        User user = userResult.data;

                        //Getting the profile image url
                        String profileImage = user.profileImageUrl.replace("_normal", "");
                        updateHeader(profileImage, false);
                    }
                });

        // Set Information
        mName.setText(username);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        connectionResult.toString();
    }

    /*
    Get Profile
     */
    private void getProfile() {
        String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
        if (userID == null || userID.equalsIgnoreCase("")) {
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
            if (mProgress != null) mProgress.setVisibility(View.GONE);
            mParent.setEnabled(true);

            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
        param.put("user_id", userID); //User ID
        CommonMethods.getInstance().e("GET PROFILE PARAMS", "Param-> " + param.toString());
        APIHandler.getInstance(ConstantFile.APIURL).getProfile(param, this);
    }

    /*
    Verify Driver Profile before updating
     */
    private void DriverVerification() {
        if (TextUtils.isEmpty(mName.getText().toString())) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.validation_name), mParent);
            mName.requestFocus();
        } else if (TextUtils.isEmpty(mEmail.getText().toString())) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.validation_email), mParent);
            mEmail.requestFocus();
        } else if (!CommonMethods.getInstance().validateEmail(mEmail.getText().toString())) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.validation_valid_email), mParent);
            mEmail.requestFocus();
        }  else if (TextUtils.isEmpty(mDOBDatePicker.getText().toString())) {
            CommonMethods.getInstance().displaySnackBar(mContext, "Please fill Date of Birth", mParent);
            mDOBDatePicker.requestFocus();
        }
        else if (TextUtils.isEmpty(mAnniversaryDatePicker.getText().toString())) {
            CommonMethods.getInstance().displaySnackBar(mContext, "Please fill Date of Anniversary", mParent);
            mAnniversaryDatePicker.requestFocus();
        }
        else if (TextUtils.isEmpty(mLicense.getText().toString())) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.validation_license), mParent);
            mLicense.requestFocus();
        }else if (TextUtils.isEmpty(mSsn.getText().toString())) {
                CommonMethods.getInstance().displaySnackBar(mContext, "Please fill SSN", mParent);
            mSsn.requestFocus();
            }
        else {
            String transmission = "", navigation = "", gender = "";
            //get Which transmission is selected
            switch (mTransmission.getCheckedRadioButtonId()) {
                case R.id.driver_profile_transmission_automatic:
                    transmission = "automatic";
                    break;
                case R.id.driver_profile_transmission_manual:
                    transmission = "manual";
                    break;
            }
            //get Which Navigation is selected
            switch (mNavigation.getCheckedRadioButtonId()) {
                case R.id.driver_profile_navigation_googlemaps:
                    navigation = "google_map";
                    break;
                case R.id.driver_profile_navigation_dezimaps:
                    navigation = "dezi_map";
                    break;
            }
            // Get Which Gender is selected
            switch (mGender.getCheckedRadioButtonId()) {
                case R.id.driver_pofile_male_gender:
                    gender = "male";
                    break;
                case R.id.driver_pofile_female_gender:
                    gender = "female";
                    break;
            }
            String terms = "1";
            // Check Profile Pic is attachd or not ?
            String imagePath = "";

            // get Image path from storage
            File croppedFile = new File(Environment.getExternalStorageDirectory()
                    + "/" + mContext.getResources().getString(R.string.app_name) + "/croppedImage.png");
            if (croppedFile.isFile() && isProfileChanged) {
                imagePath = croppedFile.getAbsolutePath();
            } else {
                imagePath = "";
            }

//            CommonMethods.getInstance().DisplayToast(mContext,imagePath);
            CommonMethods.getInstance().e("", "File Path-> " + imagePath);
            if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()) {
                updateProfile(imagePath, mName.getText().toString(), mEmail.getText().toString(),
                        mDOBDatePicker.getText().toString(), gender, mAnniversaryDatePicker.getText().toString(), transmission, navigation, mLicense.getText().toString(), mSsn.getText().toString());
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), mParent);
            }
        }
    }

    /*
    Update Profile Method
     */
    private void updateProfile(String profile_pic, String name, String email,  String dob, String gender,
                               String anniversary, String transmission, String navigationSystem,String license,String ssn) {
        //
        String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
        if (userID == null || userID.equalsIgnoreCase("")) {
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
            return;
        }
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.show();

        Map<String, String> param = new HashMap<>();
        param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
        param.put("email", email);
        param.put("user_id", userID);
        param.put("name", name);
        param.put("dob", dob);
        param.put("gender", gender);
        param.put("anniversary", anniversary);
        param.put("transmission", transmission);
        param.put("navigation", navigationSystem);
        param.put("license",license );
        param.put("ssn", ssn);

//        param.put("profile_pic", profile_pic);   // Profile Pic
        param.put("remove_profile_pic", "0");
        CommonMethods.getInstance().e("UPDATE PROFILE PARAMS", "Param-> " + param.toString());
        APIHandler.getInstance(ConstantFile.APIURL).updateDriverProfile(param, profile_pic, this);
    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        try {
            if (mProgress != null) mProgress.setVisibility(View.GONE);
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
            }
            mParent.setEnabled(true);
            if (tag.equalsIgnoreCase("get_profile")) {
                if (response.isSuccessful()) {
                    GetProfileResponse obj = (GetProfileResponse) response.body();
                    if (obj.getStatus() == 0) {
                        CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                    } else if (obj.getStatus() == 1) {
                        // API Successfull
                        updateUI(obj);
                    }
                    else if(obj.getStatus() == 3){
                        CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                        ((BaseActivity)mContext).logout();
                    }
                } else {
                    CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), mParent);
                }
            } else if (tag.equalsIgnoreCase("update_driver_profile")) {
                if (response.isSuccessful()) {
                    UpdateDriverProfileResponse obj = (UpdateDriverProfileResponse) response.body();
                    if (obj.getStatus() == 1) {
                        CommonMethods.getInstance().displaySnackBar(mContext, "Profile Updated Succcessfully", mParent);
                    } else {
                        CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage(), mParent);
                    }
                } else {
                    CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), mParent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        try {
            if (mProgress != null) mProgress.setVisibility(View.GONE);
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
            }
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), mParent);
            mParent.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    Updaet UI
     */
    private void updateUI(GetProfileResponse obj) {
        //update Profile Pic
        if (!obj.getData().getProfile_pic().equalsIgnoreCase("")) {
            updateHeader(ConstantFile.PROFILE_BASE + obj.getData().getProfile_pic(), false);
        } else {
            // No Picture from server
        }
        mName.setText(obj.getData().getFirst_name());
        mEmail.setText(obj.getData().getEmail());
        if (obj.getData().getEmail().equalsIgnoreCase("")) {
            mEmail.setEnabled(true);
        } else {
            mEmail.setEnabled(false);
        }
        if (obj.getData().getDob().equalsIgnoreCase("0000-00-00")) {

        } else {
            mDOBDatePicker.setText(obj.getData().getDob());
        }

        if (obj.getData().getGender().equalsIgnoreCase("male")) {
            mMale.setChecked(true);
            mFemale.setChecked(false);
        } else if (obj.getData().getGender().equalsIgnoreCase("female")) {
            mMale.setChecked(false);
            mFemale.setChecked(true);
        }
        if (obj.getData().getAnniversary().equalsIgnoreCase("0000-00-00")) {

        } else {
            mAnniversaryDatePicker.setText(obj.getData().getAnniversary());
        }

        // get Transmission
        if (obj.getData().getTransmission().equalsIgnoreCase("automatic")) {
            mTransmissionAutomatic.setChecked(true);
            mTransmissionManual.setChecked(false);
        } else if (obj.getData().getTransmission().equalsIgnoreCase("manual")) {
            mTransmissionAutomatic.setChecked(false);
            mTransmissionManual.setChecked(true);
        }
        //get Preffered navigation system
        if (obj.getData().getNavigation_system().equalsIgnoreCase("google_map")) {
            mNavigationGoogleMaps.setChecked(true);
            mNavigationDeziAppMaps.setChecked(false);
        }  else if (obj.getData().getTransmission().equalsIgnoreCase("dezi_map")) {
            mNavigationGoogleMaps.setChecked(false);
            mNavigationDeziAppMaps.setChecked(true);
        }
        // get terms & consitions
    }

    /*
   update header
    */
    private void updateHeader(String profilepic, boolean isFile) {
        // First check for permission then execute rest of code
        boolean isExternalStorage = PermissionsAndroid.getInstance().checkWriteExternalStoragePermission(getActivity());
        if (!isExternalStorage) {
            PermissionsAndroid.getInstance().requestForWriteExternalStoragePermission(getActivity());
        }

        try {
            if (isFile) {
                Uri uri = Uri.fromFile(new File(profilepic));
                Picasso.with(mContext).load(new File(profilepic))
                        .resize(350, 350).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(mProfilePic, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Update Avatar Background
                        Bitmap bp = ((BitmapDrawable) mProfilePic.getDrawable()).getBitmap();
                        if (bp != null && bp.getWidth() > 0) {
                            Bitmap image = BlurBuilder.blur(mContext, bp);
                            mProfileBG.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), image));
                        }
                        isProfileChanged = true;
                    }

                    @Override
                    public void onError() {
                        CommonMethods.getInstance().e("", "Profile Pic couldn't downloaded");
                    }
                });
            } else {
                Picasso.with(mContext).load(profilepic)
                        .resize(350, 350).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(mProfilePic, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Update Avatar Background
                        Bitmap bp = ((BitmapDrawable) mProfilePic.getDrawable()).getBitmap();
                        if (bp != null && bp.getWidth() > 0) {
                            Bitmap image = BlurBuilder.blur(mContext, bp);
                            mProfileBG.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), image));

                            saveImageonDevice(bp);  // Download image on storage
                        }
                        isProfileChanged = true;
                    }

                    @Override
                    public void onError() {
                        CommonMethods.getInstance().e("", "Profile Pic couldn't downloaded");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    Save Image on Device storage
     */
    private void saveImageonDevice(Bitmap finalBitmap) {
        // Download image to sd card location for future use
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + mContext.getResources().getString(R.string.app_name));
        if (!myDir.isDirectory())
            myDir.mkdirs(); // Make Directory if not already made
        String fname = "croppedImage.png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSampledImage(String base64, String path) {
        try {
            CommonMethods.getInstance().e("", "Imagepath After Sampling-> " + path);
            updateHeader(path, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
