package com.example.thread

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var isRunning = true

    //아래 2개 비교
    var handler: Handler? = null
    lateinit var handler_2: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //버튼 클릭해서 클릭 시의 시간을 textView 에 입력
        button1.setOnClickListener { view ->
            var now = System.currentTimeMillis()
            text1.text = "버튼클릭 : $now"
        }


        /*
        //1. Main Thread 에서 loop 돌아가면 loop 도는동안 아무 것도 화면에 출력되지 않음
        while (true) {
            var now = System.currentTimeMillis()
            text2.text = "루프 : $now"
        }
        */

        /*
        //2. Thread 에서 바로 UI 에 접근 안됨
        var thread = Thread1()
        thread.start()
        */

        /*
        //3. Thread 내부에서 계속 post 호출해서 loop 돌림
        handler = Handler()
        var thread2 = Thread2()
        handler?.post(thread2)
        */

        //Thread 를 시작한다.
        //Thread 안에서 handler 로 메시지를 전송한다.
        //Handler에서 UI변경 작업을 진행한다.

        //4. Handler - sendEmptyMessage 통해 UI 처리
        handler = HanlderClass_1()
        var thread = Thread4()
        //thread.start()

        //5. Handler - sendMessage 통해 UI 처리

        handler_2 = HanlderClass_2()
        var thread_2 = Thread4_1()
        thread_2.start()


    }

    inner class Thread1 : Thread() {
        override fun run() {
            while (isRunning) {
                SystemClock.sleep(100)
                var now = System.currentTimeMillis()
                Log.d("test", "현재시간 + $now")
                //Thread 에서 바로 UI 에 접근 안됨
                text2.text = "루프 : $now"
            }
        }
    }

    inner class Thread2 : Thread() {
        override fun run() {
            SystemClock.sleep(100)

            var now = System.currentTimeMillis()
            Log.d("test", "현재시간 + $now")
            text2.text = "루프 : $now"
            //while 사용 대신 계속 handler 로 post
            handler?.post(this)
        }
    }

    inner class HanlderClass_1 : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            var now = System.currentTimeMillis()
            text2.text = "루프 : $now"
        }
    }

    inner class Thread4 : Thread() {
        override fun run() {
            while (isRunning) {
                SystemClock.sleep(100)
                var now = System.currentTimeMillis()
                Log.d("test", "현재시간 + $now")

                handler?.sendEmptyMessage(0)
            }
        }
    }

    inner class HanlderClass_2 : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            //var now = System.currentTimeMillis()
            text2.text = msg?.obj.toString()
        }
    }

    inner class Thread4_1 : Thread() {
        override fun run() {
            while (isRunning) {
                SystemClock.sleep(100)
                var now = System.currentTimeMillis()
                Log.d("test", "현재시간 + $now")

                var msg = Message()
                msg.what = 0
                msg.obj = now

                handler_2?.sendMessage(msg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }
}
