/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.mainFragments;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.material.snackbar.Snackbar;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import tech.pcreate.gesty_thesmartreader.R;
import tech.pcreate.gesty_thesmartreader.epubReader.ReaderActivity;
import tech.pcreate.gesty_thesmartreader.library.AddToLibraryAsync;
import tech.pcreate.gesty_thesmartreader.mainFragments.adapter.HomeBooksAdapter;
import tech.pcreate.gesty_thesmartreader.mainFragments.model.HomeBookCover;

import static com.github.florent37.runtimepermission.RuntimePermission.askPermission;
import static tech.pcreate.gesty_thesmartreader.utils.AppConstants.PATH;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements
        DiscreteScrollView.OnItemChangedListener,
        View.OnClickListener{


    private DiscreteScrollView itemPicker;
    private List<HomeBookCover> booklist =  new ArrayList<>();
    private Button newBookButton;
    private InfiniteScrollAdapter infiniteAdapter;
    private Button openBook;
    private String mFileLoc;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        itemPicker = view.findViewById(R.id.picker);

        newBookButton = view.findViewById(R.id.newBook);
        newBookButton.setOnClickListener(this);
        openBook = view.findViewById(R.id.open_book);
        openBook.setOnClickListener(this);
        getCovers();

        String fileLoc = null;
        if (getArguments() != null) {
            fileLoc = getArguments().getString("loc");
            new AddToLibraryAsync().execute(fileLoc);
        }
        if (fileLoc != null) openBook(fileLoc);

        return view;
    }

    private void getCovers() {

        File directory = new File(PATH);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {

            String file_name = files[i].getName();

            try {
                Book book = (new EpubReader()).readEpub(new FileInputStream(PATH + file_name));
                byte[] asd = book.getCoverImage().getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(asd, 0, asd.length);
                booklist.add(new HomeBookCover(bitmap, PATH + file_name));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        displayBooks();
    }

    private void displayBooks() {
        itemPicker.setAdapter(new HomeBooksAdapter(booklist));
        itemPicker.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new HomeBooksAdapter(booklist));
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.newBook){
            //open book picker
            checkPermissions();
        }else if (view.getId() == R.id.open_book){
            openBook(mFileLoc);
        }

    }

    private void checkPermissions() {
        askPermission(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)

                .onAccepted((result) -> {
                    //all permissions already granted or just granted
                    displayPicker();
                })
                .onDenied((result) -> {
                    Snackbar.make(newBookButton, getString(R.string.permissions_needed), Snackbar.LENGTH_SHORT).show();
                    //permission denied, but you can ask again, eg:

                    new AlertDialog.Builder(getActivity())
                            .setMessage(getContext().getString(R.string.give_permission_storage))
                            .setPositiveButton(getContext().getString(R.string.okay_string), (dialog, which) -> result.askAgain()) // ask again
                            .setNegativeButton(getContext().getString(R.string.no_string), (dialog, which) -> dialog.dismiss())
                            .show();

                })
                .onForeverDenied((result) -> {

                    Snackbar.make(newBookButton, getString(R.string.permissions_needed), Snackbar.LENGTH_SHORT)
                            .setAction(getContext().getString(R.string.go_to_settings), view -> result.goToSettings()).show();

                })
                .ask();
    }

    private void displayPicker() {

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        FilePickerDialog dialog = new FilePickerDialog(getActivity(),properties);
        dialog.setTitle("Select an EPub File");
        dialog.show();

        dialog.setDialogSelectionListener(files -> {
            //Log.e("path = ", files[0]);

            //Add book to offline library
            new AddToLibraryAsync().execute(files[0]);

            openBook(files[0]);

        });
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int position) {
        int pos = itemPicker.getCurrentItem();
        HomeBookCover bookCover = booklist.get(pos );
        String fileLoc = bookCover.getLocation();
        //Log.e("Position = ", fileLoc);

        mFileLoc = fileLoc;
    }

    private void openBook(String fileloc){
        Intent intent = new Intent(getActivity(), ReaderActivity.class);
        intent.putExtra("loc", fileloc);
        startActivity(intent);

        getActivity().finish();
    }
}
