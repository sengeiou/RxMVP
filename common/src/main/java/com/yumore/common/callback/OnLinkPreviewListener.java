package com.yumore.common.callback;

import com.yumore.common.utility.SourceContent;

/**
 * Callback that is invoked with before and after the loading of a link preview
 */
public interface OnLinkPreviewListener {

    void onPreview();

    /**
     * @param sourceContent Class with all Contents from preview.
     * @param isNull        Indicates if the content is null.
     */
    void onPosition(SourceContent sourceContent, boolean isNull);
}
