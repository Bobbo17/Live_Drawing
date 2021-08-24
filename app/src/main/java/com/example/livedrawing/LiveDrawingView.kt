package com.example.livedrawing

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView

class LiveDrawingView (context:Context, screenX: Int): SurfaceView(context), Runnable {
    private val debugging = true

    private lateinit var canvas: Canvas
    private val paint = Paint()

    private var fps: Long = 0

    private val milInSecs: Long = 1000

    private val fontSize: Int = screenX / 20
    private val fontMargin: Int = screenX /75

    private var btnReset: RectF
    private var btnTogglePause: RectF

    private val particleSystems = ArrayList<ParticleSystem>()

    private var nextSystem = 0
    private val maxSystems = 1000
    private val particlesPerSystem = 25
    private val maxOnScreenSystems = 400

    private lateinit var thread: Thread

    @Volatile //can be accessed inside and outside the thread
    private var drawing: Boolean = false
    private var paused = false

    init {
        btnReset = RectF(0f,0f,100f,100f)
        btnTogglePause = RectF(0f,150f,100f,250f)

        for (i in 0 until maxSystems) {
            particleSystems.add(ParticleSystem())
            particleSystems[i].initParticles(particlesPerSystem)
        }
    }

    override fun run() {
        //using a boolean allows for finer control, booleans can be switched very quickly
        while (drawing) {
            //detect the amount of time that has passed since the previous frame for animation smoothness
            val frameStartTime = System.currentTimeMillis()

            //if app isn't paused, then allow frame to be updated
            if (!paused) {
                update()
            }
            draw()
            val timeThisFrame = System.currentTimeMillis() - frameStartTime

            if (timeThisFrame > 0) {
                fps = milInSecs/timeThisFrame
            }
        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN){
            if (btnReset.contains(motionEvent.x, motionEvent.y)) {
                nextSystem = 0
            }
            else if (btnTogglePause.contains(motionEvent.x, motionEvent.y)) {
                paused = !paused
            }
            else{
                particleSystems[nextSystem].emitParticles(PointF(motionEvent.x, motionEvent.y))
                nextSystem++
                if (nextSystem == maxSystems) {
                    nextSystem = 0
                }
            }

        }
        else if (motionEvent.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_MOVE ||
            motionEvent.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_POINTER_DOWN) {
            particleSystems[nextSystem].emitParticles(PointF(motionEvent.x, motionEvent.y))
            nextSystem++
            if (nextSystem == maxSystems) {
                nextSystem = 0
            }
        }

        
        return true
    }

    fun pause(){
        drawing = false //thread doesn't always stop instantly
        try {
            thread.join() //stop the thread
        }catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }
    }

    fun resume() {
        drawing = true
        // Initialize the instance of Thread the thread will manage everything in this class
        thread = Thread(this)

        thread.start()
    }

    private fun draw(){
        if (holder.surface.isValid) {
            //Lock the canvas (graphics memory) ready to draw
            canvas = holder.lockCanvas()

            canvas.drawColor(Color.argb(255,0,0,0))

            paint.color = Color.argb(255,255,255,255)

            paint.textSize = fontSize.toFloat()

            if (nextSystem < maxOnScreenSystems){
                for (i in maxSystems - (maxOnScreenSystems - nextSystem) until maxSystems){
                    particleSystems[i].draw(canvas)
                }
                for (i in 0 .. nextSystem){
                    particleSystems[i].draw(canvas)
                }
            }
            else {
                for (i in nextSystem-maxOnScreenSystems .. nextSystem) {
                    particleSystems[i].draw(canvas)
                }
            }


            canvas.drawRect(btnReset, paint)
            canvas.drawRect(btnTogglePause, paint)

            if (debugging) {
                printDebuggingText()
            }
            //Display the drawing on screen
            //unlockCanvasAndPost is a function of SurfaceHolder
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun update() {
        //
        for (i in 0 until particleSystems.size) {
            if (particleSystems[i].isRunning) {
                particleSystems[i].update(fps)
            }
        }
    }

    private fun printDebuggingText() {
        val debugSize = fontSize / 2
        val debugStart = 350
        paint.textSize = debugSize.toFloat()
        canvas.drawText("fps: $fps", 10f, (debugStart + debugSize).toFloat(), paint)
        canvas.drawText("Systems: ${nextSystem}", 10f, (fontMargin + debugStart + debugSize * 2).toFloat(), paint)
        canvas.drawText("Particles: ${nextSystem * particlesPerSystem}", 10f, (fontMargin + debugStart + debugSize * 3).toFloat(), paint)
    }

}