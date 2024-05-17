# BlogMultiplatform
Code generated following the course [Full Stack Kotlin Multiplatform KMP Development | Web Mobile](https://www.udemy.com/course/full-stack-kotlin-multiplatform-kmp-development-web-mobile).
The course aims to teach how to build and deploy a Blog Website, API, Admin Panel, and Android App 
with a Single Codebase.

This is a [Kobweb](https://github.com/varabyte/kobweb) project bootstrapped with the `app/empty` template.

You can check the site UI design here: [BlogMultiplatform Figma](https://www.figma.com/design/3haMLRlBL7I20mfdzDZ02c/Blog%2BApp)

## Tech Stack
It is a prototype app based on the following technologies:

1. [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
2. [Kobweb](https://github.com/varabyte/kobweb)
3. [KMongo](https://litote.org/kmongo/) at an early stage (deprecated now) and replaced with [MongoDB Kotlin Driver](https://www.mongodb.com/docs/drivers/kotlin/coroutine/current/)
4. [MongoDB Atlas](https://www.mongodb.com/products/platform/atlas-database)
5. [HumorAPI](https://humorapi.com/)
6. [Jetpack Compose](https://developer.android.com/courses/pathways/compose?hl=es-419)
7. [BuildKonfig](https://github.com/yshrsmz/BuildKonfig)
8. [Render.com](https://render.com/) for deploying the project
9. [Android Studio](https://developer.android.com/studio)

## Getting Started

The source code placed on the `main` branch contains the configuration to run the server locally.

As it was mentioned before, the app uses the [HumorAPI](https://humorapi.com/) to fetch, cache and display
random jokes. So in order to build the app you will need an API key,
and it should be placed in the [local.properties](local.properties) file, as next:

```properties

humorApiKey=yourApiKey

```

It also requires a connection to a remote database using [MongoDB Atlas](https://www.mongodb.com/products/platform/atlas-database).
So, in order to build and run the app, you also need to create your own cluster and place your 
MongoDB Atlas database connection uri in the [local.properties](local.properties) file, as next:

```properties

mongoDbConnectionUri=mongodb+srv://<username>:<password>@<clusterName>.mongodb.net

```

You can check the official documentation here: [Connect via Drivers](https://www.mongodb.com/docs/atlas/driver-connection/)

### Run the server

First, run the development server by typing the following command in a terminal under the `site` folder:

```bash
$ cd site
$ kobweb run
```

Open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

The site also provides an administration panel which is accessible from 
[http://localhost:8080/admin](http://localhost:8080/admin)

You can use any editor you want for the project, but we recommend using **IntelliJ IDEA Community Edition** downloaded
using the [Toolbox App](https://www.jetbrains.com/toolbox-app/).

Press `Q` in the terminal to gracefully stop the server.

### Live Reload

Feel free to edit / add / delete new components, pages, and API endpoints! When you make any changes, the site will
indicate the status of the build and automatically reload when ready.

## Exporting the Project

When you are ready to ship, you should shutdown the development server and then export the project using:

```bash
kobweb export
```

When finished, you can run a Kobweb server in production mode:

```bash
kobweb run --env prod
```

If you want to run this command in the Cloud provider of your choice, consider disabling interactive mode since nobody
is sitting around watching the console in that case anyway. To do that, use:

```bash
kobweb run --env prod --notty
```

Kobweb also supports exporting to a static layout which is compatible with static hosting providers, such as GitHub
Pages, Netlify, Firebase, any presumably all the others. You can read more about that approach here:
https://bitspittle.dev/blog/2022/staticdeploy

### Run the Android app

With the server running you can select from the Run/Debug configurations at the top, the `androidapp`,
and run it no your Android emulator or a real device.

## Deploy the app on [Render.com](https://render.com/)

The source code placed on the `deploy` branch contains the configuration needed to deploy and run
the site on [Render.com](https://render.com/). 
You can see a running example here: https://blogmultiplatform-2kd0.onrender.com/

If you want to deploy your own project using your own GitHub repository (from this source code), 
you should edit the file [conf.yaml](site/.kobweb/conf.yaml), changing the url with your own:

```yaml

  cors:
    hosts:
      - name: "blogmultiplatform-2kd0.onrender.com" #<-- Edit this url
        schemes:
          - "https"

```
