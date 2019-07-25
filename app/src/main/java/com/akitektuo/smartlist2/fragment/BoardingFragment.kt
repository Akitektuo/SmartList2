package com.akitektuo.smartlist2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.Constants.Companion.KEY_DESCRIPTION
import com.akitektuo.smartlist2.util.Constants.Companion.KEY_IMAGE_ID
import com.akitektuo.smartlist2.util.Constants.Companion.KEY_TITLE
import kotlinx.android.synthetic.main.fragment_boarding.*

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 08:35.
 */
class BoardingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_boarding, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(arguments!!) {
            val imageId = getInt(KEY_IMAGE_ID)
            val title = getString(KEY_TITLE)
            val description = getString(KEY_DESCRIPTION)
            imageBoarding.setImageResource(imageId)
            textBoardingTitle.text = title
            textBoardingDescription.text = description
        }
    }

}