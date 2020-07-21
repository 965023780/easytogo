package com.example.easytogo.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView

class CircleView: ImageView{
    companion object{
        private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG);
    }

    constructor(context: Context?,  attributeSet: AttributeSet?):this(context,attributeSet,0){
    }

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(context,attributeSet,i){
    }


    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (null != drawable) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val b = getCircleBitmap(bitmap, 14)
            val rectSrc =
                Rect(0, 0, b.width, b.height)
            val rectDest =
                Rect(0, 0, width, height)
            mPaint.reset()
            canvas.drawBitmap(b, rectSrc, rectDest, mPaint)
        } else {
            super.onDraw(canvas)
        }
    }

    private fun getCircleBitmap(bitmap: Bitmap, pixels: Int): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        mPaint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        mPaint.setColor(color)
        val x = bitmap.width
        canvas.drawCircle(x / 2.toFloat(), x / 2.toFloat(), x / 2.toFloat(), mPaint)
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, mPaint)
        return output
    }
}