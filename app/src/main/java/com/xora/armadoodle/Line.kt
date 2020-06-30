package com.xora.armadoodle

import android.graphics.Color.WHITE
import com.google.ar.core.Anchor
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class Line (private val arFragment: ArFragment) {
    private var vertices = ArrayList<AnchorNode>()
    private var segmentCenters = ArrayList<AnchorNode>()

    // Function to add a new point
    fun addVertex() {
        // Create a vertex and add it to the vertices list
        makeVertex()

        if (vertices.size == 1) return // Don't do anything if there's only one vertex
        // Otherwise, we're going to add a line segment between this point and the last one
        // ... IN THE NEXT EPISODE!
    }

    // Creates a sphere in the world, as a vertex point
    private fun makeVertex() {
        val anchor = getAnchor()
        val point: Vector3 = AnchorNode(anchor).worldPosition // Specified for documentation's sake more than anything

        MaterialFactory.makeOpaqueWithColor(arFragment.context, Color(WHITE))
            .thenAccept {
                addNodeToScene(anchor,
                ShapeFactory.makeSphere(0.1f, point, it))
            }
    }

    // Gets an anchor 0.5 meters away from camera.
    private fun getAnchor() : Anchor {
        // Get information about the camera and current session
        val frame = arFragment.arSceneView.arFrame!!
        val session = arFragment.arSceneView.session!!

        // Creates an anchor out of this position and returns it
        return session.createAnchor(
            frame.camera.pose
                .compose(Pose.makeTranslation(0f, 0f, -0.5f))
                .extractTranslation())
    }

    // Adds a node to the scene, with the object. Adds the node to vertices list for safekeeping.
    private fun addNodeToScene(anchor: Anchor, model: ModelRenderable) {
        val anchorNode = AnchorNode(anchor)

        TransformableNode(arFragment.transformationSystem).apply {
            renderable = model
            setParent(anchorNode)
        }

        arFragment.arSceneView.scene.addChild(anchorNode) // Add the node to the scene
        vertices.add(anchorNode)
    }
}