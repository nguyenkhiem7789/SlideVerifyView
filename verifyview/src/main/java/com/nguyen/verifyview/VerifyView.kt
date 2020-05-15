package com.nguyen.verifyview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

interface VerifyCheckedListener {
    fun onChecked()
}

class VerifyView : View {

    companion object {
        const val DEFAULT_BACKGROUND_COLOR = Color.LTGRAY

        const val DEFAULT_STROKE_COLOR = Color.DKGRAY

        const val DEFAULT_STROKE_WIDTH = 1

        const val DEFAULT_TEXT_COLOR = Color.WHITE

        const val DEFAULT_TEXT_SIZE = 24

        const val DEFAULT_RADIUS_IMAGE = 15
    }

    private var bgColor = DEFAULT_BACKGROUND_COLOR

    private var strokeColor = DEFAULT_STROKE_COLOR

    private var strokeWidth = DEFAULT_STROKE_WIDTH

    private var progressColor = ContextCompat.getColor(context, R.color.default_progress)

    private var textColor = DEFAULT_TEXT_COLOR

    private var textSize = DEFAULT_TEXT_SIZE

    private var imageThumb = R.drawable.thumb

    private var imageFinish = R.drawable.checked

    private var radiusImageThumb = DEFAULT_RADIUS_IMAGE

    private var borderBgRect: RectF? = null

    private var bgRect: RectF? = null

    private var thumbRect: RectF? = null

    private var finishRect: RectF? = null

    private var progressRect: RectF? = null

    private var editable: Boolean = false

    private var text: String? = resources.getString(R.string.default_text)

    private var xTouch: Float = 0.0F
        set(value) {
            field = value
            invalidate()
        }

    private val bgPaint: Paint by lazy {
        Paint()
    }

    private val thumbPaint: Paint by lazy {
        Paint()
    }

    private val finishPaint: Paint by lazy {
        Paint()
    }

    private val progressPaint: Paint by lazy {
        Paint()
    }

    private val textPaint: Paint by lazy {
        Paint()
    }

    private var thumbImageBitmap: Bitmap? = null

    private var thumbImageRect: RectF? = null

    private val thumbImagePaint: Paint by lazy {
        Paint(Paint.FILTER_BITMAP_FLAG)
    }

    private var finishImageBitmap: Bitmap? = null

    private var finishImageRect: RectF? = null

    private val finishImagePaint: Paint by lazy {
        Paint(Paint.FILTER_BITMAP_FLAG)
    }

    private val borderBgPaint: Paint by lazy {
        Paint()
    }

    var listener: VerifyCheckedListener? = null

    fun setVerifyCheckedListener(listener: VerifyCheckedListener) {
        this.listener = listener
    }

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        var typeArray = context.obtainStyledAttributes(attrs, R.styleable.VerifyView)
        bgColor = typeArray.getColor(R.styleable.VerifyView_verify_bg_color, Color.parseColor("#dbdbdb"))
        strokeColor = typeArray.getColor(R.styleable.VerifyView_verify_stroke_color, Color.parseColor("#000000"))
        strokeWidth = typeArray.getDimensionPixelSize(R.styleable.VerifyView_verify_stroke_width, DEFAULT_STROKE_WIDTH)
        textColor = typeArray.getColor(R.styleable.VerifyView_verify_text_color, Color.parseColor("#ffffff"))
        textSize = typeArray.getDimensionPixelSize(R.styleable.VerifyView_verify_text_size, DEFAULT_TEXT_SIZE)
        progressColor = typeArray.getColor(R.styleable.VerifyView_verify_progress_color, Color.parseColor("#2f7cd3"))
        imageThumb = typeArray.getResourceId(R.styleable.VerifyView_verify_image_thumb, R.drawable.thumb)
        imageFinish = typeArray.getResourceId(R.styleable.VerifyView_verify_image_finish, R.drawable.checked)
        text = typeArray.getString(R.styleable.VerifyView_verify_text)
        radiusImageThumb = typeArray.getDimensionPixelSize(R.styleable.VerifyView_verify_radius_image_thumb, DEFAULT_RADIUS_IMAGE)
        typeArray.recycle()
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

        bgRect = RectF(
            strokeWidth.toFloat(), strokeWidth.toFloat(), width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth
        )
        borderBgRect = RectF(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth
        )

        // thumb
        thumbImagePaint.isFilterBitmap = true
        thumbImageBitmap = BitmapFactory.decodeResource(resources, imageThumb)

        thumbPaint.isAntiAlias = true
        thumbPaint.color = Color.WHITE

