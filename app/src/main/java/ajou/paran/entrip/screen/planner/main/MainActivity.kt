package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.R
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        viewModel.insert()
        findViewById<Button>(R.id.testBtn).setOnClickListener{
            startActivity(Intent(this, PlannerActivity::class.java))
        }
    }
}