package com.example.mypathrecorder.presentation.cropimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import coil.ImageLoader
import coil.load
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import com.example.mypathrecorder.R
import com.example.mypathrecorder.presentation.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CropImageView(private val context:Context, attrs: AttributeSet) : ConstraintLayout(context,attrs) {
    private var bitmap: Bitmap? = null
    private val min = dpToPx(context,50f)
    private val max = dpToPx(context, 150f)
    private var cropRect: RectF = RectF(min,min,max,max)
    private var curX = 0f
    private var curY = 0f
    private var case:Array<Direction?> = arrayOf(null,null,null) //LR,TB,Inside
    var ratio:Ratio = Ratio.RatioFree
    private val touchBorderSize = 50f
    private val rect = Paint().apply {
        color = context.getColor(R.color.black)
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(context,2f)
    }
    private val iv: ImageView by lazy {
        ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
    private val minHeight = dpToPx(context,50f)
    private val minWidth = dpToPx(context, 50f)

    init {
        addView(iv)
    }

    suspend fun loadImageFromUri(uri: String){
        ImageLoader(context).execute(
            ImageRequest.Builder(context)
                .data(uri)
                .size(Size.ORIGINAL)
                .build()
        ).let {
            if(it is SuccessResult){
                bitmap = (it.drawable as? BitmapDrawable)?.bitmap
                iv.setImageBitmap(bitmap)
                invalidate()
            }
        }
    }

    private fun resize(direction:Direction, offset:Float, x:Float? =null, y:Float? =null){
        when(direction){
            Direction.Top -> {
                val new = cropRect.top + offset
                val newHeight = cropRect.height() - offset
                if(newHeight in minHeight..iv.height.toFloat()){
                    cropRect.top = new
                }
            }
            Direction.Bottom -> {
                val new = cropRect.bottom + offset
                val newHeight = cropRect.height() + offset
                if(newHeight in minHeight..iv.height.toFloat()){
                    cropRect.bottom = new
                }
            }
            Direction.Left -> {
                val new = cropRect.left + offset
                val newWidth = cropRect.width() - offset
                if(newWidth in minWidth..iv.width.toFloat()){
                    cropRect.left = new
                }
            }
            Direction.Right -> {
                val new = cropRect.right + offset
                val newWidth = cropRect.width() + offset
                if(newWidth in minWidth..iv.width.toFloat()){
                    cropRect.right = new
                }
            }
            else -> null
        }
        if(x!=null && y!=null){
            curX = x
            curY = y
        }
    }

    fun CropBitmap():Result<Bitmap>?{
        bitmap?.let {
            val width = iv.width
            val height = iv.height

            return runCatching {
                Bitmap.createBitmap(it,
                    (cropRect.left*it.width/width).toInt(),
                    (cropRect.top*it.height/height).toInt(),
                    (cropRect.width()*it.width/width).toInt(),
                    (cropRect.height()*it.height/height).toInt()
                    )
            }
        }
        return null
    }

    fun fixRatio(){
        var left=min
        var top=min
        var width= dpToPx(context,100f)
        val height = when(ratio){
            Ratio.RatioFree -> width
            Ratio.Ratio1to1 -> width
            Ratio.Ratio3to4 -> width*4/3
            Ratio.Ratio4to3 -> width*3/4
            Ratio.Ratio9to16 -> width*16/9
            Ratio.Ratio16to9 -> width*9/16
            Ratio.RatioFull -> iv.height.toFloat()
        }
        if(ratio == Ratio.RatioFull) {
            left=0f
            top=0f
            width =iv.width.toFloat()
            ratio = Ratio.RatioFree
        }
        cropRect = RectF(left,top,left+width,top+height)
        invalidate()
    }

    private fun resizeWithRatio(){
        val ratioF:Float? = when(ratio){
            Ratio.RatioFree -> null
            Ratio.Ratio1to1 -> 1f
            Ratio.Ratio3to4 -> 4f/3f
            Ratio.Ratio4to3 -> 3f/4f
            Ratio.Ratio9to16 -> 16f/9f
            Ratio.Ratio16to9 -> 9f/16f
            Ratio.RatioFull -> null
        }
        if(ratioF==null) return

        if(case[0]!=null){
            cropRect.bottom=minOf(cropRect.top + cropRect.width()*ratioF!!,iv.height.toFloat())
        }else if(case[1]!=null){
            cropRect.right=minOf(cropRect.left + cropRect.height()/ratioF!!,iv.width.toFloat())
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawRect(cropRect, rect)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action){
                MotionEvent.ACTION_DOWN -> {
                    curX = it.x
                    curY = it.y
                    case[0]=if(curX in cropRect.left..cropRect.left + touchBorderSize){
                        Direction.Left
                    }else if(curX in cropRect.right - touchBorderSize..cropRect.right){
                        Direction.Right
                    }else{
                        null
                    }
                    case[1] = if(curY in cropRect.top..cropRect.top+touchBorderSize){
                        Direction.Top
                    }else if(curY in cropRect.bottom - touchBorderSize..cropRect.bottom){
                        Direction.Bottom
                    }else{
                        null
                    }
                    case[2] = if(case[0]==null&&case[1]==null&&cropRect.contains(curX,curY)){
                        Direction.Inside
                    }else{
                        null
                    }
                    if (cropRect.contains(it.x, it.y)) return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val offX = it.x - curX
                    val offY = it.y - curY
                    if(case[0]==Direction.Left){
                        resize(Direction.Left,offX,it.x,it.y)
                    }else if(case[0]==Direction.Right){
                        resize(Direction.Right,offX,it.x,it.y)
                    }
                    if(case[1]==Direction.Top){
                        resize(Direction.Top,offY,it.x,it.y)
                    }else if(case[1]==Direction.Bottom){
                        resize(Direction.Bottom,offY,it.x,it.y)
                    }
                    resizeWithRatio()
                    invalidate()
                    if(case[2]==Direction.Inside){
                        val maxX = minOf(cropRect.right+offX,iv.width.toFloat())
                        val minX = maxOf(cropRect.left+offX,0f)
                        val maxY = minOf(cropRect.bottom+offY,iv.height.toFloat())
                        val minY = maxOf(cropRect.top+offY,0f)

                        val adjustLeft = if(offX>0) maxX - cropRect.width() else minX
                        val adjustTop = if(offY>0) maxY - cropRect.height() else minY

                        cropRect.offsetTo(adjustLeft,adjustTop)
                        invalidate()
                        curX = it.x
                        curY = it.y
                    }
                    return true
                }
                MotionEvent.ACTION_UP ->{
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
}

fun dpToPx(context: Context, dp:Float): Float{
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}

enum class Direction{
    Top,
    Bottom,
    Left,
    Right,
    Inside
}

enum class Ratio(val idx:Int){
    RatioFree(0),
    Ratio1to1(1),
    Ratio3to4(2),
    Ratio4to3(3),
    Ratio9to16(4),
    Ratio16to9(5),
    RatioFull(6)

}