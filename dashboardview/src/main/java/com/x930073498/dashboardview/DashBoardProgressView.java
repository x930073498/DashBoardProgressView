package com.x930073498.dashboardview;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class DashBoardProgressView extends View {
    private static final String TAG = "ProgressView";
    //背景刻度的画笔
    private Paint basePaint;
    //进度文字的画笔
    private Paint mProgressTextPaint;
    //文字描述的画笔
    private Paint mDescriptionPaint;
    //进度刻度的画笔
    private Paint mProgressPaint;
    //固定刻度的画笔
    private Paint dashPaint;
    //最大进度值
    private float max = 100;
    //进度
    private float progress = 0;


    //进度开始的角度
    private float angleStart = 120;

    //进度结束的角度
    private float angleEnd = 420;
    //进度刻度短线的长度
    private float shortProgressLength = 60;
    //进度刻度上线的长度
    private float longProgressLength = 80;

    //背景刻度短线的长度
    private float shortBaseLength = 60;
    //背景刻度长线的长度
    private float longBaseLength = 80;
    //固定刻度短线长度
    private float shortDashLength = 10;
    //固定刻度长线长度
    private float longDashLength = 20;

    //背景刻度的颜色
    private int colorBasePaint = Color.BLUE;
    //进度文字的颜色
    private int colorProgressText = Color.WHITE;
    //描述文字的颜色
    private int colorDescription = Color.WHITE;
    //进度颜色
    private int colorProgress = Color.WHITE;
    //固定刻度的颜色
    private int colorDash = Color.WHITE;
    //背景颜色
    private int colorBackground = Color.TRANSPARENT;

    //背景刻度的宽度
    private float basePaintWidth = 5;
    //进度文字的线条宽度
    private float progressTextWidth = 40;

    //进度文字的大小
    private float progressTextSize = sp2px(40);

    //描述文字的线条宽度
    private float descriptionTextWidth = 20;
    //描述文字的大小
    private float descriptionTextSize = sp2px(18);
    //进度刻度的宽度
    private float progressWidth = 10;
    //固定刻度的宽度
    private float dashWidth = 6;
    // 进度刻度的半径
    private float progressRadius = 300;
    //固定刻度的半径
    private float dashRadius = 270;

    //固定刻度与进度刻度之间的间隙距离

    private float dashMargin = 30;
    //中心点
    private PointF mCenter = new PointF();

    //progress刻度总数
    private int numberProgress = 47;
    //progress刻度中间位置
    private int centerIndex;
    //progress刻度的角度间隔
    private double unitProgressAngle;
    //progress 对应的刻度位置
    private int progressIndex;
    //固定刻度总数
    private int numberDash = 59;
    //固定刻度间隔的角度
    private double unitDashAngle;
    //progressText与progressDescription之间的间距
    private float textMargin = 20;

    //进度文字
    private CharSequence progressText;
    //进度文字描述
    private CharSequence description;

    private Paint.FontMetrics progressMetrics;
    private Paint.FontMetrics descriptionMetrics;

    private DescriptionProvider descriptionProvider;

    private ProgressTextProvider progressTextProvider;

    private BackgroundColorProvider backgroundColorProvider;
    private int mPadding;

    private ArrayMap<Float, Integer> colors = new ArrayMap<>();

    public DashBoardProgressView(Context context) {
        this(context, null);
    }

    public DashBoardProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashBoardProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DashBoardProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initAttrs(context, attrs, defStyleAttr);
        initPaint();
        initData();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashBoardProgressView);
        progress = typedArray.getFloat(R.styleable.DashBoardProgressView_progress, this.progress);
        max = typedArray.getFloat(R.styleable.DashBoardProgressView_max, this.max);
        angleStart = typedArray.getFloat(R.styleable.DashBoardProgressView_startAngle, this.angleStart);
        angleEnd = typedArray.getFloat(R.styleable.DashBoardProgressView_endAngle, this.angleEnd);
        dashMargin = typedArray.getDimension(R.styleable.DashBoardProgressView_dashMargin, dashMargin);
        textMargin = typedArray.getDimension(R.styleable.DashBoardProgressView_textMargin, textMargin);
        colorProgress = typedArray.getColor(R.styleable.DashBoardProgressView_progressColor, colorProgress);
        colorBasePaint = typedArray.getColor(R.styleable.DashBoardProgressView_baseColor, colorBasePaint);
        colorDash = typedArray.getColor(R.styleable.DashBoardProgressView_dashColor, colorDash);
        progressText = typedArray.getText(R.styleable.DashBoardProgressView_progressText);
        description = typedArray.getText(R.styleable.DashBoardProgressView_description);
        progressTextSize = typedArray.getDimension(R.styleable.DashBoardProgressView_progressTextSize, progressTextSize);
        descriptionTextSize = typedArray.getDimension(R.styleable.DashBoardProgressView_descriptionTextSize, descriptionTextSize);
        progressWidth = typedArray.getDimension(R.styleable.DashBoardProgressView_progressLineWidth, progressWidth);
        dashWidth = typedArray.getDimension(R.styleable.DashBoardProgressView_dashLineWidth, dashWidth);
        basePaintWidth = typedArray.getDimension(R.styleable.DashBoardProgressView_baseLineWidth, basePaintWidth);
        shortProgressLength = typedArray.getDimension(R.styleable.DashBoardProgressView_progressShortLength, shortProgressLength);
        longProgressLength = typedArray.getDimension(R.styleable.DashBoardProgressView_progressLongLength, longProgressLength);
        shortBaseLength = typedArray.getDimension(R.styleable.DashBoardProgressView_baseShortLength, shortBaseLength);
        longBaseLength = typedArray.getDimension(R.styleable.DashBoardProgressView_baseLongLength, longBaseLength);
        shortDashLength = typedArray.getDimension(R.styleable.DashBoardProgressView_dashShortLength, shortDashLength);
        longDashLength = typedArray.getDimension(R.styleable.DashBoardProgressView_dashLongLength, longDashLength);
        colorDescription = typedArray.getColor(R.styleable.DashBoardProgressView_descriptionColor, colorDescription);
        colorProgressText = typedArray.getColor(R.styleable.DashBoardProgressView_progressTextColor, colorProgressText);
        colorBackground = typedArray.getColor(R.styleable.DashBoardProgressView_backgroundColor, colorBackground);
        typedArray.recycle();
        max = Math.abs(max);
        progress = Math.abs(progress);
        dashMargin = Math.abs(dashMargin);
        textMargin = Math.abs(textMargin);
    }

    private void initPaint() {
        mProgressPaint = createPaint(colorProgress, progressWidth);
        basePaint = createPaint(colorBasePaint, Math.min(basePaintWidth, progressWidth));
        mProgressTextPaint = createPaint(colorProgressText, progressTextWidth);
        mDescriptionPaint = createPaint(colorDescription, descriptionTextWidth);
        dashPaint = createPaint(colorDash, dashWidth);
    }

    private void initData() {
        calculateSize();
        calculateProgressNumber();
        calculateCenterProgressIndex();
        calculateProgressIndex();
        calculateProgressUnitAngle();
        calculateDashUnitAngle();
        calculateBackgroundColor();
        initText();
    }

    private void calculateSize() {
        calculateProgressData();
        calculateBaseData();
        calculateDashData();
    }

    private void calculateBaseData() {
        if (shortBaseLength > longBaseLength) {
            float temp = shortBaseLength;
            shortBaseLength = longBaseLength;
            longBaseLength = temp;
        }
        shortBaseLength = Math.min(shortBaseLength, shortProgressLength);
        longBaseLength = Math.min(longBaseLength, longProgressLength);

    }

    private void calculateProgressData() {
        if (shortProgressLength > longProgressLength) {
            float temp = shortProgressLength;
            shortProgressLength = longProgressLength;
            longProgressLength = temp;
        }
    }

    private void calculateDashData() {
        if (shortDashLength > longDashLength) {
            float temp = shortDashLength;
            shortDashLength = longDashLength;
            longDashLength = temp;
        }
    }


    private void initText() {
        progressMetrics = new Paint.FontMetrics();
        descriptionMetrics = new Paint.FontMetrics();
        mProgressTextPaint.setTextSize(progressTextSize);
        mProgressTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        mDescriptionPaint.setTextSize(descriptionTextSize);
        mDescriptionPaint.setTextAlign(Paint.Align.CENTER);
    }


    private Paint createPaint(int color, float width) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.BUTT);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getBackgroundColor(progress));
        drawProgress(canvas);
        drawDash(canvas);
        drawText(canvas);
    }

    private void drawDash(Canvas canvas) {
        float angle;
        for (int i = 0; i < numberDash; i++) {
            angle = (float) (angleStart + unitDashAngle * i);
            if (checkAngleDashLong(angle)) {
                drawArcInnerLine(canvas, mCenter, dashRadius, angle, longDashLength, dashPaint);
            } else {
                drawArcInnerLine(canvas, mCenter, dashRadius, angle, shortDashLength, dashPaint);
            }
        }

    }


    private void drawText(Canvas canvas) {
        CharSequence textProgress = getProgressText(progress);
        if (!TextUtils.isEmpty(textProgress)) {
            mProgressTextPaint.getFontMetrics(progressMetrics);
        }
        CharSequence description = getDescription(progress);
        if (!TextUtils.isEmpty(description)) {
            mDescriptionPaint.getFontMetrics(descriptionMetrics);
        }
        float progressTextHeight = Math.abs(progressMetrics.top) + Math.abs(progressMetrics.bottom);
        float descriptionHeight = Math.abs(descriptionMetrics.top) + Math.abs(descriptionMetrics.bottom);
        float heightTotal = descriptionHeight + progressTextHeight + textMargin;
        float progressTextStartY = mCenter.y - (heightTotal / 2) + Math.abs(progressMetrics.top);
        drawProgressText(canvas, mCenter.x, progressTextStartY);
        float descriptionTextStartY = mCenter.y + (heightTotal / 2) - Math.abs(descriptionMetrics.bottom);
        drawProgressDescription(canvas, mCenter.x, descriptionTextStartY);
    }


    private void drawProgressText(Canvas canvas, float x, float y) {
        CharSequence progressText = getProgressText(progress);
        if (TextUtils.isEmpty(progressText)) return;
        canvas.drawText(progressText, 0, progressText.length(), x, y, mProgressTextPaint);
    }

    private void drawProgressDescription(Canvas canvas, float x, float y) {
        CharSequence description = getDescription(progress);
        if (TextUtils.isEmpty(description)) return;
        canvas.drawText(description, 0, description.length(), x, y, mDescriptionPaint);
    }

    private void drawProgress(Canvas canvas) {
        for (int i = 0; i < numberProgress; i++) {
            if (i <= progressIndex && progress != 0) {
                if (i == centerIndex) {
                    drawArcOuterLine(canvas, mCenter, progressRadius, (float) (angleStart + i * unitProgressAngle), longProgressLength, mProgressPaint);
                } else
                    drawArcOuterLine(canvas, mCenter, progressRadius, (float) (angleStart + i * unitProgressAngle), shortProgressLength, mProgressPaint);
            } else {
                if (i == centerIndex) {
                    drawArcOuterLine(canvas, mCenter, progressRadius, (float) (angleStart + i * unitProgressAngle), longBaseLength, basePaint);
                } else
                    drawArcOuterLine(canvas, mCenter, progressRadius, (float) (angleStart + i * unitProgressAngle), shortBaseLength, basePaint);
            }
        }
    }

    private int calculateProgressIndex() {
        progressIndex = (int) (numberProgress * calculateProgress());
        return progressIndex;
    }

    private float calculateProgress() {
        float ratio = progress / max;
        return Math.min(ratio, 1);
    }

    public void setProgress(float progress) {
        this.progress = Math.abs(progress);
        calculateProgressIndex();
        postInvalidate();
    }

    public void setMax(float max) {
        this.max = Math.abs(max);
        calculateProgressIndex();
        postInvalidate();
    }

    public void setStartAngle(float angle) {
        this.angleStart = angle;
        calculateProgressUnitAngle();
        calculateDashUnitAngle();
        postInvalidate();
    }

    public void setEndAngle(float angle) {
        this.angleEnd = angle;
        calculateProgressUnitAngle();
        calculateDashUnitAngle();
        postInvalidate();
    }

    public void setDashMargin(float margin) {
        this.dashMargin = dp2px(margin);
        postInvalidate();
    }

    public void setTextMargin(float margin) {
        this.textMargin = dp2px(margin);
        postInvalidate();
    }

    public void setBaseColor(@ColorInt int color) {
        this.colorBasePaint = color;
        basePaint.setColor(color);
        postInvalidate();
    }

    public void setProgressColor(@ColorInt int color) {
        this.colorProgress = color;
        mProgressPaint.setColor(color);
        postInvalidate();
    }

    public void setDashColor(@ColorInt int color) {
        this.colorDash = color;
        dashPaint.setColor(color);
        postInvalidate();
    }

    public void setProgressText(CharSequence text) {
        this.progressText = text;
        postInvalidate();
    }

    public void setDescription(CharSequence text) {
        this.description = text;
        postInvalidate();
    }

    public void setProgressTextSize(float textSize) {
        this.progressTextSize = sp2px(textSize);
        mProgressTextPaint.setTextSize(progressTextSize);
        postInvalidate();
    }

    public void setDescriptionTextSize(int textSize) {
        this.descriptionTextSize = sp2px(textSize);
        mDescriptionPaint.setTextSize(descriptionTextSize);
        postInvalidate();
    }

    public void setProgressLineWidth(float width) {
        this.progressWidth = dp2px(width);
        mProgressPaint.setStrokeWidth(progressWidth);
        postInvalidate();
    }

    public void setDashLineWidth(float width) {
        this.dashWidth = dp2px(width);
        dashPaint.setStrokeWidth(dashWidth);
        postInvalidate();
    }

    public void setBaseLineWidth(float width) {
        this.basePaintWidth = dp2px(width);
        basePaint.setStrokeWidth(basePaintWidth);
        postInvalidate();
    }

    public void setProgressShortLength(float length) {
        this.shortProgressLength = dp2px(length);
        calculateProgressData();
        calculateRadius();
        postInvalidate();
    }

    public void setProgressLongLength(float length) {
        this.longProgressLength = dp2px(length);
        calculateProgressData();
        calculateRadius();
        postInvalidate();
    }

    public void setBaseShortLength(float length) {
        this.shortBaseLength = dp2px(length);
        calculateBaseData();
        postInvalidate();
    }

    public void setBaseLongLength(float length) {
        this.longBaseLength = dp2px(length);
        calculateBaseData();
        postInvalidate();
    }

    public void setDashShortLength(float length) {
        this.shortDashLength = dp2px(length);
        calculateDashData();
        postInvalidate();
    }

    public void setDashLongLength(float length) {
        this.longDashLength = dp2px(length);
        calculateDashData();
        postInvalidate();
    }

    public void setDescriptionColor(@ColorInt int color) {
        this.colorDescription = color;
        mDescriptionPaint.setColor(color);
        postInvalidate();
    }

    public void setProgressTextColor(@ColorInt int color) {
        this.colorProgressText = color;
        mProgressTextPaint.setColor(color);
        postInvalidate();
    }

    public void setBackgroundColor(@ColorInt int color) {
        this.colorBackground = color;
        postInvalidate();
    }

    public void setDescriptionProvider(DescriptionProvider descriptionProvider) {
        this.descriptionProvider = descriptionProvider;
    }


    public void setProgressTextProvider(ProgressTextProvider progressTextProvider) {
        this.progressTextProvider = progressTextProvider;
    }

    public void setBackgroundColorProvider(BackgroundColorProvider backgroundColorProvider) {
        this.backgroundColorProvider = backgroundColorProvider;
    }

    private int calculateProgressNumber() {
        if (numberProgress % 2 == 0) numberProgress++;
        return numberProgress;
    }

    private int calculateCenterProgressIndex() {
        return centerIndex = (numberProgress) / 2;
    }

    private double calculateProgressUnitAngle() {
        float angleSwipe = angleEnd - angleStart;
        if (Math.abs(angleSwipe) > 360) {
            angleSwipe = angleSwipe % 360;
        }
        return unitProgressAngle = angleSwipe * 1d / (numberProgress - 1);
    }

    private double calculateDashUnitAngle() {
        float angleSwipe = angleEnd - angleStart;
        if (Math.abs(angleSwipe) > 360) {
            angleSwipe = angleSwipe % 360;
        }
        return unitDashAngle = angleSwipe / (numberDash - 1);
    }

    private void calculateBackgroundColor() {
        colors.clear();
        if (backgroundColorProvider == null) return;
    }

    private void calculateRadius() {
        progressRadius = (getMeasuredWidth() - mPadding * 2 - longProgressLength * 2) / 2;
        dashRadius = progressRadius - dashMargin;
    }

    private boolean checkAngleDashLong(float angle) {
        double unit = Math.abs(unitDashAngle * 1d / 2);
        double upper = 90 - unit;
        float mode = Math.abs(angle % 90);
        return mode >= upper || mode <= unit;
    }

    private void drawArcOuterLine(Canvas canvas, PointF mCenter, float radius, float angle, float length, Paint paint) {
        if (mCenter == null) mCenter = new PointF();
        double arc = Math.PI * angle * 1d / 180;
        double sin = Math.sin(arc);
        double cos = Math.cos(arc);
        float x1 = (float) (cos * radius);
        float y1 = (float) (sin * radius);
        float x2 = (float) (cos * (radius + length));
        float y2 = (float) (sin * (radius + length));
        canvas.drawLine(mCenter.x + x1, mCenter.y + y1, mCenter.x + x2, mCenter.y + y2, paint);
    }

    private void drawArcInnerLine(Canvas canvas, PointF mCenter, float radius, float angle, float length, Paint paint) {
        if (mCenter == null) mCenter = new PointF();
        double arc = Math.PI * angle * 1d / 180;
        double sin = Math.sin(arc);
        double cos = Math.cos(arc);
        float x1 = (float) (cos * radius);
        float y1 = (float) (sin * radius);
        float x2 = (float) (cos * (radius - length));
        float y2 = (float) (sin * (radius - length));
        canvas.drawLine(mCenter.x + x1, mCenter.y + y1, mCenter.x + x2, mCenter.y + y2, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);
        int width = resolveSize(dp2px(220), widthMeasureSpec);
        setMeasuredDimension(width, width);
        progressRadius = (width - mPadding * 2 - longProgressLength * 2) / 2;
        dashRadius = progressRadius - dashMargin;
        mCenter.x = getMeasuredWidth() / 2;
        mCenter.y = getMeasuredHeight() / 2;
        calculateRadius();
    }

    private CharSequence getDescription(float progress) {
        ArgbEvaluator evaluator = new ArgbEvaluator();

        if (descriptionProvider != null) return descriptionProvider.provideDescription(progress);
        return description;
    }

    private CharSequence getProgressText(float progress) {
        if (progressTextProvider != null) return progressTextProvider.provideProgressText(progress);
        return TextUtils.isEmpty(progressText) ? String.valueOf(progress) : progressText;
    }

    private int getBackgroundColor(float progress) {
        if (backgroundColorProvider != null)
            return backgroundColorProvider.provideBackgroundColor(getContext(), progress);
        return colorBackground;
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public interface DescriptionProvider {
        CharSequence provideDescription(float progress);
    }

    public interface ProgressTextProvider {
        CharSequence provideProgressText(float progress);
    }

    public interface BackgroundColorProvider {
        @ColorInt
        int provideBackgroundColor(Context context, float progress);
    }

}
