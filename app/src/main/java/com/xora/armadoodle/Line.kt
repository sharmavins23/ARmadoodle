package com.xora.armadoodle

import android.graphics.Color.WHITE
import com.google.ar.core.Anchor
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class Line (private val arFragment: ArFragment) {
    // Fragment variables
    private val frame = arFragment.arSceneView.arFrame!!
    private val session = arFragment.arSceneView.session!!
    private val scene = arFragment.arSceneView.scene

    // Keeping track of our line points for deletion purposes
    private var vertices = ArrayList<AnchorNode>()
    private var segmentCenters = ArrayList<AnchorNode>()

    // Keeping track of our size variables for simplicity purposes
    private val lineRadius = 0.01f

    // Function to add a new point
    fun addVertex() {
        // Create a vertex and add it to the vertices list
        makeVertex()

        // Then, we're going to add a line segment between this point and the last one
        // The segment function will check if there aren't enough points
        makeSegment()
    }

    // Creates a sphere in the world, as a vertex point
    private fun makeVertex() {
        val anchor = getAnchor()
        val point: Vector3 = AnchorNode(anchor).worldPosition // Specified for documentation's sake more than anything

        // TODO:
        //  To create custom shapes on runtime-rendered objects, you first have to manually create
        //  a dummy .sfb object, then clone the renderable. Link from creativetech in docs.
        MaterialFactory.makeOpaqueWithColor(arFragment.context, Color(WHITE))
            .thenAccept {
                addNodeToScene(
                    anchor,
                    ShapeFactory.makeSphere(lineRadius, point, it).apply { // Force disable shadows
                        this.isShadowCaster = false
                        this.isShadowReceiver = false
                    }
                )
            }
    }

    // Creates a line segment (cylinder) between the second-to-last vertex and the last vertex
    private fun makeSegment() {
        if (vertices.size < 2) return; // Extra check to not get into any trouble

        val points = vertices.takeLast(2).map { it.localPosition } // Get the last two points out of the vertices list

        // Prepare an anchor position and pose
        val camQ: Quaternion = scene.camera.worldRotation // Quaternion describing the camera's current rotation
        val poseT: FloatArray = floatArrayOf(points[1].x, points[1].y, points[1].z) // Pose translation
        val poseR: FloatArray = floatArrayOf(camQ.x, camQ.y, camQ.z, camQ.w) // Pose rotation
        val anchorPose = Pose(poseT, poseR) // Create a pose out of the translation and rotation

        // Create an ARCore AnchorNode for the cylinder, in between the two points
        val anchorNode = AnchorNode(session.createAnchor(anchorPose))

        // Compute our line's length (and other interesting stuff!
        val lineLength = Vector3.subtract(points[0], points[1]).length() // Magnitude of the resolution vector

        // Make a material
        MaterialFactory.makeOpaqueWithColor(arFragment.context, Color(WHITE))
            .thenAccept {
                // Create a model cylinder with the material (not offloading to addNodeToScene due to complexity)
                val model = ShapeFactory.makeCylinder(
                    lineRadius,
                    lineLength,
                    Vector3(0f, lineLength / 2, 0f),
                    it // Material
                ).apply { // Force disable shadows
                    this.isShadowReceiver = false
                    this.isShadowCaster = false
                }

                // Make our transformable node
                val node = Node().apply {
                    renderable = model
                    setParent(anchorNode)
                }

                // Finally, set the rotation of the cylinder to line up with the points
//                node.worldRotation =
//                    Quaternion.multiply( // Don't ask me what most of this code does
//                        // It's some maths that I don't understand and probably should
//                        Quaternion.lookRotation( // This calculates the rotation from point 1 to point 2
//                            Vector3.subtract( // Subtract vectors (to - from) and normalize to get unit vector of direction
//                                points[1],
//                                points[0]
//                            ).normalized(),
//                            Vector3.up()
//                        ),
//                    Quaternion.axisAngle(Vector3(1.0f, 0.0f, 0.0f), 90f) // Not even gonna begin to guess what this is
//                    // A basis vector?
//                )

                val difference: Vector3 = Vector3.subtract(points[1], points[0])
                val directionFromTopToBottom: Vector3 = difference.normalized()
                val rotationFromAToB: Quaternion = Quaternion.lookRotation(
                    directionFromTopToBottom,
                    Vector3.up()
                )
                val axisAngleVector = Vector3(1.0f, 0.0f, 0.0f)
                node.worldRotation = Quaternion.multiply(
                    rotationFromAToB,
                    Quaternion.axisAngle(
                        axisAngleVector,
                        90f
                    )
                )

                scene.addChild(anchorNode) // Actually add the node to the scene
                segmentCenters.add(anchorNode) // Save to our list of segment centers for safe keeping
            }
    }

    // Gets an anchor away from camera.
    private fun getAnchor() : Anchor {
        // Creates an anchor out of this camera position and returns it
        return session.createAnchor(
            frame.camera.pose
                //.compose(Pose.makeTranslation(0f, 0f, -1f))
                .extractTranslation())
    }

    // Adds a node to the scene, with the object. Adds the node to vertices list for safekeeping.
    private fun addNodeToScene(anchor: Anchor, model: ModelRenderable) {
        val anchorNode = AnchorNode(anchor)

        Node().apply {
            renderable = model
            setParent(anchorNode)
        }

        scene.addChild(anchorNode) // Add the node to the scene
        vertices.add(anchorNode)
    }

    // TODO:
    //  Create some functionality to delete objeects, by looping through anchor nodes and deleting
    //  individual values.
}