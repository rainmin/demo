package com.rainmin.demo.palette;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.rainmin.demo.BaseActivity;
import com.rainmin.demo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenming on 2017/10/30
 */

public class PaletteActivity extends BaseActivity
        implements View.OnClickListener, PaletteView.Callback {

    PaletteView mPaletteView;
    View mRedo;
    View mUndo;
    View mPen;
    View mEraser;
    View mClear;
    View mSave;

    private ProgressDialog mSaveProgressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomizedView(R.layout.activity_palette, "画板", true);

        mPaletteView = (PaletteView) findViewById(R.id.palette);
        mRedo = findViewById(R.id.iv_redo);
        mUndo = findViewById(R.id.iv_undo);
        mPen = findViewById(R.id.iv_pen);
        mEraser = findViewById(R.id.iv_eraser);
        mClear = findViewById(R.id.iv_clear);
        mSave = findViewById(R.id.iv_save);

        mRedo.setOnClickListener(this);
        mUndo.setOnClickListener(this);
        mPen.setOnClickListener(this);
        mEraser.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mSave.setOnClickListener(this);

        mPen.setSelected(true);
        mUndo.setEnabled(false);
        mRedo.setEnabled(false);
        //mPaletteView.setMode(PaletteView.Mode.DRAW);
        mPaletteView.setCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onUndoRedoStatusChanged() {
        mUndo.setEnabled(mPaletteView.canUndo());
        mRedo.setEnabled(mPaletteView.canRedo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_undo:
                mPaletteView.undo();
                break;
            case R.id.iv_redo:
                mPaletteView.redo();
                break;
            case R.id.iv_pen:
                v.setSelected(true);
                mEraser.setSelected(false);
                mPaletteView.setMode(PaletteView.Mode.DRAW);
                break;
            case R.id.iv_eraser:
                v.setSelected(true);
                mPen.setSelected(false);
                mPaletteView.setMode(PaletteView.Mode.ERASER);
                break;
            case R.id.iv_clear:
                mPaletteView.clear();
                break;
            case R.id.iv_save:
                if (mSaveProgressDlg == null) {
                    initSaveDialog();
                }
                mSaveProgressDlg.show();

                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        Bitmap bm = mPaletteView.buildBitmap();
                        String savedFile = saveImage(bm, 100);
                        if (savedFile == null)
                            savedFile = "fail";
                        e.onNext(savedFile);
                        e.onComplete();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String savedFile) throws Exception {
                                String message;
                                if (TextUtils.equals(savedFile, "fail")) {
                                    message = "save the palette failed";
                                } else {
                                    message = "save the palette successfully";
                                }
                                mSaveProgressDlg.dismiss();
                                Snackbar.make(mSave, message, Snackbar.LENGTH_SHORT).show();
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void initSaveDialog() {
        mSaveProgressDlg = new ProgressDialog(this);
        mSaveProgressDlg.setMessage("It's saving, please waiting...");
        mSaveProgressDlg.setCancelable(false);
    }

    private  String saveImage(Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (appDir == null) {
            return null;
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
