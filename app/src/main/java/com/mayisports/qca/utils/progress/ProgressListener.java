package com.mayisports.qca.utils.progress;

public interface ProgressListener {

    void progress(long bytesRead, long contentLength, boolean done);

}
