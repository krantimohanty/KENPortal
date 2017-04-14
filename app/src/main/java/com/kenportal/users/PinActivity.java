package com.kenportal.users;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.kenportal.users.utils.CustomPreference;

import java.io.File;
import java.util.ArrayList;

public class PinActivity extends Activity {

    String[] keypad = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "CLR", "0", "DEL"};
    GridView gridView;
    TextView txt1, txt2, txt3, txt4;
    ArrayList<String> values = new ArrayList<>();
    AppCompatTextView pin_status;
    AppCompatTextView forget_pin;
    String s, s1;
    int flg = 0;
    String savedPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_view);
        //getting the saved pin
        savedPin = CustomPreference.with(PinActivity.this).getString("pin", "");
        pin_status = (AppCompatTextView) findViewById(R.id.pin_status);
        forget_pin = (AppCompatTextView) findViewById(R.id.forget_pin);

        if (savedPin.equals("")) {
            pin_status.setText("Setup your pin");
        } else {
            pin_status.setText("Enter your pin");
        }
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);

        gridView = (GridView) findViewById(R.id.gridview);
        CustomGridAdapter customGridAdapter = new CustomGridAdapter();
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                AppCompatTextView t = (AppCompatTextView) view.findViewById(R.id.pin_key);
                                                // Toast.makeText(getApplicationContext(), t.getText().toString(), Toast.LENGTH_SHORT).show();
                                                if (t.getText().toString().equals("DEL")) {
                                                    values.clear();
                                                    txt1.setBackgroundResource(R.drawable.gray_dot);
                                                    txt2.setBackgroundResource(R.drawable.gray_dot);
                                                    txt3.setBackgroundResource(R.drawable.gray_dot);
                                                    txt4.setBackgroundResource(R.drawable.gray_dot);
                                                } else if (getIntent().getStringExtra("chng_flag").equals("new")) {
                                                    if (savedPin.equals("")) {
                                                        if (flg == 0) {
                                                            pin_status.setText("Setup your pin");
                                                        } else {
                                                            pin_status.setText("Re-enter your pin");
                                                        }

                                                        values.add(t.getText().toString());

                                                        if (values.size() == 1) {
                                                            txt1.setBackgroundResource(R.drawable.blue_dot);
                                                        } else if (values.size() == 2) {
                                                            txt2.setBackgroundResource(R.drawable.blue_dot);
                                                        } else if (values.size() == 3) {
                                                            txt3.setBackgroundResource(R.drawable.blue_dot);
                                                        } else if (values.size() == 4) {
                                                            txt4.setBackgroundResource(R.drawable.blue_dot);
                                                            if (flg == 0) {
                                                                s = values.get(0) + values.get(1) + values.get(2)
                                                                        + values.get(3);
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        values.clear();
                                                                        txt1.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt2.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt3.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt4.setBackgroundResource(R.drawable.gray_dot);
                                                                        flg = 1;
                                                                    }
                                                                }, 100);
                                                                pin_status.setText("Re-enter your pin");
                                                            } else {
                                                                s1 = values.get(0) + values.get(1) + values.get(2)
                                                                        + values.get(3);

                                                                if (s.equals(s1)) {
                                                                    //Toast.makeText(PinActivity.this, s + "---" + s1 + "Success", Toast.LENGTH_SHORT).show();
                                                                    CustomPreference.with(PinActivity.this).save("pin", s);
                                                                    startActivity(new Intent(PinActivity.this, PortalDashboardActivity.class));
                                                                    finish();
                                                                } else {
                                                                    pin_status.setText("Pincode mismatch.Try again!!");
                                                                    values.clear();
                                                                    new Handler().postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            txt1.setBackgroundResource(R.drawable.gray_dot);
                                                                            txt2.setBackgroundResource(R.drawable.gray_dot);
                                                                            txt3.setBackgroundResource(R.drawable.gray_dot);
                                                                            txt4.setBackgroundResource(R.drawable.gray_dot);
                                                                        }
                                                                    }, 100);
                                                                }
                                                            }

                                                        }
                                                    } else {
                                                        pin_status.setText("Enter your pin");
                                                        values.add(t.getText().toString());

                                                        if (values.size() == 1) {
                                                            txt1.setBackgroundResource(R.drawable.blue_dot);
                                                        } else if (values.size() == 2) {
                                                            txt2.setBackgroundResource(R.drawable.blue_dot);
                                                        } else if (values.size() == 3) {
                                                            txt3.setBackgroundResource(R.drawable.blue_dot);
                                                        } else if (values.size() == 4) {
                                                            txt4.setBackgroundResource(R.drawable.blue_dot);

                                                            s = values.get(0) + values.get(1) + values.get(2)
                                                                    + values.get(3);
                                                            values.clear();
                                                            if (savedPin.equals(s)) {
                                                                //Toast.makeText(PinActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                                                new Handler().postDelayed(new Runnable() {
//                                                                    @Override
//                                                                    public void run() {
//                                                                        txt1.setBackgroundResource(R.drawable.gray_dot);
//                                                                        txt2.setBackgroundResource(R.drawable.gray_dot);
//                                                                        txt3.setBackgroundResource(R.drawable.gray_dot);
//                                                                        txt4.setBackgroundResource(R.drawable.gray_dot);
                                                                startActivity(new Intent(PinActivity.this, PortalDashboardActivity.class));
                                                                finish();
//                                                                    }
//                                                                }, 200);

                                                            } else {
                                                                pin_status.setText("Invalid pin code");
                                                                values.clear();
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        txt1.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt2.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt3.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt4.setBackgroundResource(R.drawable.gray_dot);
                                                                    }
                                                                }, 100);
                                                            }

                                                        }

                                                    }
                                                } else if (getIntent().getStringExtra("chng_flag").equals("change")) {
                                                    pin_status.setText("Setup your pin");
                                                    values.add(t.getText().toString());

                                                    if (values.size() == 1) {
                                                        txt1.setBackgroundResource(R.drawable.blue_dot);
                                                    } else if (values.size() == 2) {
                                                        txt2.setBackgroundResource(R.drawable.blue_dot);
                                                    } else if (values.size() == 3) {
                                                        txt3.setBackgroundResource(R.drawable.blue_dot);
                                                    } else if (values.size() == 4) {
                                                        txt4.setBackgroundResource(R.drawable.blue_dot);
                                                        if (flg == 0) {
                                                            s = values.get(0) + values.get(1) + values.get(2)
                                                                    + values.get(3);
                                                            values.clear();
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    txt1.setBackgroundResource(R.drawable.gray_dot);
                                                                    txt2.setBackgroundResource(R.drawable.gray_dot);
                                                                    txt3.setBackgroundResource(R.drawable.gray_dot);
                                                                    txt4.setBackgroundResource(R.drawable.gray_dot);
                                                                    flg = 1;
                                                                }
                                                            }, 100);
                                                            pin_status.setText("Re-enter your pin");
                                                        } else {
                                                            s1 = values.get(0) + values.get(1) + values.get(2)
                                                                    + values.get(3);

                                                            if (s.equals(s1)) {
                                                                //Toast.makeText(PinActivity.this, s + "---" + s1 + "Success", Toast.LENGTH_SHORT).show();
                                                                CustomPreference.with(PinActivity.this).save("pin", s);
                                                                startActivity(new Intent(PinActivity.this, PortalDashboardActivity.class));
                                                                finish();
                                                            } else {
                                                                pin_status.setText("Pincode mismatch.Try again!!");
                                                                values.clear();
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        txt1.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt2.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt3.setBackgroundResource(R.drawable.gray_dot);
                                                                        txt4.setBackgroundResource(R.drawable.gray_dot);
                                                                    }
                                                                }, 100);
                                                            }
                                                        }

                                                    }
                                                }
                                            }


                                        }
        );

        forget_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPreference.with(PinActivity.this).removeAll();

                File cache = getCacheDir();
                File appDir = new File(cache.getParent());
                if (appDir.exists()) {
                    String[] children = appDir.list();
                    for (String s : children) {
                        if (!s.equals("lib")) {
                            deleteDir(new File(appDir, s));
                            startActivity(new Intent(PinActivity.this, LoginActivity.class));
                        }
                    }
                }
            }
        });
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public class CustomGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return keypad.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.pin_item_view, null);
            AppCompatTextView t = (AppCompatTextView) convertView.findViewById(R.id.pin_key);
            t.setText(keypad[position]);
            if (position == 9) {
                t.setVisibility(View.GONE);

            }
            return convertView;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
