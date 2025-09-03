@echo off
echo Creating PDFToPodcast project structure...
mkdir PDFToPodcast
cd PDFToPodcast
mkdir app
mkdir app/src
mkdir app/src/main
mkdir app/src/main/java
mkdir app/src/main/java/com
mkdir app/src/main/java/com/example
mkdir app/src/main/java/com/example/pdftopodcast
mkdir app/src/main/res
mkdir app/src/main/res/layout
mkdir app/src/main/res/xml
mkdir app/src/main/res/values

echo Creating code files...
echo // MainActivity.kt code here > app/src/main/java/com/example/pdftopodcast/MainActivity.kt
echo // activity_main.xml code here > app/src/main/res/layout/activity_main.xml
echo // strings.xml code here > app/src/main/res/values/strings.xml
echo // AndroidManifest.xml code here > app/src/main/AndroidManifest.xml
echo // build.gradle code here > app/build.gradle
echo // project build.gradle code here > build.gradle
echo // settings.gradle code here > settings.gradle
echo // README content here > README.md

echo Project structure created!
echo Now open GitHub Desktop to upload your project.
pause