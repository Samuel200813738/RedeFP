package com.example.redefp

import android.app.Application
import com.cloudinary.android.MediaManager

class CloudinaryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = HashMap<String, String>()

        config["cloud_name"] = "dkpgp1dn9"
        config["api_key"] = "373889387972914"
        config["api_secret"] = "g0BNmESEAmUFukQ-KaTJlz6hqG4"

        MediaManager.init(this, config)
    }
}