package com.example.root.contactsdashboard;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.root.contactsdashboard.adapter.ContactAdapter;
import com.example.root.contactsdashboard.util.CallLogDetail;
import com.example.root.contactsdashboard.util.DurationComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    GridView mContactGridView;
    ProgressBar mProgressBar;
    //Call Log List type as Class Type
    List<CallLogDetail> mCallLogDetailsList;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_dashboard_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mContactGridView = (GridView) findViewById(R.id.gridview_contact);
        mCallLogDetailsList = new ArrayList<>();

        mContactGridView.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!checkPermission()) {

                requestPermission();

            }
        }

        new CallLogTask().execute();


    }


    /**
     * CallLogTask Async Task class used doing back ground operations for fetching Call Log and Contacts
     * running in new thread for Preventing ANR(Application Not Responding)
     */
    public class CallLogTask extends AsyncTask<Void, Void, List<CallLogDetail>> {
        @Override
        protected List<CallLogDetail> doInBackground(Void... params) {

            //Getting Call Log
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_CALL_LOG, READ_CONTACTS}, PERMISSION_REQUEST_CODE);
            } else {
                Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
                int Name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
                int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
                while (managedCursor.moveToNext()) {
                    String name = managedCursor.getString(Name);
                    String phNumber = managedCursor.getString(number);
                    String callType = managedCursor.getString(type);
                    String callDate = managedCursor.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));
                    int callDuration = Integer.valueOf(managedCursor.getString(duration));
                    String dir = null;
                    String email = null;


                    Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                            Uri.encode(phNumber));
                    String[] projection = new String[]{ContactsContract.Contacts._ID};

                    Uri photoUri = null;
                    String contactID = null;
                    String emailContactId = null;
                    long contactIdPerson = 0;


                    //Querying Photo Uri
                    Cursor c = getContentResolver().query(contactUri, projection,
                            null, null, null);


                    if (c.moveToFirst()) {
                        contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        contactIdPerson = Long.parseLong(contactID);
                        emailContactId = c.getString(c
                                .getColumnIndex(BaseColumns._ID));


                        photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID));

                    }

                    c.close();


                    if (callDuration > 0) {


                        //Quering Email id
                        Cursor emailCur = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + emailContactId,
                                null, null);
                        if (emailCur.moveToNext()) {
                            // This would allow you get several email addresses
                            // if the email addresses were stored in an array
                            email = emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        }
                        emailCur.close();

                    }


                    //Finding Type of call
                    int dircode = Integer.parseInt(callType);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            dir = "OUTGOING";
                            break;

                        case CallLog.Calls.INCOMING_TYPE:
                            dir = "INCOMING";
                            break;

                        case CallLog.Calls.MISSED_TYPE:
                            dir = "MISSED";
                            break;
                    }

                    //Adding Call Duration for Every Contact
                    if (!mCallLogDetailsList.isEmpty() && callDuration > 0) {
                        boolean flag = false;

                        for (int i = 0; i < mCallLogDetailsList.size(); i++) {
                            CallLogDetail callLogDetail = mCallLogDetailsList.get(i);
                            callLogDetail.getContactPhoneNumber();

                            if (contactIdPerson == callLogDetail.getContactId()) {
                                mCallLogDetailsList.get(i).setContactLastContactTime(callDayTime);
                                int duration1 = mCallLogDetailsList.get(i).getTotalCallDuration();
                                mCallLogDetailsList.get(i).setTotalCallDuration(callDuration + duration1);
                                flag = true;

                            }
                        }
                        if (flag == false) {
                            mCallLogDetailsList.add(new CallLogDetail(contactIdPerson, name, phNumber, email, dir, callDayTime, callDuration, photoUri));


                        }


                    } else if (callDuration > 0) {
                        mCallLogDetailsList.add(new CallLogDetail(contactIdPerson, name, phNumber, email, dir, callDayTime, callDuration, photoUri));

                    }

                }
            }


            return mCallLogDetailsList;
        }

        @Override
        protected void onPostExecute(List<CallLogDetail> callLogDetailList) {
            super.onPostExecute(callLogDetailList);

            //Sorting Desending order through Duration Comparator
            Collections.sort(callLogDetailList, new DurationComparator());
            mContactGridView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            ContactAdapter contactAdapter = new ContactAdapter(getApplicationContext(), callLogDetailList);
            mContactGridView.setAdapter(contactAdapter);

        }
    }






    //Checking Permission for above and Android 6.0
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALL_LOG);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    //Requesting Permissions for above and Android 6.0
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_CALL_LOG, READ_CONTACTS}, PERMISSION_REQUEST_CODE);

    }


    //Requesting Permissions for above and Android 6.0
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean readLog = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readContacts = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if ((!readLog) && (!readContacts)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            shouldShowRequestPermissionRationale(READ_CALL_LOG);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{READ_CALL_LOG, READ_CONTACTS},
                                        PERMISSION_REQUEST_CODE);
                            }
                        }

                    }


                }
                break;


        }
    }

}