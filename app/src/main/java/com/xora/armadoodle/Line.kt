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
    // Keeping track of our line points for deletion purposes
    private var vertices = ArrayList<AnchorNode>()
    private var segmentCenters = ArrayList<AnchorNode>()

    // Keeping track of our size variables for simplicity purposes
    private val lineRadius = 0.01f

    // Function to add a new point
    fun addVertex() {
        // Create a vertex and add it to the vertices list
        makeVertex()

        if (vertices.size == 1) return // Don't do anything if there's only one vertex
        // Otherwise, we're going to add a line segment between this point and the last one
        // ... It's now the next episode
    }

    // Creates a sphere in the world, as a vertex point
    private fun makeVertex() {
        val anchor = getAnchor()
        val point: Vector3 = AnchorNode(anchor).worldPosition // Specified for documentation's sake more than anything

        // TODO:
        //  To create custom shapes on runtime-rendered objects, you first have to manually create
        //  a dummy .sfb object, then clone the renderable. Link from creativetech in docs.
        MaterialFactory.makeTransparentWithColor(arFragment.context, Color(WHITE))
            .thenAccept {
                addNodeToScene(anchor,
                ShapeFactory.makeSphere(lineRadius, point, it))
            }
    }

    // Creates a line segment between the second-to-last vertex and the last vertex
    private fun makeSegment() {

    }

    // Gets an anchor 0.1 meter away from camera.
    private fun getAnchor() : Anchor {
        // Get information about the camera and current session
        val frame = arFragment.arSceneView.arFrame!!
        val session = arFragment.arSceneView.session!!

        // Creates an anchor out of this position and returns it
        return session.createAnchor(
            frame.camera.pose
                .compose(Pose.makeTranslation(0f, 0f, -1f))
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

    // TODO:
    //  Create some functionality to delete objeects, by looping through anchor nodes and deleting
    //  individual values.
}