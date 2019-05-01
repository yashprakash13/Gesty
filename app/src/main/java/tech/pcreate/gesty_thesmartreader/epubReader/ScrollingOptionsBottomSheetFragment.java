/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.epubReader;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import tech.pcreate.gesty_thesmartreader.R;

public class ScrollingOptionsBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private BottomSheetClick bottomSheetClick;
    private CheckedChangeListener changeListener;
    private Dialog dialog;

    public ScrollingOptionsBottomSheetFragment() {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        this.dialog = dialog;

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.scroll_options_bottom_sheet_layout, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        Button slow = view.findViewById(R.id.slow);
        Button fast = view.findViewById(R.id.fast);
        Button pretty_fast = view.findViewById(R.id.pretty_fast);
        slow.setOnClickListener(this);
        fast.setOnClickListener(this);
        pretty_fast.setOnClickListener(this);

        AppCompatCheckBox checkBox = view.findViewById(R.id.rememberScrollChoice);
        checkBox.setOnCheckedChangeListener(this);
    }

    public void setBottomSheetClickListener(BottomSheetClick bottomSheetClick){
        this.bottomSheetClick = bottomSheetClick;
    }

    public void setChangeListener(CheckedChangeListener changeListener){
        this.changeListener = changeListener;
    }

    public void dismissBottomSheet(){
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.slow)  bottomSheetClick.onBottomSheetClick(0);

        else if (v.getId() == R.id.fast) bottomSheetClick.onBottomSheetClick(1);

        else bottomSheetClick.onBottomSheetClick(2);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        changeListener.onChecked(b);
    }

    public interface BottomSheetClick{
        void onBottomSheetClick(int i);
    }

    public interface CheckedChangeListener{
        void onChecked(boolean b);
    }
}