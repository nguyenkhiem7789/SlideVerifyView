package com.nguyen.slideverifyview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nguyen.verifyview.VerifyCheckedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyView.setVerifyCheckedListener(object : VerifyCheckedListener {
            override fun onChecked() {
                Toast.makeText(applicationContext, "Checked", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
