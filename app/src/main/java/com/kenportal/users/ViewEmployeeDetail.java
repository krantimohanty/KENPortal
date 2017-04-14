package com.kenportal.users;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kenportal.users.local_db.DbHelper;

public class ViewEmployeeDetail extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private ImageView mImageparallax;
    private FrameLayout mFrameParallax;
    private Toolbar mToolbar;

    private TextView mobileNo, emailId, tele_o, emp_code, emp_doj, emp_section, emp_dept, emp_ra, pre_address, pre_tel_r, pre_city, pre_state, pre_country, per_address, per_tel_r, per_city, per_state, per_country;
    private RelativeLayout mobile, call_tele_o, email;
    String i;
    ImageView prfPic;

    //Calling Local Db
    DbHelper dbHelper = new DbHelper(ViewEmployeeDetail.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee_detail);

        bindActivity();

        mToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);

        setSupportActionBar(mToolbar);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        initParallaxValues();

        //getting position
        i = getIntent().getStringExtra("id");

        //getting data from local db
//        final DbModel dbModelList = dbHelper.getEmpById(i);

        //Toolbar data
//        for (final DbModel d : dbModelList) {
        TextView main_name = (TextView) findViewById(R.id.main_textview_title);
//        main_name.setText(dbModelList.getEmp_name());
        TextView name = (TextView) findViewById(R.id.uncollapsed_title);
//        name.setText(dbModelList.getEmp_name());
        TextView desig = (TextView) findViewById(R.id.uncollapsed_design);
//        desig.setText(dbModelList.getEmp_designation());
        TextView loc = (TextView) findViewById(R.id.uncollapsed_location);
//        loc.setText(dbModelList.getOffc_loc());

        //setData
        mobileNo = (TextView) findViewById(R.id.textMobile);
//        mobileNo.setText("Mobile Number\n " + dbModelList.getMobile());

        emailId = (TextView) findViewById(R.id.textEmail);
//        emailId.setText("Email Id\n " + dbModelList.getEmail());
        tele_o = (TextView) findViewById(R.id.textTele_O);
//        tele_o.setText("Telephone(O)\n " + dbModelList.getEmp_pre_tel_o());
        emp_code = (TextView) findViewById(R.id.emp_code);
//        emp_code.setText(dbModelList.getEmp_code());
        emp_doj = (TextView) findViewById(R.id.emp_doj);
//        emp_doj.setText(dbModelList.getEmp_date_of_joining());
        emp_section = (TextView) findViewById(R.id.emp_section);
//        emp_section.setText(dbModelList.getEmp_section());
        emp_dept = (TextView) findViewById(R.id.emp_dept);
//        emp_dept.setText(dbModelList.getEmp_department());
        emp_ra = (TextView) findViewById(R.id.emp_ra);
//        emp_ra.setText(dbModelList.getRa());

        pre_address = (TextView) findViewById(R.id.pre_address);
//        pre_address.setText(Html.fromHtml(dbModelList.getEmp_pre_address()));
        pre_tel_r = (TextView) findViewById(R.id.pre_tel_r);
//        pre_tel_r.setText(dbModelList.getEmp_pre_tel_r());
        pre_city = (TextView) findViewById(R.id.pre_city);
//        pre_city.setText(dbModelList.getEmp_pre_city());
        pre_state = (TextView) findViewById(R.id.pre_state);
//        pre_state.setText(dbModelList.getEmp_pre_state());
        pre_country = (TextView) findViewById(R.id.pre_country);
//        pre_country.setText(dbModelList.getEmp_pre_country());
        per_address = (TextView) findViewById(R.id.per_address);
//        per_address.setText(Html.fromHtml(dbModelList.getEmp_per_address()));
        per_tel_r = (TextView) findViewById(R.id.per_tel_r);
//        per_tel_r.setText(dbModelList.getEmp_per_tel_r());
        per_city = (TextView) findViewById(R.id.per_city);
//        per_city.setText(dbModelList.getEmp_per_city());
        per_state = (TextView) findViewById(R.id.per_state);
//        per_state.setText(dbModelList.getEmp_per_state());
        per_country = (TextView) findViewById(R.id.per_country);
//        per_country.setText(dbModelList.getEmp_per_country());

        //call mobile
        mobile = (RelativeLayout) findViewById(R.id.mobile);
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNo.equals("")) {

                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Mobile number not available", Snackbar.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) snack.getView();
                    group.setBackgroundColor(ContextCompat.getColor(ViewEmployeeDetail.this, R.color.colorPrimary));
                    snack.setActionTextColor(Color.WHITE)
                            .show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:" + dbModelList.getMobile()));
                    startActivity(intent);
                }
            }
        });
//        String emp_img = dbModelList.getEmp_photo().replace(" ", "%20");
        prfPic = (ImageView) findViewById(R.id.prfPic);
//        Picasso.with(this)
//                .load(ServerLinks.smallPhoto + emp_img)
//                .placeholder(R.drawable.strips)
//                .error(R.drawable.strips)
//                .into(prfPic);

        //email
        email = (RelativeLayout) findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailId.equals("")) {
                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Email Id not available", Snackbar.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) snack.getView();
                    group.setBackgroundColor(ContextCompat.getColor(ViewEmployeeDetail.this, R.color.colorPrimary));
                    snack.setActionTextColor(Color.WHITE)
                            .show();
                } else {
                    Intent emailIntent = new Intent(
                            android.content.Intent.ACTION_SEND);
                    emailIntent.setAction(Intent.ACTION_SEND);
                    emailIntent.setType("message/rfc822");
//                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
//                            new String[]{dbModelList.getEmail()});
                    emailIntent.putExtra(android.content.Intent.EXTRA_CC, "");
                    emailIntent.putExtra(android.content.Intent.EXTRA_BCC, "");
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                            "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    emailIntent.setType("text/html");

// FOLLOWING STATEMENT CHECKS WHETHER THERE IS ANY APP THAT CAN HANDLE OUR EMAIL INTENT
                    startActivity(Intent.createChooser(emailIntent,
                            "Send Email Using: "));
                }
            }
        });

        //office telephone
        call_tele_o = (RelativeLayout) findViewById(R.id.tele_o);
        call_tele_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailId.equals("")) {
                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Contact number not available", Snackbar.LENGTH_LONG);
                    ViewGroup group = (ViewGroup) snack.getView();
                    group.setBackgroundColor(ContextCompat.getColor(ViewEmployeeDetail.this, R.color.colorPrimary));
                    snack.setActionTextColor(Color.WHITE)
                            .show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:" + dbModelList.getEmp_pre_tel_o()));
                    startActivity(intent);
                }
            }
        });

//        }

    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mImageparallax = (ImageView) findViewById(R.id.main_imageview_placeholder);
        mFrameParallax = (FrameLayout) findViewById(R.id.main_framelayout_title);
    }

    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mImageparallax.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFrameParallax.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mImageparallax.setLayoutParams(petDetailsLp);
        mFrameParallax.setLayoutParams(petBackgroundLp);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onBackPressed() {

        finish();

//        if (getIntent().getStringExtra("flag").equals("dash")) {
//            startActivity(new Intent(ViewEmployeeDetail.this, PortalDashboardActivity.class));
//            finish();
//        } else if (getIntent().getStringExtra("flag").equals("emp_dir")) {
//            startActivity(new Intent(ViewEmployeeDetail.this, EmployeeDirectoryActivity.class));
//            finish();
//        }
    }
}
