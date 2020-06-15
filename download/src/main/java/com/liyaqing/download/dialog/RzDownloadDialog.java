package com.liyaqing.download.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyaqing.download.R;
import com.liyaqing.download.RzDownLoadManager;
import com.liyaqing.download.widget.FlikerProgressBar;

public class RzDownloadDialog extends Dialog implements  DialogInterface.OnDismissListener {
//    private TextView tvProgress;
//    private ProgressBar progressBar;
    //    private TextView tvCheckBackground;
    private ImageView ivBack;
    private TextView tvTitle, tvText;
    private ImageView ivIcon;
    private String url;
    private boolean isPlaying = true;
    private FlikerProgressBar flikerProgressBar;
    private String icon,title;

    public RzDownloadDialog(Context context) {
        this(context, R.style.QDownAlphaDialog);
    }

    public RzDownloadDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.setOwnerActivity((Activity) context);
        this.setCanceledOnTouchOutside(false);
        this.initViews();
    }

    private void initViews() {
        View root = LayoutInflater.from(this.getContext()).inflate(R.layout.rz_download_dialog, (ViewGroup) null);
        this.setContentView(root);
        this.flikerProgressBar = (FlikerProgressBar) root.findViewById(R.id.round_flikerbar);

        this.setOnDismissListener(this);
        this.tvTitle = (TextView) root.findViewById(R.id.tv_title);
        this.tvText = (TextView) root.findViewById(R.id.tv_text);
        this.ivIcon = (ImageView) root.findViewById(R.id.iv_icon);
        this.ivBack = (ImageView) root.findViewById(R.id.iv_close);
        String str = "请务必允许\"趣头条\"安装应用";
        tvText.setText(str);
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                flikerProgressBar.setStop(false);
                RzDownLoadManager.get().pauseDownload(url);
                RzDownLoadManager.get().dialog=null;
            }
        });
        flikerProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flikerProgressBar.isFinish()){
                    flikerProgressBar.toggle();

                    if(flikerProgressBar.isStop()){
                        RzDownLoadManager.get().pauseDownload(url);
                    } else {
                        RzDownLoadManager.get().downloadAndInstallApk(url, title,icon,getOwnerActivity());
                    }

                }
            }
        });
    }

    public void setIcon(String icon) {
//        this.ivIcon.noDefaultLoadImage().setImage(icon);
        this.icon=icon;
    }

    public void setTitle(String title) {
        this.tvTitle.setText(title);
        this.title=title;
    }



    public void setProgress(int progress) {
        if (this.flikerProgressBar != null) {
            progress = progress >= 0 ? progress : 0;
            progress = progress <= 100 ? progress : 100;
            this.flikerProgressBar.setProgress(progress);
        }

    }

//    @Override
//    public boolean canShow() {
//        Activity activity = this.getOwnerActivity();
//        if (activity == null) {
//            return false;
//        } else {
//            return !activity.isFinishing() && (Build.VERSION.SDK_INT < 17 || !activity.isDestroyed());
//        }
//    }

    @Override
    public void show() {
        super.show();
        flikerProgressBar.setStop(false);
//        QDown.resumeTask(id);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

//    @Override
//    public void registerDismissListener(OnDismissListener listener) {
//        if (listener != null) {
//            Observerable.add(listener);
//        }
//    }
//
//    @Override
//    public void unRegisterDismissListener(OnDismissListener listener) {
//        if (listener != null) {
//            Observerable.remove(listener);
//        }
//    }
//
    @Override
    public void onDismiss(DialogInterface dialog) {
//        for (int i = 0; i < Observerable.size(); ++i) {
//            ((OnDismissListener) Observerable.get(i)).onDismiss(dialog);
//        }
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
