# Common stuff
## Setup
* standard maven setup
* for jcublas support on nvidia (windows) 
  * you need cublas64_11.dll from Cuda11 in your java.library path. I downloaded and installed CUDA 11 and copied the dll manually 
  * [CUDA Download Page](https://developer.nvidia.com/cuda-downloads?target_os=Windows&target_arch=x86_64&target_version=11)
  * I used `<project_jdk_home>/bin` as location
  



# All aboard the Raytracer-Challenge-Train
## Why?
I saw this TikTok where someone mentioned the Book ["The Ray Tracer Challenge" by Jamis Buck"](http://raytracerchallenge.com/).
For one, I've been missing focused coding on the job since I moved to Operations. 
Also, I was always wondering how raytracing actually works, i.e. what would an implementation look like. 
I was aware of the basics, like, cast a ray, see where it hits something, draw a pixel there.
Obviously, this isn't enough to go and implement this thing. So this video opened an opportunity to scratch these itches.

## What?
I considered using Python, as I'm using it mostly for my current tasks on the job. Then I considered Rust because I've never worked with it.
Then I wanted to use Go, as I already had some experience with it. C also crossed my mind.
In the end, I decided to use Java for the first implementation.
It's the language I have the most experience with as I've been coding it on the job for 10 years until I moved top Operations a few years back.
When I left the Java Ecosystem, Java 11 was on the rise and seeing, that we already have 18 released made the decision easier.
Thought being "Hey, what's going on there and is there anything I should freshen up?".

So here we go, a simple Maven Project. Latest jUnit 5 and, as demanded by the book, a TDD approach.

If you look at the code and wonder why I'm implementing all this basic stuff by foot: yes, I'm aware that there are powerful libraries for vector and matrix computations.
But where's the fun in that? This code isn't looking for optimisations and crazy magic.
All implementations follow a naive approach at this point to try and keep things simple.
That's why you will not find crazy inheritance trees or abstractions at this stage. They will come later when needed.

Documentation will be rather sparse. Well, yeah. That's it for now. Have fun checking it out or, even better, start your own journey!

## Resources
* The amazing 3Blu1Brown on YouTube has a great series on linear algebra. 
  * https://www.youtube.com/playlist?list=PLZHQObOWTQDPD3MizzM2xVFitgF8hE_ab 


## Journal
Decided to add some sort of journal to write down thoughts and ideas and problems I encountered. This will not be written on a regular basis but when I feel like it.
I wonder if anyone ever finds this and thinks, this helpful. Until then, I'll take it as a practice to jot down my thoughts.
### Day 4
Okay. I hit a brick wall when I encountered the section about intersections (pages 58ff).
It started pretty easy, like, hey! Have an intersection that calculates the intersect-points for a ray and a sphere!

Implementing Ray and Sphere was too trivial (yeah, let's see when it bites me). And a simple class for an Intersection too.
Let it calculate the intersection for a ray and sphere and store it. Return some array with the intersection points.
Right?

No. The next steps in the book suddenly talked about tracking intersections. Having a function "intersect(ray, sphere)" (see above) 
and now function "intersection(t, sphere)" and intersections(intersection...) and ... what the heck?
What is this? Why "intersection(t, sphere)"? Why isn't the intersection I just implemented enough? Where is the ray?
And why do I now need a holder of intersections that I can ask all sorts of silly questions, like, is there a hit in a set of interactions?

Sleep.

### Day 5.1
Well, a good nights sleep is always helpful. I re-read the section about "keeping track of intersections".
It would be wrong for me to say that I understand it, but I figured, hey, trust the process! 

That's when the refactoring kicks in.

First, start with some sane packages to cluster my classes. Geometry? Check. Stuff for Tracing? Check.

Hey! Let's do some premature optimisation and abstract the Sphere into a `GeometryObject` interface. 
Now let's use this in my intersections. Also, move the intersection-calculation into the `GeometryObject`. 
It should be fair to anticipate different Object with different intersection algorithms (unless of course it gets really wild and all we will ever use is the unit sphere and transform it into whatever shape we need...).

This also allows us to refactor `Intersection` into something that just keeps track of the intersected object and the *ticks* it took.
I guess, at this point we just don't care about which ray was responsible for the intersection. "We'll cross this bridge when we get there".

Last piece is the `IntersectionTracker`, which does it exactly what it says. It keeps track of all intersections per `GeometryObject`.

### Day 5.2
Just when I thought I had it. The next section starts. We are now at "transforming rays and spheres" and a little box in the book, titled *World Space vs. Object Space* - I don't get it, my head spins.
I wonder if I can find intuitive access to this.

I decided to increase `EPSILON` for my matrices. I just feel itchy when I see all those doubles, confined to the Epsilon of my meager doubles.

Yeah! Let's use a logging framework. Yes, the corporate is all SLF4J and stuf, but I just found [TinyLog](https://tinylog.org).
Let's try this (found via german blog https://blog.doubleslash.de/welches-logging-framework-fuer-java-nutzen/). It talked about "for small projects", what a perfect fit!
Begone SOUT!

... ... Wow. It RENDERS THE SHADOW OF A SPHERE! YAY! Seriously, I'm grinning from ear to ear. This is amazing.
Also the little ideas from the book at the end of the chapter, that says to play with a few transformations of the sphere and gives examples. Crazy! Magic! Wooohooo!

On a more serious note: I still can't wrap my head around this rendering on a canvas with Y=0 at the top-left, world coordinates, something about rendering based on "half" of the scene etc.
I guess it's important to play around with the `RenderSphereShadow`-Renderer, maybe debug it, to get a better grasp of the concepts.
Or even trying to optimize it, like, why not start rendering from the origin outwards until there's no intersection? Then again, we would need to rotate the ray for every possible angle, so that's non-trivial too, at least for me.
But it would definitely deepen my understanding and even reduce render time. a 500x500 rendering takes 5 seconds. He.

Then again, I know myself and already hear me thinking: nah, let's move on! The next chapters will help with understanding!

So long :)

### Day X
Okay, I lost track of the days. Let's start with a new age, as we had many sweeping changes.
The amazing news first: we can now render full spheres! With lighting/shading and stuff! Wooohoooo! 

I'm still amazed by the matrix magic. Just use this transformation-matrix and your sphere becomes a deformed blob, still shaded beautifully.

First, the rendering took 4-6 seconds. Well, that's slow, it's only 500x500 pixels. What the heck? Pulled out the profiling tools,
in this instance YourKit (I'm not sure if I will buy a license, it's so expensive!), and figured that much of the time was spent in matrix creation. 
More specific, in the handling of the multi-dimensional arrays!

Enter: jagged-arrays

While searching the interwebz for ideas, I learned that Java uses something called jagged-arrays. I.e. the language doesn't support "true" multidimensional arrays.
That means, depending on how most of the access on such an array is done, the performance becomes abysmal, at least in my oppinion (see [this stackoverflow](https://stackoverflow.com/questions/2512082/java-multi-dimensional-array-vs-one-dimensional) for a first glimpse). So here we are,
with a fresh implementation of 2D-Arrays as offset-based-1D implementation.

Additionally, I added parallel rendering, i.e. each parallel job gets a bunch of pixels to render. I see a bright future for more optimisations in the rendering :)

Then I hoped on to implement some of the new elements for cameras and what not.

During this, I decided to use `double` for all my floating point values. This also led me to extract `EPSILON` into a dedicated constants class.
Yes, I'm aware that it looks ugly to have a single class with just one constant, but that's what I'm going with at the moment.
I want every implementation to use the same precision for comparisons and when the time comes, the constants-class will get an appropriate name and home.

While doing that, I figured that I didn't use `Math.abs()` in the some of the `equals` implementations. Hey, better late then never, so I fixed that ...
That's when the tests went red.

Great, I ran around, all these iterations, with bogus tests, returning green when they should have been fiery red! 
Let that be a lesson, details matter and here, there's also me not really understanding what's going on. But fear not! 
The red tests helped me gain a better understanding of the geometry and what is happening. Still superficial, but more than the last time.

One last red test remained. My `SphereTest.normalNotOnAxis()` didn't want to go green. What was going on? For an hour or so I reverted, debugged, re-implemented, re-read the book,
back and forth but without success. I started looking up the raw formulas for "point on a sphere", understood that I'm working with a unit sphere here and the default transformation matrix is just the identity matrix,
and none of the calculated vectors in `Sphere.normalVectorAt(Tuple.point)` should be influenced by each other or by `normalize()`. But normalize changed the calculated vector.
Whaaaaaaat is going on?

Well, as I've said, after an hour I figured that I made an error while transcribing the test from the book. It's actually just a square-root. Not a cube-root.
Yes, that's all that was to it.
A standard bug, with the standard procedure and time spent on those kind of bugs :)
In the end, I got a better feeling about normals, unit-vectors and spheres and so this littly typo helped me a lot.

Looking forward to the next iteration, when we have a moving camera and more objects to render.
This is still fun!

Cheers!

### Day X+2
Yesterday and today I've been working on finalizing chapter 7. This was rather straight forward and no real hurdles along the way.
I find it amazing that we just take three spheres, "squash" and rotate them and just like that, they are 3 walls for our scene!
Looking forward to the day I get the hang of this and start creating transformations because I know what I'm doing.
Especially the part of shader pre-computation was interesting. It's something I've seen so many games, making me wait while 
it is "precomputing shaders". Still, it seems so simple, and it'll probably be getting out of hand soon.

Then I again saw those awful render-times. I played with parallel streams in the Sphere Renderer, but this time I wanted
to take it a step further. Remember benchmarking software or other renderers that render sections of the image in parallel?
Yeah, that's what I went with. Divide the canvas in chunks and create a thread per chunk. You "just" need to calculate the batch
of x and y pixels each chunk should process and off you go, thanks to Java's easy parallelStream() API.

I guess the chunk calculation is rather naive, but hey, I'm happy I worked it out.

Next step will be establishing `Point` and `Vector` as first class citizens. The idea is to make this interface/abstract driven,
so I can "easily" switch to a fastmath lib or something without major refactorings. There seems to be a handful of powerful
vector/matrix libraries out there. 

That's it, fun has been had and off to bed!


### Day X+3
If someone sent me a PR, that is as huge as this single commit, I'd go berserk. Then again, think of it as a squash commit.
Think of it of several small commits towards one goal:
*Introduce a third-party library for matrix/vector calculations and provide an interface to switch between implementations*

That's how we ended up with:
* `MatrixProvider.MatrixType.NAIVE`
* `MatrixProvider.MatrixType.EJML`

`NAIVE` is my original implementation, using only java primitives, wrapped in my own classes, Tuple and Matrix.
`EJML` is a library I found mentioned in a [Baeldung](https://www.baeldung.com/java-matrix-multiplication) post. 

First step was to move this stuff to a new package. `algebra` seems like a fitting name to me as it is pure calculation stuff.
The Provider architecture is rather raw, as is the mechanism to switch between Matrix implementations directly in the `MatrixProvider`
by setting a different enum value. But hey, this is ongoing work.

In the end, using EJML halves render-time which is great! Some of the implementations in the `EjmlMatrixWrapper` are rather ugly,
as a result of direct ancestry from the old Matrix. 

I guess, it will all fall in place when I start moving the calculation off of the CPU and into the GPU. That will be fun!

That's all for now, no new GeometryObjects, no new Worlds or Scenes, sorry. But sometimes we need to do the work.

Have fun!