        thumbRect = RectF(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            height.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth
        )
        thumbImageRect = RectF(
            thumbRect!!.width() / 2 - radiusImageThumb,
            thumbRect!!.height() / 2 - radiusImageThumb,
            thumbRect!!.width() / 2 + radiusImageThumb,
            thumbRect!!.height() / 2 + radiusImageThumb
        )

        // finish
        finishImagePaint.isFilterBitmap = true
        finishImagePaint.color = Color.TRANSPARENT
        finishImageBitmap = BitmapFactory.decodeResource(resources, imageFinish)

        finishPaint.isAntiAlias = true
        finishPaint.color = Color.TRANSPARENT

        finishRect = RectF(
            width - height - strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth
        )
        finishImageRect = RectF(
            width - height / 2 - strokeWidth.toFloat() - radiusImageThumb,
            height / 2.toFloat() - radiusImageThumb,
            width - height / 2 - strokeWidth.toFloat() + radiusImageThumb,
            height / 2.toFloat() + radiusImageThumb
        )

        // progress
        progressPaint.isAntiAlias = true
        progressPaint.color = progressColor

        progressRect = RectF(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            height/2.toFloat(),
            height.toFloat() - strokeWidth.toFloat()
        )

        // text
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = textSize.toFloat()
        textPaint.color = textColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = 0
        var height = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else if(widthMode == MeasureSpec.AT_MOST) {
            width = widthSize
        } else {
            width = 0
        }

        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else if(heightMode == MeasureSpec.AT_MOST) {
            height = heightSize
        } else {
            height = 0
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bgRect!!.set(
            strokeWidth.toFloat(), strokeWidth.toFloat(), width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth
        )

        borderBgRect!!.set(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth
        )

        finishRect!!.set(
            width - height - strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            width.toFloat() - strokeWidth,
            height.toFloat() - strokeWidth
        )

        finishImageRect!!.set(
            width - height / 2 - strokeWidth.toFloat() - radiusImageThumb,
            height / 2.toFloat() - radiusImageThumb,
            width - height / 2 - strokeWidth.toFloat() + radiusImageThumb,
            height / 2.toFloat() + radiusImageThumb
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        ///draw background
        canvas.drawRoundRect(borderBgRect!!, 8F, 8F, borderBgPaint)
        canvas.drawRoundRect(bgRect!!, 8F, 8F, bgPaint)
        progressRect!!.set(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            if(height/2.toFloat() + xTouch < width) height/2.toFloat() + xTouch else width - strokeWidth.toFloat(),
            height.toFloat() - strokeWidth.toFloat())
        canvas.drawRoundRect(progressRect!!, 8F, 8F, progressPaint)
        thumbRect!!.set(
            strokeWidth.toFloat() + xTouch,
            strokeWidth.toFloat(),
            height.toFloat() - strokeWidth + xTouch,
            height.toFloat() - strokeWidth
        )
        thumbImageRect!!.set(
            thumbRect!!.width() / 2 - radiusImageThumb + xTouch,
            thumbRect!!.height() / 2 - radiusImageThumb,
            thumbRect!!.width() / 2 + radiusImageThumb + xTouch,
            thumbRect!!.height() / 2 + radiusImageThumb
        )
        canvas.drawRoundRect(thumbRect!!, 8F, 8F, thumbPaint)
        canvas.drawBitmap(thumbImageBitmap!!, null, thumbImageRect!!, thumbImagePaint)
        canvas.drawRoundRect(finishRect!!, 8F, 8F, finishPaint)
        canvas.drawBitmap(finishImageBitmap!!, null, finishImageRect!!, finishImagePaint)

        val textHeight = textPaint.descent() - textPaint.ascent()
        val textOffset = textHeight / 2 - textPaint.descent()
        canvas.drawText(text!!, bgRect!!.centerX(), bgRect!!.centerY() + textOffset, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (checkInsideThumbnail(event.x, event.y)) {
                    editable = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (editable) {
                    xTouch = event.x
                    if(checkInsideFinish(xTouch)) {
                        editable = false
                        thumbPaint.color = Color.TRANSPARENT
                        thumbImagePaint.color = Color.TRANSPARENT
                        finishPaint.color = Color.WHITE
                        finishImagePaint.color = Color.WHITE
                        this.listener?.onChecked()
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (editable) {
                    xTouch = 0F
                }
            }
        }
        return true
    }

    private fun checkInsideThumbnail(x: Float, y: Float): Boolean {
        return (0 < x && x < thumbRect!!.width() && 0 < y && y < thumbRect!!.height())
    }

    private fun checkInsideFinish(x: Float): Boolean {
        return (width - height < x)
    }
}