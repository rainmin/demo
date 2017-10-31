package com.rainmin.demo.palette;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.rainmin.demo.BaseActivity;
import com.rainmin.demo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

/**
 * Created by chenming on 2017/10/30
 */

public class PaletteActivity extends BaseActivity
        implements View.OnClickListener, PaletteView.Callback {

    @BindView(R.id.palette) PaletteView mPaletteView;
    @BindView(R.id.iv_redo) View mRedo;
    @BindView(R.id.iv_undo) View mUndo;
    @BindView(R.id.iv_pen) View mPen;
    @BindView(R.id.iv_eraser) View mEraser;
    @BindView(R.id.iv_clear) View mClear;
    @BindView(R.id.iv_save) View mSave;

    private ProgressDialog mSaveProgressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomizedView(R.layout.activity_palette, "画板");

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

                Bitmap bm = mPaletteView.buildBitmap();
                String savedFile = saveImage(bm, 100);
                String message;
                if (savedFile != null) {
                    message = "save the palette successfully";
                } else {
                    message = "save the palette failed";
                }
                mSaveProgressDlg.dismiss();
                Snackbar.make(mSave, message, Snackbar.LENGTH_SHORT).show();
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
