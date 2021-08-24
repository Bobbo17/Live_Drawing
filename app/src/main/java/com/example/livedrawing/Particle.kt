package com.example.livedrawing

import android.graphics.Color
import android.graphics.PointF
import java.util.Random
import kotlin.math.absoluteValue

class Particle(direction: PointF) {

    private val velocity: PointF = PointF()
    val position: PointF = PointF()
    private var originalVector: PointF = PointF()
    var red:Int = -1
    var green:Int = -1
    var blue:Int =-1
    val rand = Random()
    var walls:Boolean = true
    private val sizeX = MainActivity().
    private val sizeY = MainActivity().

    var previousVelocity:Float = 0f

    constructor(direction:PointF, r:Int, g:Int, b:Int) : this(direction) {
        red = r
        green = g
        blue = b
    }


    init {
        velocity.x = direction.x
        velocity.y = direction.y
        originalVector = direction
        if (red == -1) red = rand.nextInt(255) + 1
        if (green == -1) green = rand.nextInt(255) + 1
        if (blue == -1) blue = rand.nextInt(255) + 1
    }

    fun update() {
        if (walls){
            if (position.x <= 5f){
                velocity.x *= -0.8f
                position.x = 5f
            }else if (position.x >= sizeX-5){
                velocity.x *= -0.8f
                position.x = sizeX.toFloat()-5
            }
            if (position.x >= 0){

                position.x += velocity.x
            }
            else{
                position.x = 0f
            }
            if (position.y >= sizeY){
                position.y = sizeY.toFloat()
                velocity.y *= -0.6f
            }
        }
        else{

        }
        position.x += velocity.x
        position.y += velocity.y
        if (velocity.y.absoluteValue < 1f && (position.y >= sizeY-3)){
            velocity.y = 0f
            position.y = sizeY.toFloat()
        }else{
            velocity.y += 1f
        }




        if (red <= 252) red += 3
        if (green <= 253) green += 2
        if (blue <= 254) blue +=1
    }
    fun reset() {
        velocity.x = originalVector.x
        velocity.y = originalVector.y
        red = 25
        blue = 6
        green = 100
    }
}