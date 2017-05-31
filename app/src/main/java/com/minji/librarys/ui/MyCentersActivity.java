package com.minji.librarys.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseActivity;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.uitls.ACache;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.ViewsUitls;
import com.minji.librarys.widget.roundediamgeview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyCentersActivity extends BaseActivity implements View.OnClickListener {

    private TextView studentNumber;
    private TextView allIntegral;
    private RoundedImageView roundedImageView;
    private Dialog mCameraDialog;
    private String imageDir;

    @Override
    public void onCreateContent() {

        setBackVisibility(View.VISIBLE);
        setSettingVisibility(View.GONE);
        setTitleVisibility(View.VISIBLE);
        setTitleImageVisibility(View.GONE);

        View view = setContent(R.layout.layout_my_center);
        studentNumber = (TextView) view.findViewById(R.id.tv_my_center_top_part_student_number);
        allIntegral = (TextView) view.findViewById(R.id.tv_my_center_middle_part_all_integral);
        roundedImageView = (RoundedImageView) view.findViewById(R.id.tv_my_center_top_part_head_portrait);
        roundedImageView.setOnClickListener(this);

        setUserHeadImage();

        requestStudentNumberAndIntegral();

        initBottomView();


    }

    /*根据文件夹中是否有图片来显示头像*/
    private void setUserHeadImage() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/library/image/user_head_image.jpg");
        if (file.exists()) {
            Bitmap bit = BitmapFactory.decodeFile(file.getPath()); //自定义//路径
            roundedImageView.setImageBitmap(bit);
        } else {
            roundedImageView.setImageResource(R.mipmap.text_people);
        }
    }

    private void initBottomView() {
        RelativeLayout myInformation = (RelativeLayout) findViewById(R.id.my_center_bottom_part_normal_my_information);
        myInformation.setOnClickListener(this);
        RelativeLayout meMessage = (RelativeLayout) findViewById(R.id.my_center_bottom_part_normal_me_message);
        meMessage.setOnClickListener(this);
        RelativeLayout myIntegral = (RelativeLayout) findViewById(R.id.my_center_bottom_part_normal_my_integral);
        myIntegral.setOnClickListener(this);
        RelativeLayout myOrder = (RelativeLayout) findViewById(R.id.my_center_bottom_part_normal_my_order);
        myOrder.setOnClickListener(this);
        RelativeLayout myOrderVoucher = (RelativeLayout) findViewById(R.id.my_center_bottom_part_normal_my_order_voucher);
        myOrderVoucher.setOnClickListener(this);
    }

    private void requestStudentNumberAndIntegral() {
        String mUserId = SharedPreferencesUtil.getString(this, StringsFiled.USERID, "");
        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody meInformationFormBody = new FormBody.Builder()
                .add("userid", mUserId).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request meInformationRequest = new Request.Builder()
                .url(address + IpFiled.MY_CENTER_INFORMATION_ONE)
                .post(meInformationFormBody)
                .build();

        RequestBody allPointFormBody = new FormBody.Builder()
                .add("userid", mUserId).build();
        Request allPointRequest = new Request.Builder()
                .url(address + IpFiled.MY_CENTER_INFORMATION_TWO)
                .post(allPointFormBody)
                .build();
        okHttpClient.newCall(meInformationRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("meInformationRequest---onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultString = response.body().string();
                System.out.println(resultString);
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(resultString);
                            studentNumber.setText("学号 : " + object.optString("studentId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        okHttpClient.newCall(allPointRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("allPointRequest---onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultString = response.body().string();
                System.out.println(resultString);
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(resultString);
                            allIntegral.setText(object.optInt("myPoint") + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void skipFourActivity(String title) {
        Intent intent = new Intent(ViewsUitls.getContext(), IntegralAndOrderOrStatementActivity.class);
        intent.putExtra(StringsFiled.ACTIVITY_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.my_center_bottom_part_normal_my_information:
                skipFourActivity("我的资料");
                break;
            case R.id.my_center_bottom_part_normal_me_message:
                skipFourActivity("系统消息");
                break;
            case R.id.my_center_bottom_part_normal_my_integral:
                skipFourActivity("积分明细");
                break;
            case R.id.my_center_bottom_part_normal_my_order:
                skipFourActivity("我的预约");
                break;
            case R.id.my_center_bottom_part_normal_my_order_voucher:
                skipFourActivity("预约凭证");
                break;
            case R.id.tv_my_center_top_part_head_portrait:
                selectImageDialog();
                break;
            case R.id.btn_open_camera:// 跳转相机
                imageDir = "temp.jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(), imageDir)));
                startActivityForResult(intent, TAKE_PHOTO);
                break;
            case R.id.btn_choose_img:// 跳转图库
                Intent toChooseImage = new Intent(Intent.ACTION_GET_CONTENT);
                toChooseImage.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(toChooseImage, null);
                startActivityForResult(wrapperIntent, PHOTO_ZOOM);
                break;
            case R.id.btn_cancel:
                mCameraDialog.cancel();
                break;
        }
    }


    private void selectImageDialog() {
        mCameraDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.layout_camera_control, null);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    public final static int PHOTO_ZOOM = 0;
    public final static int TAKE_PHOTO = 1;
    public final static int PHOTO_RESULT = 2;
    public static final String IMAGE_UNSPECIFIED = "image/*";

    // 图片缩放
    public void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高

        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }

    // 在onActivityResult对返回的图片进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_ZOOM) {
                // 从图库获取了图片后在次跳转到缩放页面进行图片处理
                photoZoom(data.getData());
            }
            if (requestCode == TAKE_PHOTO) {
                // 从相机获取了图片后在次跳转到缩放页面进行图片处理
                File picture = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + imageDir);
                photoZoom(Uri.fromFile(picture));
            }

            if (requestCode == PHOTO_RESULT) {
                // 缩放图片处理页面跳回时调用
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // 这是处理后的图片Bitmap
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // 对图片进行压缩处理
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);

                    File file = Environment.getExternalStorageDirectory();
                    ACache aCache = ACache.get(new File(file, "library/image"));
                    aCache.put("user_head_image.jpg", photo);

                    roundedImageView.setImageBitmap(photo);
                    mCameraDialog.cancel();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
