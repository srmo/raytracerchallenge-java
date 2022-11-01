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

--srm
