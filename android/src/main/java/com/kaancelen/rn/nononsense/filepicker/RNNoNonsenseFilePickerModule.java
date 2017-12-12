package com.kaancelen.rn.nononsense.filepicker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RNNoNonsenseFilePickerModule extends ReactContextBaseJavaModule {
    private static final String NAME = "RNNoNonsenseFilePickerModule";
    private static final int READ_REQUEST_CODE = 12346;
    private static final String OPTION_TYPE = "type";

    private static final String E_ACTIVITY_DOES_NOT_EXIST = "ACTIVITY_DOES_NOT_EXIST";
    private static final String E_FAILED_TO_SHOW_PICKER = "FAILED_TO_SHOW_PICKER";
    private static final String E_DOCUMENT_PICKER_CANCELED = "DOCUMENT_PICKER_CANCELED";
    private static final String E_UNKNOWN_ACTIVITY_RESULT = "UNKNOWN_ACTIVITY_RESULT";
    private static final String E_INVALID_DATA_RETURNED = "INVALID_DATA_RETURNED";
    private static final String E_UNEXPECTED_EXCEPTION = "UNEXPECTED_EXCEPTION";

    private static final String FIELD_PATH = "path";
    private static final String FIELD_URI = "uri";

    private Promise promise;

    private final ActivityEventListener activityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == READ_REQUEST_CODE) {
                if (promise != null) {
                    onShowActivityResult(resultCode, data, promise);
                    promise = null;
                }
            }
        }
    };

    public RNNoNonsenseFilePickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(activityEventListener);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        getReactApplicationContext().removeActivityEventListener(activityEventListener);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void pick(ReadableMap args, Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Current activity does not exist");
            return;
        }

        this.promise = promise;

        ArrayList<String> extensions = new ArrayList<String>();
        if(!args.isNull(OPTION_TYPE)){
            ReadableArray types = args.getArray(OPTION_TYPE);
            for(int i=0; i<types.size(); i++){
                extensions.add(types.getString(i));
            }
        }

        try {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
            i.putStringArrayListExtra(FilteredFilePickerActivity.EXTRA_SHOW_EXTENSIONS, extensions);
            currentActivity.startActivityForResult(i, READ_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            this.promise.reject(E_FAILED_TO_SHOW_PICKER, e.getMessage());
            this.promise = null;
        }
    }

    private void onShowActivityResult(int resultCode, Intent data, Promise promise) {
        if (resultCode == Activity.RESULT_CANCELED) {
            promise.reject(E_DOCUMENT_PICKER_CANCELED, "User canceled document picker");
        } else if (resultCode == Activity.RESULT_OK) {
            try {
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                if(files.size() == 0 || files.get(0) == null){
                    promise.reject(E_INVALID_DATA_RETURNED, "Invalid data returned by intent");
                    return;
                }
                //Result and remove first '/root'
                String path = files.get(0).getPath().replaceFirst("/root", "");
                WritableMap writableMap = Arguments.createMap();
                writableMap.putString(FIELD_PATH, path);
                writableMap.putString(FIELD_URI, files.get(0).toString());

                promise.resolve(writableMap);
            } catch (Exception e) {
                promise.reject(E_UNEXPECTED_EXCEPTION, e.getMessage(), e);
            }
        } else {
            promise.reject(E_UNKNOWN_ACTIVITY_RESULT, "Unknown activity result: " + resultCode);
        }
    }
}
