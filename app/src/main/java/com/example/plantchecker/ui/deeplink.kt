package com.example.plantchecker.ui.deeplink

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.plantchecker.R
import com.example.plantchecker.util.DeepLinkHelper

class DeepLinkDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link_demo)


        findViewById<Button>(R.id.btn_home_deeplink).setOnClickListener {
            startActivity(DeepLinkHelper.createHomeDeepLink())
        }

        findViewById<Button>(R.id.btn_add_plant_deeplink).setOnClickListener {
            startActivity(DeepLinkHelper.createAddPlantDeepLink())
        }

        findViewById<Button>(R.id.btn_add_plant_with_species_deeplink).setOnClickListener {
            startActivity(DeepLinkHelper.createAddPlantWithSpeciesDeepLink("Monstera Deliciosa"))
        }

        findViewById<Button>(R.id.btn_plant_detail_deeplink).setOnClickListener {

            val samplePlantId = "sample_plant_id"
            startActivity(DeepLinkHelper.createPlantDetailDeepLink(samplePlantId))
        }

        findViewById<Button>(R.id.btn_calendar_deeplink).setOnClickListener {
            startActivity(DeepLinkHelper.createCalendarDeepLink())
        }
    }
}