package com.mayisports.qca.bean;

/**
 * Created by Zpj on 2018/3/23.
 */

public class EditNickNameBean {

    /**
     * status : {"login":1,"errno":3,"result":1,"errstr":"您上一次修改昵称的时间是2018年03月22日 16:37，30天内只能修改一次"}
     */

    public StatusBean status;

    public static class StatusBean {
        /**
         * login : 1
         * errno : 3
         * result : 1
         * errstr : 您上一次修改昵称的时间是2018年03月22日 16:37，30天内只能修改一次
         */

        public int login;
        public int errno;
        public int result;
        public String errstr;
    }
}
