package com.example.livedrawing

import android.graphics.*
import java.util.*  //Well then... java.util.* contains
import kotlin.concurrent.thread
import kotlin.math.absoluteValue

class ParticleSystem (scrn:Point){
    private var alpha: Int = 225
    private var duration: Float = 0f
    private var particles: ArrayList<Particle> = ArrayList()
    private val random = Random()
    private val screen = scrn
    var isRunning = false
    val screenX = scrn.x
    val screenY = scrn.y



    fun initParticles(numParticles:Int){
        for (i in 0 until numParticles) {
            var angle: Double = random.nextInt(360).toDouble()
            angle *= (3.14 / 180) // pi / 180 degrees = angle in radians

            //val speed = random.nextFloat() / 3
            val speed = random.nextInt(10)+1

            val direction: PointF = PointF(Math.cos(angle).toFloat()*speed, Math.sin(angle).toFloat() * speed)

            particles.add(Particle(direction,screen, 166, 55, 5))
        }
    }

    fun update(fps: Long) {
        duration -= 1f / fps
        for (p in particles){
            p.update()
        }
        if (duration < 0) {
            fizzle(fps)
        }
    }

    fun fizzle(fps:Long){
        duration -= 1f / fps
        for (p in particles){
            alpha = 255 - (duration.absoluteValue/2*225).toInt()
        }
        if (duration < -2 && isRunning){
            alpha = 0
            isRunning = false
        }
    }

    fun emitParticles(startPosition: PointF) {
        alpha = 255
        isRunning = true
        duration = 300f
        //duration = 2f

        for (p in particles) {
            p.reset()
            p.position.x= startPosition.x
            p.position.y = startPosition.y
        }
    }

    fun paintOptions(choice: Int): Paint {
        val paint = Paint()
        when (choice) {
            1 ->
                for (p in particles) {
                    //particleColor
                    paint.setARGB(alpha, random.nextInt(256), random.nextInt(256),random.nextInt(256))
                    //paint.setARGB(255,255,255,255)

                }
            2 ->
                for (p in particles) {
                    paint.setARGB(alpha,255,255,255)
                }
        }
        return paint
    }

    fun draw(canvas: Canvas) {


        val paint = Paint()
        for (p in particles) {
            //particleColor
            paint.setARGB(alpha, p.red, p.green, p.blue)
            //paint.setARGB(255,255,255,255)

            //particle size
//            val sizeX = 25f
//            val sizeY = 25f

//            val sizeX = 10f
//            val sizeY = 10f

            val sizeX = 2f
            val sizeY = 2f

            //canvas.drawRect(p.position.x, p.position.y, p.position.x + sizeX, p.position.y + sizeY, paint)
//            if (p.position.x <= 0) p.position.x = 0f
//            else if (p.position.x >= screenX-5) p.position.x = screenX-5f
//            if (p.position.y >= screenY) p.position.y = screenY.toFloat()
            canvas.drawCircle(p.position.x, p.position.y, (sizeX + sizeY) / 2,paint)
        }
    }

}