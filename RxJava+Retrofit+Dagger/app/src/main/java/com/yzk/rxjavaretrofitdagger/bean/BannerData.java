package com.yzk.rxjavaretrofitdagger.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by android on 9/21/16.
 */
public class BannerData implements Serializable {
    // {"message":"取得成功"
    // ,"ret_code":"0"
    // ,"data"
    // :[{"id":61,"createDate":1457428656000,"updateDate":1457428656000,"releaseDate":null,"picDir":"http://ityf.dzwsyl.com:8080/upload/photo/20160308171734.jpg","createrId":"admin","enabled":"Y"}
    // ,{"id":60,"createDate":1453789624000,"updateDate":1453789624000,"releaseDate":null,"picDir":"http://ityf.dzwsyl.com:8080/upload/photo/20160126142702.jpg","createrId":"admin","enabled":"Y"}
    // ,{"id":58,"createDate":1453449065000,"updateDate":1453449065000,"releaseDate":null,"picDir":"http://ityf.dzwsyl.com:8080/upload/photo/20160122155103.jpg","createrId":"admin","enabled":"Y"}]}


    public String message;
    public String ret_code;
    public List<Banner> data;

    @Override
    public String toString() {
        return "BannerData{" +
                "message='" + message + '\'' +
                ", ret_code='" + ret_code + '\'' +
                ", data=" + data +
                '}';
    }

    public class Banner implements Serializable{
        public String id;
        public String createDate;
        public String updateDate;
        public String picDir;

        @Override
        public String toString() {
            return "Banner{" +
                    "id='" + id + '\'' +
                    ", createDate='" + createDate + '\'' +
                    ", updateDate='" + updateDate + '\'' +
                    ", picDir='" + picDir + '\'' +
                    ", enabled='" + enabled + '\'' +
                    '}';
        }

        public String enabled;

    }
}
