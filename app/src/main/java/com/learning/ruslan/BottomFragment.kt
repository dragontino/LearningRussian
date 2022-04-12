package com.learning.ruslan

import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.learning.ruslan.databinding.BottomSheetBinding
import com.learning.ruslan.settings.SettingsViewModel

open class BottomFragment(private val activity: AppCompatActivity):
    BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomFragment.TAG"
    }

    private lateinit var binding: BottomSheetBinding

    protected lateinit var settings: SettingsViewModel
    private val headView = BottomHeading()
    private val viewBuffer = ArrayList<BottomView>()

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetBinding.inflate(layoutInflater, container, false)
        settings = SettingsViewModel.getInstance(activity)
        binding.root.backgroundTint = settings.backgroundColor

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (headView.headText.isNotEmpty())
            setHeading(headView)

        if (viewBuffer.isNotEmpty())
            viewBuffer.forEachIndexed { pos, bottomView ->
                bottomView.id = pos
                addView(bottomView)
            }
    }


    private fun TextView.setTypeface(fontPath: String, textStyle: Int? = null) {
        val tf = Typeface.createFromAsset(activity.assets, fontPath)
        if (textStyle == null)
            this.typeface = tf
        else
            setTypeface(tf, textStyle)
    }


    fun setHeading(
        headText: String,
        subtitleText: String = "",
        fontPath: String = "",
        fontStyle: Int = Typeface.NORMAL,
        headSize: Float = 20f,
        subtitleSize: Float = 17f,
    ) = headView.set(headText, subtitleText, fontPath, fontStyle, headSize, subtitleSize)


    private fun setHeading(headingView: BottomHeading) {
        val heading = binding.root.findViewById<LinearLayout>(R.id.bottom_sheet_heading)
        heading.show()
        binding.headerDivider.show()

        val head = heading.findViewById<TextView>(R.id.header_name)
        val subtitle = heading.findViewById<TextView>(R.id.header_subtitle)

        headingView.run {
            head.text = headText
            head.setTextColor(settings.fontColor)

            if (subtitleText.isEmpty())
                subtitle.hide()
            else {
                subtitle.text = subtitleText
                subtitle.setTextColor(settings.fontColor)
            }

            if (fontPath.isNotEmpty()) {
                for (view in arrayOf(head, subtitle))
                    view.setTypeface(fontPath, fontStyle)
            }

            head.textSize = headSize
            subtitle.textSize = subtitleSize
        }


    }


    fun addView(
        @DrawableRes image: Int?,
        @StringRes text: Int,
        fontPath: String = "",
        fontStyle: Int = Typeface.NORMAL,
        textSize: Float = 18f,
        listener: View.OnClickListener,
    ) {
        val view = BottomView(image, activity.getString(text), listener)
        view.fontPath = fontPath
        view.fontStyle = fontStyle
        view.textSize = textSize

        if (view !in viewBuffer)
            viewBuffer += view
    }


    fun addView(
        @DrawableRes image: Int,
        imageBound: ImageBounds,
        text: String,
        fontPath: String = "",
        fontStyle: Int = Typeface.NORMAL,
        listener: View.OnClickListener
    ) {
        val view = BottomView(image, text, listener)
        view.imageBound = imageBound
        view.fontPath = fontPath
        view.fontStyle = fontStyle

        if (view !in viewBuffer)
            viewBuffer += view
    }

    fun addView(
        text: String,
        fontPath: String = "",
        fontStyle: Int = Typeface.NORMAL,
        listener: View.OnClickListener,
    ) {
        val view = BottomView(null, text, listener)
        view.fontPath = fontPath
        view.fontStyle = fontStyle

        if (view !in viewBuffer)
            viewBuffer += view
    }


    fun addViews(images: IntArray? = null, strings: IntArray, listener: View.OnClickListener) {
        if (images != null && images.size != strings.size)
            return

        for (index in strings.indices)
            addView(images?.get(index), strings[index], listener = listener)
    }


    operator fun get(id: Int): Button =
        binding.root.findViewById(id)


    /**
     * Использовать только в паре с removeView(view: View)
     */
    protected fun addView(view: View) =
        binding.root.addView(view)

    protected fun removeView(view: View) =
        binding.root.removeView(view)


    open fun updateColors(@ColorInt highlightColor: Int = settings.highlightColor) {
        binding.root.backgroundTint = settings.backgroundColor

        for (id in viewBuffer.indices)
            this[id].updateColors(highlightColor)
    }


    fun show(fragmentManager: FragmentManager) =
        show(fragmentManager, TAG)





    @SuppressLint("InflateParams")
    private fun addView(view: BottomView) {

        val child = layoutInflater
            .inflate(view.itemViewId, null, false) as Button

        child.setDrawables(view)
        child.updateColors(activity.getColor(R.color.raspberry))

        child.text = view.text
        child.setOnClickListener(view.listener)
        child.textSize = view.textSize
        child.id = view.id
        if (view.fontPath.isNotEmpty())
        child.setTypeface(Typeface.createFromAsset(activity.assets, view.fontPath), view.fontStyle)

        addView(child)
    }

    private fun TextView.updateColors(@ColorInt highlightColor: Int = settings.highlightColor) {
        setTextColor(settings.fontColor)
        setCompoundDrawableColor(highlightColor)
    }

    private fun Button.setDrawables(
        left: Drawable?,
        right: Drawable?,
        top: Drawable? = null,
        bottom: Drawable? = null
    ) =
        setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)


    protected fun Button.setDrawables(view: BottomView? = null) {
        val v = if (view == null) {
            val index = id
            if (index > viewBuffer.size)
                return

            viewBuffer[index]
        }
        else view

        if (v.image == null) {
            removeDrawables()
            return
        }

        val drawableImage = context?.let { ContextCompat.getDrawable(it, v.image!!) }
        drawableImage?.bounds = Rect(0, 0, 0, 0)

        when (v.imageBound) {
            ImageBounds.LEFT -> setDrawables(drawableImage, null)
            ImageBounds.RIGHT -> setDrawables(null, drawableImage)
        }
    }

    fun Button.removeDrawables() =
        setDrawables(null, null)
}


data class BottomHeading(
    var headText: String,
    var subtitleText: String
) {
    constructor(): this("", "")

    fun set(
        headText: String,
        subtitleText: String,
        fontPath: String,
        fontStyle: Int,
        headSize: Float,
        subtitleSize: Float,
    ) {
        this.headText = headText
        this.subtitleText = subtitleText
        this.fontPath = fontPath
        this.fontStyle = fontStyle
        this.headSize = headSize
        this.subtitleSize = subtitleSize
    }

    var fontPath = ""
    var fontStyle = Typeface.NORMAL
    var headSize = 20f
    var subtitleSize = 17f
}


data class BottomView(
    @DrawableRes var image: Int?,
    var text: String,
    var listener: View.OnClickListener
) {
    @LayoutRes var itemViewId: Int = R.layout.bottom_sheet_field
    var id = -1
    var imageBound = ImageBounds.LEFT
    var fontPath = ""
    var fontStyle = Typeface.NORMAL
    var textSize = 18f
}


enum class ImageBounds {
    LEFT,
    RIGHT
}