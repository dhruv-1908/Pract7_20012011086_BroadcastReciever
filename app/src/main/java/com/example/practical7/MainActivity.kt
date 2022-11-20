package com.example.practical7

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import com.example.practical7.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent as Intent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textClock = binding.textclock
        val pickerBtn = binding.pickerBtn
        val cancelBtn = binding.cancelBtn
        binding.card2.visibility = View.INVISIBLE
        textClock.format12Hour = "hh:mm:ss a MMM, dd yyyy"

        pickerBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val timepicker = TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,min)
                sendDialogDataToActivity(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
                binding.subheading2.text = SimpleDateFormat("hh:mm").format(cal.time)
            }
            TimePickerDialog(this,timepicker,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),false).show()
            binding.card2.visibility = View.VISIBLE
        }
        cancelBtn.setOnClickListener {
            setAlarm(null, "Cancel")
            binding.card2.visibility = View.INVISIBLE
        }
    }
    fun sendDialogDataToActivity(hour: Int, minute: Int) {
        val cal = Calendar.getInstance()
        val year:Int = cal.get(Calendar.YEAR)
        val month:Int = cal.get(Calendar.MONTH)
        val day:Int = cal.get(Calendar.DATE)
        cal.set(year,month,day,hour,minute,0)
        setAlarm(cal.timeInMillis,"Start")
    }
    fun setAlarm(millisTime: Long?, str:String) {
        var i = Intent(applicationContext, MyBroadcastReceiver::class.java)
        val pi:PendingIntent = PendingIntent.getBroadcast(applicationContext,234324243,i,0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if(str== "Start"){
            if (millisTime != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,millisTime,pi)
            }
        } else if (str == "Cancel") {
            alarmManager.cancel(pi)
        }
    }
}
