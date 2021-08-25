package com.example.livedrawing

import android.graphics.Color
import android.graphics.Point
import android.graphics.PointF
import android.util.Log
import java.util.Random
import kotlin.math.absoluteValue

class Particle(direction: PointF, scrn: Point) {

    private val velocity: PointF = PointF()
    val position: PointF = PointF()
    private var originalVector: PointF = PointF()
    var red:Int = -1
    var green:Int = -1
    var blue:Int =-1
    val rand = Random()
    var walls:Boolean = true
    private val screenX = scrn.x
    private val screenY = scrn.y - 275

    private var redAnim:Boolean = true
    private var blueAnim:Boolean = true
    private var greenAnim:Boolean = true

    var previousVelocity:Float = 0f

    constructor(direction:PointF, scrn:Point, r:Int, g:Int, b:Int) : this(direction, scrn) {
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
        position.x += velocity.x
        position.y += velocity.y
        if (walls){
            if (position.x <= 5f){
                velocity.x *= -0.8f
                position.x = 5f
            }else if (position.x >= screenX - 5){
                velocity.x *= -0.8f
                position.x = screenX.toFloat()-5
            }
            if (position.x >= 0){

                position.x += velocity.x
            }
            else{
                position.x = 0f
            }
            if (position.y >= screenY){
                position.y = screenY.toFloat()
                velocity.y *= -1f
            }
        }
//        if (velocity.y <= 85f){
//            velocity.y += 1f
//        }
        velocity.y += 1f





        if (redAnim){
            if (red <= 252) red += 3
            else redAnim = !redAnim
        }else{
            if (red >= 3) red -= 3
            else redAnim = !redAnim
        }
        if (greenAnim){
            if (green <= 253) green += 2
            else greenAnim = !greenAnim
        }else{
            if (green >= 2) green -= 2
            else greenAnim = !greenAnim
        }
        if (blueAnim){
            if (blue <= 254) blue +=1
            else {
                blueAnim = !blueAnim
            }
        }else {
            if (blue >= 1) blue -=1
            else {
                blueAnim = !blueAnim
            }
        }

    }
    fun reset() {
        velocity.x = originalVector.x
        velocity.y = originalVector.y
        red = 25
        blue = 6
        green = 100
    }

}