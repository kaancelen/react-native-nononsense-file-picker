package com.kaancelen.rn.nononsense.filepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nononsenseapps.filepicker.AbstractFilePickerActivity;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;
import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by berat.kaan.celen on 11.12.2017.
 */

public class FilteredFilePickerActivity extends AbstractFilePickerActivity<File> {

    public static final String EXTRA_SHOW_EXTENSIONS = "extra_show_extensions";

    @Override
    protected AbstractFilePickerFragment<File> getFragment(
            @Nullable String startPath,
            int mode,
            boolean allowMultiple,
            boolean allowCreateDir,
            boolean allowExistingFile,
            boolean singleClick) {

        //Get extensions for filter
        ArrayList<String> extensions = getIntent().getStringArrayListExtra(EXTRA_SHOW_EXTENSIONS);
        //Decide if files will filtered or not?
        AbstractFilePickerFragment<File> fragment = null;
        if(extensions == null || extensions.size() == 0){
            fragment = new FilePickerFragment();
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(FilteredFilePickerFragment.KEY_SHOW_EXTENSIONS, extensions);

            fragment = new FilteredFilePickerFragment();
            fragment.setArguments(bundle);
        }
        fragment.setArgs(startPath, mode, allowMultiple, allowCreateDir, allowExistingFile, singleClick);

        return fragment;

    }

}
