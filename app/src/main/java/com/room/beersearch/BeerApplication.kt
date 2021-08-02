package com.room.beersearch

import android.app.Application
import android.content.Context

class BeerApplication : Application() {

        init {
            instance = this
        }

        companion object {
            lateinit var instance: BeerApplication

            fun getContext(): Context {
                return instance.applicationContext
            }
        }
}