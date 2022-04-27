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

        val intent = Intent(this, PlannerActivity::class.java)

        findViewById<Button>(R.id.testBtn1).setOnClickListener{
            // 플래너 추가
            viewModel.createPlanner()
        }
        findViewById<Button>(R.id.testBtn2).setOnClickListener{
            // 플래너 4
            intent.putExtra("plannerId", 4L)
            startActivity(intent)
        }
        findViewById<Button>(R.id.testBtn3).setOnClickListener{
            // 플래너 5
            intent.putExtra("plannerId", 6L)
            startActivity(intent)
        }
    }
}