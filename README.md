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
Have it calculate the intersection for a ray and sphere and store it. Return some array with the intersection points.
Right?

No. The next steps in the book suddenly talked about tracking intersections. Having a function "intersect(ray, sphere)" (see above) 
and now function "intersection(t, sphere)" and intersections(intersection...) and ... what the heck?
What is this? Why "intersection(t, sphere)"? Why isn't the intersection I just implemented enough? Where is the ray?
And why do I now need a holder of intersections that I can ask all sorts of silly questions, like, is there a hit in a set of interactions?

Sleep.

### Day 5
Well, a good nights sleep is always helpful. I re-read the section about "keeping track of intersections".
It would be wrong for me to say that I understand it but I figured, hey, trust the process! 

That's when the refactoring kicks in.

First, start with some sane packages to cluster my classes. Geometry? Check. Stuff for Tracing? Check.

Hey! Let's do some premature optimisation and abstract the Sphere into a `GeometryObject` interface. 
Now let's use this in my intersections. Also, move the intersection-calculation into the `GeometryObject`. 
It should be fair to anticipate different Object with different intersection algorithms (unless of course it gets really wild and all we will ever use is the unit sphere and transform it into whatever shape we need...).

This also allows us to refactor `Intersection` into something that just keeps track of the intersected object and the *ticks* it took.
I guess, at this point we just don't care about which ray was responsible for the intersection. "We'll cross this bridge when we get there".

Last piece is the `IntersectionTracker`, which does it exactly what it says. It keeps track of all intersections per `GeometryObject`.
