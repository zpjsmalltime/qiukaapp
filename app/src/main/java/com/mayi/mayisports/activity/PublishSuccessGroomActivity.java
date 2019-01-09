package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.mayi.mayisports.R;
import com.mayisports.qca.bean.PublishSuccessBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.text.DecimalFormat;

public class PublishSuccessGroomActivity extends BaseActivity {


    public static void start(Activity activity,String betId){
        Intent intent = new Intent(activity,PublishSuccessGroomActivity.class);
        intent.putExtra("betId",betId);
        activity.startActivity(intent);

    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_publish_success_groom;
    }

    private TextView tv_match_name_publish_success;
    private TextView tv_content_publish_success;
    private TextView tv_price_publish_success;

    @Override
    public void finish() {
        super.finish();

        Intent intent = new Intent(PublishGroomActivity.ACTION);
        intent.putExtra(LoginActivity.RESULT, 1);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_title.setText("发布成功");
        tv_ritht_title.setVisibility(View.INVISIBLE);

        tv_match_name_publish_success = findViewById(R.id.tv_match_name_publish_success);
        tv_content_publish_success = findViewById(R.id.tv_content_publish_success);
        tv_price_publish_success = findViewById(R.id.tv_price_publish_success);

    }

    private String betId;
    @Override
    protected void initDatas() {
        super.initDatas();
        betId = getIntent().getStringExtra("betId");
        requestNetDatas();
    }

    private PublishSuccessBean publishSuccessBean;
    private void requestNetDatas() {
        //http://app.mayisports.com/php/api.php?action=user_recommendation&type=matchDetail&betId=1424814
        String url = Constant.BASE_URL + "php/api.php";

        HttpParams params = new HttpParams();
        params.put("action","user_recommendation");
        params.put("type","matchDetail");
        params.put("betId",betId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                    publishSuccessBean = JsonParseUtils.parseJsonClass(string,PublishSuccessBean.class);
                    if(publishSuccessBean != null && publishSuccessBean.data != null){
                        updateViews();
                    }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }
    private DecimalFormat df = new DecimalFormat("#.00");
    private void updateViews() {
        if(publishSuccessBean.data.match == null ||publishSuccessBean.data.recommendation == null)return;
        PublishSuccessBean.DataBean.MatchBean match = publishSuccessBean.data.match;
        String hostName = URLDecoder.decode(match.hostTeamName);
        String awayName = URLDecoder.decode(match.awayTeamName);
        String matchName = match.leagueName +"\t"+hostName+"vs"+awayName;
       tv_match_name_publish_success.setText(matchName);

       //'hostOdds' 'awayOdds' 'winOdds' 'drowOdds' 'loseOdds' 'smallScoreOdds' 'bigScoreOdds'
        PublishSuccessBean.DataBean.RecommendationBean recommendation = publishSuccessBean.data.recommendation;
        String content = "";
        if(recommendation.betDetail.hostOdds != null){//
            Double aDouble = Double.valueOf(recommendation.betDetail.hostOdds)/10000;
            String format = df.format(aDouble + 1);
            String s0 = Utils.parseOddOfHandicap(recommendation.betDetail.tape) + "   "+format;
             content = "亚盘\t"+hostName+"\t"+s0;
        }else if(recommendation.betDetail.awayOdds != null){
            String a = "";
            if(recommendation.betDetail.tape.contains("-")){
                a = recommendation.betDetail.tape;
                a = a.replace("-", "");
            }else{
                a = "-"+recommendation.betDetail.tape;
            }
            Double aDouble = Double.valueOf(recommendation.betDetail.awayOdds)/10000;
            String format = df.format(aDouble + 1);
            String s0 = Utils.parseOddOfHandicap(a) + "   "+format;
            content = "亚盘\t"+awayName+"\t"+s0;
        }else if(recommendation.betDetail.winOdds != null){
            double v = Double.valueOf(recommendation.betDetail.winOdds)/ 10000.0;


            content = "欧盘\t主胜\t"+df.format(v);
        }else if(recommendation.betDetail.loseOdds != null){
            double v2 = Double.valueOf(recommendation.betDetail.loseOdds) / 10000.0;
            content = "欧盘\t客胜\t"+df.format(v2);
        }else if(recommendation.betDetail.drowOdds != null){
            double v1 = Double.valueOf(recommendation.betDetail.drowOdds) / 10000.0;
            content = "欧盘\t平局\t"+df.format(v1);
        }else if(recommendation.betDetail.bigScoreOdds != null){
            String s0 = "大小球\t大于" + Utils.parseOddOfHandicap(recommendation.betDetail.tape) + "   "+df.format(Double.valueOf(recommendation.betDetail.bigScoreOdds)/10000 + 1);
            content = s0;
        }else  if(recommendation.betDetail.smallScoreOdds != null){
            String s1 = "大小球\t小于" + Utils.parseOddOfHandicap(recommendation.betDetail.tape) + "   "+df.format(Double.valueOf(recommendation.betDetail.smallScoreOdds)/10000 + 1);
            content = s1;
        }
        tv_content_publish_success.setText(content);

        Integer price = Integer.valueOf(recommendation.price);
        if(price == 0){
          tv_price_publish_success.setText("免费");
        }else{
            tv_price_publish_success.setText(price+"金币");
        }



    }
}
