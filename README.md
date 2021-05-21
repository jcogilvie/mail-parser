A kotlin `ktor`-based implementation of a code exercise to extract headers from POSTed text body.

# Layout:
`docker`: contains the `Dockerfile`.
`src/main/fabric8`: contains bits of helm chart that are 'enriched' by the maven build.
`src/main/kotlin`: juicy Kotlin code using `ktor` as a web app framework
`src/main/resources`: ktor and logging config files
`src/test/kotlin`: a quick unit test
`target`: build output will go here once you follow the usage instructions.
`target/docker`: intermediate directory for building the docker image
`target/fabric8`: k8s/helm gets generated here
`pom.xml`:  the [fabric8](https://maven.fabric8.io/) plugin here does most of the docker/k8s heavy lifting.

# Usage:

- have maven and prereqs installed (i.e. a jdk of some sort)
- have k8s running with kubectl authenticated
- $ `mvn clean package fabric8:deploy`
- wait for your pod(s) to come up: `kubectl get pods -w`
- web service will bind to port 30001.
- use your tool of choice (curl, postman, etc) to:
    - check the pod's health:  `GET k8shost:30001/health` -- should return `It's Alive!`
    - `POST` a body per the exercise instructions.  Should return something like:
    ```
    {
        "To": "citibanksingaporelimited@cp.monitor1.returnpath.net",
        "From": "customer.service@citicorp.com",
        "Date": "Fri, 1 Apr 2011 02:57:21 GMT",
        "Subject": "Citi Alerts",
        "Message-Id": "<1479419471.1301626641534.JavaMail.pjfpbg1@saixp36>"
    }
    ```

*Caveats*:  if you make changes to the image you'd want to run `fabric8:push`, which would want you to have docker creds
inside ~/.m2/settings.xml.  Although with things named as they are, docker.io convention means you'd be trying (and
failing) to push to my repo as me.  So you'd also need to change the image name in pom.xml (currently `jcogilvie/mail-parser`).

# Thoughts

The exercise itself was pretty straightforward but I opted to use standard ecosystem libs for as much as I could, which
means using mail4j to do MIME message parsing.  This simplifies some aspects but complicates others (like time zones).

It was my first time using kotlin `ktor` and I am pretty impressed.

The maven plugins for docker and k8s are rolled into one place (io.fabric8) which does just about everything you could want.
In fact, it does so much that I opted not to use some of its magic that could have streamlined things so that I could
show you I know how to write a `Dockerfile`.  I did let it generate half my k8s deployment.yml for me, though.  That's fun.

You can find the final, post-generation helm stuff (after you run `package`) inside `/target/fabric8`.

Hope you like it!


