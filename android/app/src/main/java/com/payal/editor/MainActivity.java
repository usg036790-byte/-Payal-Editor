jpackage com.payal.editor;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(VideoEditorPlugin.class);
        super.onCreate(savedInstanceState);
    }
}package com.payal.editor;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {}
