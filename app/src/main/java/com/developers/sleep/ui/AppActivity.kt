package com.developers.sleep.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apphud.sdk.Apphud
import com.developers.sleep.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        Apphud.start(this, "app_eLuEouByBRTL843jEsrLkrdhTGWopw")
        Apphud.collectDeviceIdentifiers()
    }
}
