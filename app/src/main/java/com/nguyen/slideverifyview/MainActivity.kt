package com.nguyen.slideverifyview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.nguyen.verifyview.VerifyCheckedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyView.setVerifyCheckedListener(object : VerifyCheckedListener {
            override fun onChecked() {
                verifyView.text = "Xác thực thành công"
                verifyView.textColor = ContextCompat.getColor(applicationContext, R.color.white);
                Toast.makeText(applicationContext, "Checked", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
