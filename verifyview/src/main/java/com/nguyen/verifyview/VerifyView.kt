package com.nguyen.verifyview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class VerifyView : View {

    companion object {
        const val DEFAULT_BACKGROUND_COLOR = Color.LTGRAY

        const val DEFAULT_STROKE_COLOR = Color.DKGRAY

        const val DEFAULT_STROKE_WIDTH = 1
    }

    private var bgColor = DEFAULT_BACKGROUND_COLOR

    private var strokeColor = DEFAULT_STROKE_COLOR

    private var strokeWidth = DEFAULT_STROKE_WIDTH

    private var borderBgRect: RectF? = null

    private var bgRect: RectF? = null

    private var thumbRect: RectF? = null

    private val bgPaint: Paint by lazy {
        Paint()
    }

    private val thumbPaint: Paint by lazy {
        Paint()
    }

    private var thumbImageBitmap: Bitmap? = null

    private var thumbImageRect: RectF? = null

    private val thumbImagePaint: Paint by lazy {
        Paint(Paint.FILTER_BITMAP_FLAG)
    }

    private val borderBgPaint: Paint by lazy {
        Paint()
    }

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun init() {
        // background paint
        bgPaint.isAntiAlias = true
        bgPaint.color = bgColor
        borderBgPaint.isAntiAlias = true
        borderBgPaint.color = strokeColor
        borderBgPaint.strokeWidth = strokeWidth.toFloat()
        borderBgPaint.style = Paint.Style.STROKE

        // load bitmap
        thumbImagePaint.isAntiAlias = true
        thumbImagePaint.isFilterBitmap = true
        thumbImagePaint.isDither = true
        thumbImageBitmap = BitmapFactory.decodeResource(resources, R.drawable.thumb)

        // thumb paint
        thumbPaint.isAntiAlias = true
        thumbPaint.color = Color.WHITE

        // background rect
        bgRect = RectF(strokeWidth.toFloat(), strokeWidth.toFloat(), width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth)
        borderBgRect = RectF(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth)

        /// thumbRect
        thumbRect = RectF(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            height.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth)
        thumbImageRect = RectF(thumbRect!!.width()/2 - 12, thumbRect!!.height()/2 - 12, 24F, 24F)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        ///draw background
        bgRect!!.set(strokeWidth.toFloat(), strokeWidth.toFloat(), width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth)
        borderBgRect!!.set(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth)


        canvas.drawRoundRect(borderBgRect!!, 8F, 8F, borderBgPaint)
        canvas.drawRoundRect(bgRect!!, 8F, 8F, bgPaint)

        thumbRect!!.set(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            height.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth)
        canvas.drawRoundRect(thumbRect!!, 8F, 8F, thumbPaint)

        thumbImageRect!!.set(thumbRect!!.width()/2 - 10, thumbRect!!.height()/2 - 10, thumbRect!!.width()/2 + 10, thumbRect!!.height()/2 + 10)
        canvas.drawBitmap(thumbImageBitmap!!, null, thumbImageRect!!, thumbImagePaint)
    }
}