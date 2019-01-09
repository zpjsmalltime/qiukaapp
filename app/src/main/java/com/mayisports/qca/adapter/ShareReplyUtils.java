package com.mayisports.qca.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.ComentsDetailsActivity;
import com.mayi.mayisports.activity.PersonalDetailActivity;
import com.mayi.mayisports.activity.PicShowActivity;
import com.mayi.mayisports.activity.ShareComentsDetailsActivity;
import com.mayisports.qca.bean.ComentsDetailBean;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.HomeItemBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.view.CustomLinkMovementMethod;
import com.mayisports.qca.view.MyImageSpan;
import com.mayisports.qca.view.NoLineClickSpan;
import com.mayisports.qca.view.SeeAllTextViewUtils;

import java.util.List;

/**
 * 
 * 分享回复 拼接适配器
 * Created by zhangpengju on 2018/8/23.
 */

public class ShareReplyUtils {


    /**
     * 绑定 回复 连串@
     * @param activity
     * @param textView
     * @param parentBeanList
     */
    public static  void bindReplyLink(final Activity activity, TextView textView, final DynamicBean.ListBean.Reply replyBean, List<ComentsDetailBean.DataBean.ListBean.ParentBean> parentBeanList){
        String reply = replyBean.reply;


        textView.setMaxLines(4);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(reply);

        if(!TextUtils.isEmpty(replyBean.imglist)){
            SpannableString spannableString = new SpannableString(" 0查看图片");

            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
            spannableString.setSpan(foregroundColorSpan, 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            MyImageSpan myImageSpan = new MyImageSpan(activity,R.drawable.watch_img);
            spannableString.setSpan(myImageSpan,1,2,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            NoLineClickSpan clickableSpan = new NoLineClickSpan(replyBean.imglist);
            spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            clickableSpan.setOnClickListenter(new NoLineClickSpan.OnClick() {
                @Override
                public void onClick(String tag) {

                    PicShowActivity.start(activity,tag,0);

                }
            });


            spannableStringBuilder.append(spannableString);
        }


        if(parentBeanList != null && parentBeanList.size()>0){




            for(int i = 0;i<parentBeanList.size();i++) {

                final  ComentsDetailBean.DataBean.ListBean.ParentBean parentBean = parentBeanList.get(i);


//                parentBean.nickname = SeeAllTextViewUtils.ToDBC(parentBean.nickname);
//                parentBean.reply = SeeAllTextViewUtils.ToDBC(parentBean.reply);
                SpannableString spannableString = new SpannableString("//@" + parentBean.nickname);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
                spannableString.setSpan(foregroundColorSpan, 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


                NoLineClickSpan clickableSpan = new NoLineClickSpan(parentBean.user_id);
                spannableString.setSpan(clickableSpan, 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


                clickableSpan.setOnClickListenter(new NoLineClickSpan.OnClick() {
                    @Override
                    public void onClick(String tag) {
                        if (isInterceptStartPersonalActivity(parentBean.user_id,activity)) {
                            return;
                        }

                        String s = new Gson().toJson(parentBean);
                        PersonalDetailActivity.start(activity, parentBean.user_id, s);

                    }
                });

                spannableStringBuilder.append(spannableString);
                spannableStringBuilder.append("：" + parentBean.reply);





                if(!TextUtils.isEmpty(parentBean.imglist)) {
                    SpannableString spannableStringImg = new SpannableString(" 0查看图片");

                    ForegroundColorSpan foregroundColorSpanImg = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
                    spannableStringImg.setSpan(foregroundColorSpanImg, 1, spannableStringImg.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    MyImageSpan myImageSpan = new MyImageSpan(activity, R.drawable.watch_img);
                    spannableStringImg.setSpan(myImageSpan, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    NoLineClickSpan clickableSpanImg = new NoLineClickSpan(parentBean.imglist);
                    spannableStringImg.setSpan(clickableSpanImg, 0, spannableStringImg.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    clickableSpanImg.setOnClickListenter(new NoLineClickSpan.OnClick() {
                        @Override
                        public void onClick(String tag) {

                            PicShowActivity.start(activity, tag, 0);

                        }
                    });


                    spannableStringBuilder.append(spannableStringImg);
                }

            }
        }

        textView.setMovementMethod(CustomLinkMovementMethod.getInstance());
        textView.setText(spannableStringBuilder);
    }


    /**
     * 绑定转发原文数据
     * @param activity
     * @param llContainer
     * @param textView
     * @param reply
     * @param comment
     * @param position
     */
    public static void bindShareOriginal(final Activity activity, LinearLayout llContainer, TextView textView, final DynamicBean.ListBean.Reply reply, final DynamicBean.ListBean.CommentBean comment, final int position){
        if (reply.user != null && comment != null && comment.status == 0) {

            llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComentsDetailsActivity.startForResult(2, activity, comment.comment_id, position);

                }
            });
            llContainer.setVisibility(View.VISIBLE);



            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append("");

            SpannableString spannableString = new SpannableString("@" + reply.user.nickname);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
            spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);





            NoLineClickSpan clickableSpan = new NoLineClickSpan(reply.user.user_id);
            clickableSpan.setOnClickListenter(new NoLineClickSpan.OnClick() {
                @Override
                public void onClick(String tag) {
                    if(isInterceptStartPersonalActivity(reply.user.user_id,activity)){
                        return;
                    }

                    String s = new Gson().toJson(reply.user);
                    PersonalDetailActivity.start(activity,reply.user.user_id,s);

                }
            });


            spannableString.setSpan(clickableSpan,0,spannableString.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            spannableStringBuilder.append(spannableString);
           textView.setMovementMethod(CustomLinkMovementMethod.getInstance());

            if(TextUtils.isEmpty(comment.title)){
                spannableStringBuilder.append("："+comment.comment);
            }else{
                spannableStringBuilder.append("：【"+comment.title+"】"+comment.comment);
            }
           textView.setText(spannableStringBuilder);

           textView.setVisibility(View.VISIBLE);

            textView.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black));
        }else{
            textView.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
           textView.setVisibility(View.VISIBLE);
           textView.setText("抱歉，此动态已被删除");
           llContainer.setOnClickListener(null);
        }
    }



    private static boolean isInterceptStartPersonalActivity(String userId,Activity activity){
        if (LoginUtils.isUserSelf(userId) && activity instanceof PersonalDetailActivity) {
            return true;
        }
        return false;
    }




}
