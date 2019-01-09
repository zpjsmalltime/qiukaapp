package com.mayisports.qca.utils.share;

import com.mayisports.qca.utils.ToastUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;

/**
 * 分享回调类
 */

public class QCaSnsPostListener implements SocializeListeners.SnsPostListener  {
    @Override
    public void onStart() {
//        Toast.makeText(YZTApplication.getContext(), "分享开始喽", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
        if (eCode == 200) {
//            Toast.makeText(YZTApplication.getContext(), "分享成功", Toast.LENGTH_SHORT).show();
        } else {
            String eMsg = "";
            if (eCode == -101) {
                eMsg = "没有授权";
            }
//            Toast.makeText(YZTApplication.getContext(), "分享失败[" + eCode + "] " +
//                    eMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
