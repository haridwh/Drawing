package com.example.skday.drawing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.skday.drawing.databinding.ActivityMainBinding;

import java.util.UUID;

    public class MainActivity extends AppCompatActivity {

    private ImageButton currPaint;
    private ActivityMainBinding binding;
    private String select = "#FF999999";
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(this);
        currPaint =(ImageButton) binding.pallet.getChildAt(0);
        currPaint.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.paint_pressed));
        binding.btnBrush.setBackgroundColor(Color.parseColor(select));
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        binding.canvas.setBrushSize(mediumBrush);
    }

    public void paintOnClick(View view){
        if (currPaint != view){
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            binding.canvas.setColor(color);
            imgView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.paint_pressed));
            currPaint.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

    public void menuOnClick(View view){
        if (view.getId() == binding.btnNew.getId()){
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Buat Baru");
            newDialog.setMessage("Buat Baru?");
            newDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    binding.btnBrush.setBackgroundColor(Color.parseColor(select));
                    binding.btnEraser.setBackgroundColor(0);
                    binding.canvas.startNew();
                    binding.canvas.setErase(false);
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    binding.canvas.setErase(false);
                    dialog.cancel();
                }
            });
            newDialog.show();
        }else if(view.getId() == binding.btnEraser.getId()){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Ukuran Penghapus");
            brushDialog.setContentView(R.layout.brush_size);
            brushDialog.show();
            final ImageButton small = (ImageButton) brushDialog.findViewById(R.id.small);
            small.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.canvas.setErase(true);
                    binding.canvas.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            final ImageButton medium = (ImageButton) brushDialog.findViewById(R.id.medium);
            medium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.canvas.setErase(true);
                    binding.canvas.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            final ImageButton large = (ImageButton) brushDialog.findViewById(R.id.large);
            large.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.canvas.setErase(true);
                    binding.canvas.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            binding.btnEraser.setBackgroundColor(Color.parseColor(select));
            binding.btnBrush.setBackgroundColor(0);
            binding.canvas.setErase(true);
        }else if (view.getId() == binding.btnBrush.getId()){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Ukuran Kuas");
            brushDialog.setContentView(R.layout.brush_size);
            brushDialog.show();
            final ImageButton small = (ImageButton) brushDialog.findViewById(R.id.small);
            small.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.canvas.setBrushSize(smallBrush);
                    binding.canvas.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            final ImageButton medium = (ImageButton) brushDialog.findViewById(R.id.medium);
            medium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.canvas.setBrushSize(mediumBrush);
                    binding.canvas.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            final ImageButton large = (ImageButton) brushDialog.findViewById(R.id.large);
            large.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.canvas.setBrushSize(largeBrush);
                    binding.canvas.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            binding.btnBrush.setBackgroundColor(Color.parseColor(select));
            binding.btnEraser.setBackgroundColor(0);
            binding.canvas.setErase(false);
        }else if (view.getId() == binding.btnSave.getId()){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    binding.canvas.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(),
                            binding.canvas.getDrawingCache(), UUID.randomUUID().toString()+".png", "drawing");

                    if (imgSaved!=null){
                        Toast.makeText(getApplicationContext(),"Gambar tersimpan di galery!",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Gambar tersimpan di galery!",
                                Toast.LENGTH_SHORT).show();
                    }
                    binding.canvas.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }
}