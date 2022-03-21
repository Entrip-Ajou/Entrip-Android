package ajou.paran.entrip

import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.testBtn).setOnClickListener{
            startActivity(Intent(this, PlannerActivity::class.java))
        }
    }
}