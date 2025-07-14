package com.data.agricultural_material_management_system.utill;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import io.micrometer.common.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;

import java.io.IOException;

public class QiniuCloudUtil {

    // 设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "OQtt4h3_Y9cA5sYT2MEpq-id3XqC8X61F1ePVrq2";
    private static final String SECRET_KEY = "Jl9THCXFGRnGIOfQubIleFTFySxaBppaJT99r6s8";

    // 要上传的空间
    private static final String bucketname = "ymyimg";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //七牛云域名
    private static final String DOMAIN = "https://love.qmys.fun";


    public String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }

    // 普通上传
    public String upload(String filePath, String fileName) throws IOException {
        // 创建上传对象
        Configuration cfg = new Configuration(Region.autoRegion()); // Use autoRegion for better compatibility
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            // 调用put方法上传
            String token = auth.uploadToken(bucketname);
            if (StringUtils.isEmpty(token)) {
                System.err.println("未获取到token，请重试！");
                return null;
            }
            Response res = uploadManager.put(filePath, fileName, token);
            System.out.println("上传响应: " + res.bodyString());
            if (res.isOK()) {
                Ret ret = res.jsonToObject(Ret.class);
                return DOMAIN + "/" + ret.key;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            System.err.println("上传失败: " + r.toString());
            try {
                System.err.println("响应内容: " + r.bodyString());
            } catch (QiniuException e1) {
                // ignore
            }
        }
        return null;
    }


    //base64方式上传
    public String put64image(byte[] base64, String key) throws Exception {
        String file64 = Base64.encodeToString(base64, 0);
        Integer l = base64.length;
        String url = "http://upload.qiniu.com/putb64/" + l + "/key/" + UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            // Ensure the returned URL is correct
            return DOMAIN + "/" + key; // Added "/" between DOMAIN and key
        } else {
            throw new IOException("Failed to upload image: " + response.body().string());
        }
    }

    // 普通删除(暂未使用以下方法，未测试)
    public void delete(String key) throws IOException {
        // 实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, new Configuration(Region.huanan())); // Specify the region (e.g., huanan for South China)
        // 此处的25是去掉：https://download.pnki.cn/,剩下的key就是图片在七牛云的名称
        key = key.substring(25);
        try {
            // 调用delete方法移动文件
            bucketManager.delete(bucketname, key);
        } catch (QiniuException e) {
            // 捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }

    class Ret {
        public long fsize;
        public String key;
        public String hash;
        public int width;
        public int height;
    }
}
