package com.x930073498.dashboardprogressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.x930073498.dashboardview.DashBoardProgressView;

public class MainActivity extends AppCompatActivity implements DashBoardProgressView.DescriptionProvider, DashBoardProgressView.ProgressTextProvider, DashBoardProgressView.BackgroundColorProvider, DashBoardProgressView.LongProgressIndexProvider {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DashBoardProgressView progressView = findViewById(R.id.progressView);
        View view = findViewById(R.id.container);
        view.setOnClickListener((v) -> animate(progressView));
        progressView.setDescriptionProvider(this);
        progressView.setProgressTextProvider(this);
        progressView.setLongProgressIndexProvider(this);
//        progressView.setBackgroundColorProvider(this);
    }

    private void animate(DashBoardProgressView progressView) {

        ValueAnimator animator = ValueAnimator.ofFloat(0, 100);
        animator.addUpdateListener(animation -> {
            progressView.setProgress((Float) animation.getAnimatedValue());
        });
        animator.setDuration(3000);
        animator.start();

    }

    @Override
    public CharSequence provideDescription(float progress) {
        if (progress < 60) return "信用极差";
        if (progress < 70) return "信用良好";
        if (progress < 90) return "信用很好";
        if (progress <= 100) return "信用极好";
        return "";
    }

    @Override
    public CharSequence provideProgressText(float progress) {
        return String.valueOf((int) progress);
    }

    @Override
    public int provideBackgroundColor(Context context, float progress) {
        if (progress < 20) return ContextCompat.getColor(context, R.color.aqua);
        if (progress < 40) return ContextCompat.getColor(context, R.color.cyan);
        if (progress < 60) return ContextCompat.getColor(context, R.color.springgreen);
        if (progress < 80) return ContextCompat.getColor(context, R.color.lime);
        return ContextCompat.getColor(context, R.color.mediumspringgreen);
    }

    @Override
    public int provideIndex(float max, float progress, int numberProgress, int progressIndex) {
        return progressIndex;
    }
}
