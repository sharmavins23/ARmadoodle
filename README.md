# ARmadoodle

Code: https://github.com/sharmavins23/ARmadoodle
Creator: Vins Sharma
Description: An AR application that draws points in 3D space.
Screenshots and Shareables: https://cdn.discordapp.com/attachments/714206938719715429/728786550263119932/Screenshot_20200703-213447.jpg
Status: Finished

All assets used were created by **[Poly by Google](https://poly.google.com/user/4aEd8rQgKu2)**. These were published under a Public/Remixable (CC-BY) license.

# Overview and Functionality

At this current stage, the program's line segments do not line up with the same anchor nodes and the world positioning of the dots.

This program gets an anchor in front of the camera and places a dot in space. After placing 2 dots and onwards, these dots' anchors will be used to calculate the positioning for the anchors for the line segments.

Lines are split into line objects, which store all of the vertices (points) and all of the segment anchors (points for the cylinders.)

# Coding Process

I started by creating the button assets for the application and laid out the frame for the app. I decided on using a Floating Action Button to draw, which can be modified later. The initial plan was to have a queue of points as well as a list of lines. As the button was held, the queue was populated. Once the button was released, this queue would be flushed, and the anchors would shift over to an individual point object.

This idea was later scrapped in favor of a different paradigm - On the button being pressed, an initial line object is created, which stores the points. The state of the application changes to 'currently drawing,' and the line object begins adding points upon every frame update.

Finally, lines would be connected using cylinder line segments, which are positioned in between points.

One of the things I found with the button layout was that FloatingActionButtons can actually be constrained using gravity instead of absolute positions. In future projects, I'll utilize this feature.

---

### Guides/References

-   [https://stackoverflow.com/questions/51951704/how-to-draw-line-between-two-anchors-in-sceneform-in-arcore](https://stackoverflow.com/questions/51951704/how-to-draw-line-between-two-anchors-in-sceneform-in-arcore)
    -   This is some of the basis I used to connect lines together.
-   [https://stackoverflow.com/questions/56679208/placing-objects-right-in-front-of-camera-in-arcore-android](https://stackoverflow.com/questions/56679208/placing-objects-right-in-front-of-camera-in-arcore-android)
    -   This is the basis I used to create points in front of the camera. One of the major issues with the point creation is that it doesn't properly create a point in front of the camera, but instead it creates the point in front of the phone.
-   [https://stackoverflow.com/questions/47170075/kotlin-ontouchlistener-called-but-it-does-not-override-performclick](https://stackoverflow.com/questions/47170075/kotlin-ontouchlistener-called-but-it-does-not-override-performclick)
    -   This is a link I used to debug the issue with the FloatingActionButton.
-   [https://github.com/mickod/LineView](https://github.com/mickod/LineView)
    -   This is a sample project with object positioning and lines connecting them.
-   [https://github.com/Hariofspades/ARExperiments/blob/master/dynamic-shape-rendering/app/src/main/java/com/hariofspades/dynamicshaperendering/ARActivity.kt](https://github.com/Hariofspades/ARExperiments/blob/master/dynamic-shape-rendering/app/src/main/java/com/hariofspades/dynamicshaperendering/ARActivity.kt)
    -   This is a site I referenced when dynamically creating the points and line segments.
-   [https://creativetech.blog/home/arcore-shapes-materials](https://creativetech.blog/home/arcore-shapes-materials)
    -   This is the project guide site for the previous listing.

# Errors Encountered

### [Com.Google.Android.Material] FloatingActionButton's release listener is not working.

When working with the MotionEvents for FloatingActionButtons, the release listener was not properly functioning. This is because a DOWN MotionEvent must have True returned from it, or else other UP events are never called.

# License TL;DR

This project is distributed under the MIT license. This is a paraphrasing of a
[short summary](https://tldrlegal.com/license/mit-license).

This license is a short, permissive software license. Basically, you can do
whatever you want with this software, as long as you include the original
copyright and license notice in any copy of this software/source.

## What you CAN do:

-   You may commercially use this project in any way, and profit off it or the
    code included in any way;
-   You may modify or make changes to this project in any way;
-   You may distribute this project, the compiled code, or its source in any
    way;
-   You may incorporate this work into something that has a more restrictive
    license in any way;
-   And you may use the work for private use.

## What you CANNOT do:

-   You may not hold me (the author) liable for anything that happens to this
    code as well as anything that this code accomplishes. The work is provided
    as-is.

## What you MUST do:

-   You must include the copyright notice in all copies or substantial uses of
    the work;
-   You must include the license notice in all copies or substantial uses of the
    work.

If you're feeling generous, give credit to me somewhere in your projects.
