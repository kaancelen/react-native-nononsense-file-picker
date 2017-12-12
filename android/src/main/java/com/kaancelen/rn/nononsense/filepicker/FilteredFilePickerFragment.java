package com.kaancelen.rn.nononsense.filepicker;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by berat.kaan.celen on 11.12.2017.
 */

public class FilteredFilePickerFragment extends FilePickerFragment {

    public static final String KEY_SHOW_EXTENSIONS = "key_show_extensions";

    private ArrayList<String> extensions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get extensions that should shown
        Bundle bundle = getArguments();
        extensions = bundle.getStringArrayList(KEY_SHOW_EXTENSIONS);
    }

    /**
     *
     * @param file
     * @return The file extension. If file has no extension, it returns null.
     */
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    @Override
    protected boolean isItemVisible(final File file) {
        boolean ret = super.isItemVisible(file);
        if (ret && !isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            String ext = getExtension(file);
            return ext != null && extensions.contains(ext.toLowerCase());
        }
        return ret;
    }
}
