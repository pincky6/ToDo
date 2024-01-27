package com.diplom.todoapp.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.diplom.todoapp.R;

public class EditorsUtil {

    public static  <Editor extends EditText> boolean checkEditors(Editor... editors){
        for (Editor editor: editors){
            if(editor.getText().toString().isEmpty()){
                return true;
            }
        }
        return false;
    }
    public static  <Editor extends EditText> void setErrorBackground(Editor... editors) {
        for (Editor editor : editors) {
            if (editor.getText().toString().isEmpty()) {
                editor.setBackgroundResource(R.drawable.error_background);
                editor.setError("Empty");
            }
        }
    }
    public static <Editor extends EditText> void initTextWatchers(Editor... editors) {
        for (Editor editor : editors)
            editor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!editor.getText().toString().isEmpty()) {
                        editor.setBackgroundResource(R.drawable.normal_background);
                        editor.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
    }

    public static<Editor extends EditText> void setErrorState(Editor... editors){
        for(Editor editor: editors){
            editor.setBackgroundResource(R.drawable.error_background);
            editor.setError("Wrong input");
        }
    }
    public static<Editor extends EditText> void setConnectedTextWatchers(Editor... editors){
        for(Editor editor: editors){
            editor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    for(Editor editor1: editors){
                        editor1.setBackgroundResource(R.drawable.normal_background);
                        editor1.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
