package com.payal.editor;

import android.net.Uri;
import android.content.Context;
import java.io.File;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;
import com.getcapacitor.annotation.CapacitorPlugin;

import androidx.media3.common.MediaItem;
import androidx.media3.transformer.Transformer;
import androidx.media3.transformer.EditedMediaItem;
import androidx.media3.transformer.Composition;
import androidx.media3.transformer.ExportResult;
import androidx.media3.transformer.ExportException;

@CapacitorPlugin(name = "VideoEditor")
public class VideoEditorPlugin extends Plugin {

    @PluginMethod
    public void trimVideo(PluginCall call) {
        String inputUri = call.getString("inputUri");
        Long startMs = call.getLong("startMs");
        Long endMs = call.getLong("endMs");

        if (inputUri == null || startMs == null ||
            endMs == null || endMs <= startMs) {
            call.reject("Invalid video or trim duration");
            return;
        }

        File output = new File(
            getContext().getCacheDir(),
            "Payal_" + System.currentTimeMillis() + ".mp4"
        );

        MediaItem.ClippingConfiguration clipping =
            new MediaItem.ClippingConfiguration.Builder()
                .setStartPositionMs(startMs)
                .setEndPositionMs(endMs)
                .build();

        MediaItem item = new MediaItem.Builder()
            .setUri(Uri.parse(inputUri))
            .setClippingConfiguration(clipping)
            .build();

        EditedMediaItem edited =
            new EditedMediaItem.Builder(item).build();

        Transformer transformer =
            new Transformer.Builder(getContext())
                .addListener(new Transformer.Listener() {
                    @Override
                    public void onCompleted(
                        Composition composition,
                        ExportResult result
                    ) {
                        JSObject response = new JSObject();
                        response.put("success", true);
                        response.put("outputPath", output.getAbsolutePath());
                        call.resolve(response);
                    }

                    @Override
                    public void onError(
                        Composition composition,
                        ExportResult result,
                        ExportException exception
                    ) {
                        call.reject("Video export failed: " +
                            exception.getMessage());
                    }
                })
                .build();

        transformer.start(edited, output.getAbsolutePath());
    }
}
