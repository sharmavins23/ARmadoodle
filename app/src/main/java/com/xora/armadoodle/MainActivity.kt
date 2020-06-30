package com.xora.armadoodle

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment

    private var isDrawing: Boolean = false

    private var lines = ArrayList<Line>()

    @SuppressLint("ClickableViewAccessibility") // Turning off android studio's obsession with Parcelables
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize our ARFragment, add our frame listener
        arFragment = sceneform_fragment as ArFragment
        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            arFragment.onUpdate(frameTime)
            onUpdate() // Calls function to process on each update
        }

        // Button listeners!
        drawButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_BUTTON_PRESS -> {
                    isDrawing = true // We're drawing now
                    // Create a new line object, add it to lines ArrayList
                    lines.add(Line(arFragment)) // Create a new line and add to the list
                    lines.last().addVertex() // Then add a point!
                }
                MotionEvent.ACTION_BUTTON_RELEASE -> {
                    isDrawing = false
                }
            }

            v?.onTouchEvent(event) ?: true
        }

        // Delete the last line
        trashButton.setOnClickListener {
            // Trash the last line in the list
            if (lines.isEmpty()) return@setOnClickListener // If empty, don't do anything
            // Call the line remove function
            lines.removeAt(lines.lastIndex)
        }
    }

    // Function that calls other helpers on every frame update
    private fun onUpdate() {
        if (lines.isEmpty()) return // Don't do anything if there aren't lines
        if (!isDrawing) return // Don't do anything if not drawing

        lines.last().addVertex() // If drawing, add a new point
    }
}